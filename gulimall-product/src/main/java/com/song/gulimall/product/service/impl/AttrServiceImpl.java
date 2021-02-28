package com.song.gulimall.product.service.impl;

import com.song.common.utils.ProductConstant;
import com.song.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.song.gulimall.product.dao.AttrGroupDao;
import com.song.gulimall.product.dao.CategoryDao;
import com.song.gulimall.product.entity.*;
import com.song.gulimall.product.service.CategoryService;
import com.song.gulimall.product.vo.AttrRespVo;
import com.song.gulimall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.product.dao.AttrDao;
import com.song.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Resource
    CategoryDao categoryDao;

    @Resource
    AttrGroupDao attrGroupDao;


    @Resource
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        // 关联保存属性、属性组表
        if (attrEntity.getAttrType() == ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getType() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrListPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", "base".equals(type) ? ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getType() : ProductConstant.AttrTypeEnum.ATTR_TYPE_SALE.getType());
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });

        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> attrEntities = page.getRecords();


        //设置出参
        List<AttrRespVo> attrRespVoList = attrEntities.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            if (attrEntity.getAttrType() == ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getType()) {
                //设置属性分组
                AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity groupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                    attrRespVo.setGroupName(groupEntity.getAttrGroupName());
                }
            }
            //设置分类
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVoList);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.baseMapper.selectById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVo);

        if (attrEntity.getAttrType() == ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getType()) {
            //设置属性分组
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (relationEntity != null) {
                attrRespVo.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity groupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                attrRespVo.setGroupName(groupEntity.getAttrGroupName());
            }
        }
        //设置分类
        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if (categoryEntity != null) {
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        // 分类完整路径
        Long[] catelogPath = categoryService.getCatelogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);
        return attrRespVo;
    }

    @Override
    @Transactional
    public void updateAttr(AttrRespVo attrRespVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrRespVo, attrEntity);
        this.baseMapper.updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getType()) {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrRespVo.getAttrId());
            relationEntity.setAttrGroupId(attrRespVo.getAttrGroupId());

            Integer count = this.baseMapper.selectCount(new QueryWrapper<AttrEntity>().eq("attr_id", attrRespVo.getAttrId()));
            if (count > 0) { // 更新
                attrAttrgroupRelationDao.update(relationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
            } else { // 保存
                attrAttrgroupRelationDao.insert(relationEntity);
            }
        }
    }

    @Override
    public PageUtils getNoAttrGroup(Long attrgroupId, Map<String, Object> params) {
        IPage<AttrEntity> page = null;
        // 查询当前分类下 其他分组 没有关联到的属性
        AttrGroupEntity groupEntity = attrGroupDao.selectById(attrgroupId);
        // 获取当前分类
        Long catelogId = groupEntity.getCatelogId();
        // 获取当前分类下 所有的分组
        List<AttrGroupEntity> groupEntityList = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 获取分组ids
        List<Long> groupIds = groupEntityList.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        // 根据分组id 查询属性id
        if (!CollectionUtils.isEmpty(groupIds)) {
            List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", groupIds));
            List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(attrIds)) {
                QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type", ProductConstant.AttrTypeEnum.ATTR_TYPE_BASE.getType()).eq("catelog_id", catelogId).notIn("attr_id", attrIds);

                String key = (String) params.get("key");
                if (StringUtils.isNotEmpty(key)) {
                    wrapper.and((queryWrapper) -> {
                        queryWrapper.eq("attr_id", key).or().like("attr_name", key);
                    });
                }
                page = this.page(new Query<AttrEntity>().getPage(params), wrapper);


            }
        }
        return new PageUtils(page);
    }

}
