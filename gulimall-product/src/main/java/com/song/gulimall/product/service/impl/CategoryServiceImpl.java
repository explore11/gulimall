package com.song.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.song.gulimall.product.service.CategoryBrandRelationService;
import com.song.gulimall.product.vo.Catalog2Vo;
import com.song.gulimall.product.vo.Catalog3Vo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.product.dao.CategoryDao;
import com.song.gulimall.product.entity.CategoryEntity;
import com.song.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedissonClient redissonClient;

    @Override
    @Transactional
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        //修改品牌分类关系表中的分类的名称
        categoryBrandRelationService.updateCategoryName(category.getCatId(), category.getName());
    }

    @Override
    @Cacheable(value = {"category"}, key = "#root.method.name")
    public List<CategoryEntity> getCategoryOneLevel() {
        return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }


    @Override
    public Map<Long, List<Catalog2Vo>> getCatalogJson() {
        List<CategoryEntity> categoryOneLevel = this.getCategoryOneLevel();
        // 组装结果返回
        return getCatalogListMapWithRedisLock(categoryOneLevel);
    }


    public Map<Long, List<Catalog2Vo>> getCatalogJsonFenBuShi() {

        String catalogJson = stringRedisTemplate.opsForValue().get("CatalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            // redis 分布式锁
            String uuid = UUID.randomUUID().toString();
            // 抢占锁 和设置时间必须是原子性的
            Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);

            Map<Long, List<Catalog2Vo>> map = null;
            if (lock) {
                // 为 true 说明获取分布式锁成功
                //获取一级分类
                // 加锁解决缓存穿透的问题
                List<CategoryEntity> categoryOneLevel = this.getCategoryOneLevel();
                // 组装结果返回
                try {
                    map = getCatalogListMapWithRedisLock(categoryOneLevel);
                    // 存到缓存中
                    String s = JSON.toJSONString(map);
                    // 设置不通的时间返回缓存雪崩的问题
                    stringRedisTemplate.opsForValue().set("CatalogJson", s, 1, TimeUnit.DAYS);
                } finally {
                    // lun 脚本
                    String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                            "    return redis.call(\"del\",KEYS[1])\n" +
                            "else\n" +
                            "    return 0\n" +
                            "end";
                    // Arrays.asList("lock") ==== KEYS[1]       uuid ==== ARGV[1]   lua脚本保证判断key和删除key的原子性操作
                    Long lock1 = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
                }
                return map;
            } else {
                // 获取失败 从新执行
                getCatalogJsonFenBuShi();
            }
        }

        return JSON.parseObject(catalogJson, new TypeReference<Map<Long, List<Catalog2Vo>>>() {
        });
    }

    private Map<Long, List<Catalog2Vo>> getCatalogListMapWithRedisLock(List<CategoryEntity> categoryOneLevel) {
        return categoryOneLevel.stream().collect(Collectors.toMap(CategoryEntity::getCatId, (categoryEntity) -> {
            List<Catalog2Vo> catalog2VoList = new ArrayList<>();

            // 获取二级分类
            List<CategoryEntity> categoryEntityTwoList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", categoryEntity.getCatId()));
            // 非空判断
            if (!CollectionUtils.isEmpty(categoryEntityTwoList)) {
                for (CategoryEntity entityTwo : categoryEntityTwoList) {
                    Catalog2Vo catalog2Vo = null;
                    // 获取三级分类
                    List<CategoryEntity> categoryEntityThreeList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", entityTwo.getCatId()));

                    if (!CollectionUtils.isEmpty(categoryEntityThreeList)) {
                        List<Catalog3Vo> catalog3VoList = categoryEntityThreeList.stream().map((categoryEntityThree) -> {
                            return new Catalog3Vo(entityTwo.getCatId().toString(), categoryEntityThree.getCatId().toString(), categoryEntityThree.getName());
                        }).collect(Collectors.toList());

                        // 二级分类的出参
                        catalog2Vo = new Catalog2Vo(categoryEntity.getCatId().toString(), catalog3VoList, entityTwo.getCatId().toString(), entityTwo.getName());
                        // 添加到二级分类列表
                        catalog2VoList.add(catalog2Vo);
                    }
                }
            }
            return catalog2VoList;
        }));
    }

    private Map<Long, List<Catalog2Vo>> getCatalogListMap(List<CategoryEntity> categoryOneLevel) {
        return categoryOneLevel.stream().collect(Collectors.toMap(CategoryEntity::getCatId, (categoryEntity) -> {
            List<Catalog2Vo> catalog2VoList = new ArrayList<>();

            // 获取二级分类
            List<CategoryEntity> categoryEntityTwoList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", categoryEntity.getCatId()));
            // 非空判断
            if (!CollectionUtils.isEmpty(categoryEntityTwoList)) {
                for (CategoryEntity entityTwo : categoryEntityTwoList) {
                    Catalog2Vo catalog2Vo = null;
                    // 获取三级分类
                    List<CategoryEntity> categoryEntityThreeList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", entityTwo.getCatId()));

                    if (!CollectionUtils.isEmpty(categoryEntityThreeList)) {
                        List<Catalog3Vo> catalog3VoList = categoryEntityThreeList.stream().map((categoryEntityThree) -> {
                            return new Catalog3Vo(entityTwo.getCatId().toString(), categoryEntityThree.getCatId().toString(), categoryEntityThree.getName());
                        }).collect(Collectors.toList());

                        // 二级分类的出参
                        catalog2Vo = new Catalog2Vo(categoryEntity.getCatId().toString(), catalog3VoList, entityTwo.getCatId().toString(), entityTwo.getName());
                        // 添加到二级分类列表
                        catalog2VoList.add(catalog2Vo);
                    }
                }
            }
            return catalog2VoList;
        }));
    }

    public Map<Long, List<Catalog2Vo>> getCatalogJsonSynchronized() {

        String catalogJson = stringRedisTemplate.opsForValue().get("CatalogJson");

        if (StringUtils.isEmpty(catalogJson)) {
//获取一级分类
            synchronized (this) {
                // 加锁解决缓存穿透的问题
                List<CategoryEntity> categoryOneLevel = this.getCategoryOneLevel();
                // 组装结果返回
                Map<Long, List<Catalog2Vo>> map = getCatalogListMap(categoryOneLevel);

                // 存到缓存中
                String s = JSON.toJSONString(map);
                // 设置不通的时间返回缓存雪崩的问题
                stringRedisTemplate.opsForValue().set("CatalogJson", s, 1, TimeUnit.DAYS);
                return map;
            }
        }

        return JSON.parseObject(catalogJson, new TypeReference<Map<Long, List<Catalog2Vo>>>() {
        });
    }

    public Map<Long, List<Catalog2Vo>> getCatalogJsonFromDb() {
        //获取一级分类
        List<CategoryEntity> categoryOneLevel = this.getCategoryOneLevel();
        // 组装结果返回
        Map<Long, List<Catalog2Vo>> map = getCatalogListMap(categoryOneLevel);
        return map;
    }

    /* *
     * 根据分类id 获取分类的三级目录
     * @param catelogId
     * @return
     */
    @Override
    public Long[] getCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        this.getParentPath(catelogId, paths);
        return paths.toArray(new Long[paths.size()]);
    }


    /* *
     * 递归获取父路径
     * @param catelogId
     * @param paths
     * @return
     */
    private List<Long> getParentPath(Long catelogId, List<Long> paths) {
        paths.add(0, catelogId);
        CategoryEntity categoryEntity = this.getById(catelogId);
        Long parentCid = categoryEntity.getParentCid();
        if (parentCid != 0) {
            this.getParentPath(parentCid, paths);
        }
        return paths;
    }

    /* *
     * 批量逻辑删除
     * @param catIds
     */
    @Override
    public void removeBatchIds(Long[] catIds) {
        baseMapper.deleteBatchIds(Arrays.asList(catIds));
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> getCategoryTree() {

        // 查询出全部数据
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);
        // 查询出一级分类
        List<CategoryEntity> levelOne = categoryEntityList.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(categoryEntity -> {
                    categoryEntity.setChildren(getChildren(categoryEntity, categoryEntityList));
                    return categoryEntity;
                })
                .sorted((category1, category2) -> {
                    return (category1.getSort() == null ? 0 : category1.getSort()) - (category2.getSort() == null ? 0 : category2.getSort());
                })  //按字段排序
                .collect(Collectors.toList());
        return levelOne;
    }

    /* *
     * 递归获取孩子
     * @param root
     * @param categoryEntityList
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> categoryEntityList) {
        // 一级分类的id 为其他的分类的父id
        List<CategoryEntity> collect = categoryEntityList.stream().filter(categoryEntity -> root.getCatId() == categoryEntity.getParentCid())
                .map(categoryEntity -> {
                    categoryEntity.setChildren(getChildren(categoryEntity, categoryEntityList));
                    return categoryEntity;
                })
                .sorted((category1, category2) -> {
                    return (category1.getSort() == null ? 0 : category1.getSort()) - (category2.getSort() == null ? 0 : category2.getSort());
                })
                .collect(Collectors.toList());
        return collect;
    }
}
