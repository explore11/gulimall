package com.song.gulimall.product.service.impl;

import com.song.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.product.dao.CategoryDao;
import com.song.gulimall.product.entity.CategoryEntity;
import com.song.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);

        //修改品牌分类关系表中的分类的名称
        categoryBrandRelationService.updateCategoryName(category.getCatId(),category.getName());
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
