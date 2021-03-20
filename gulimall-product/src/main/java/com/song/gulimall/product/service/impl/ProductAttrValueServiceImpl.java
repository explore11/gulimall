package com.song.gulimall.product.service.impl;

import com.song.gulimall.product.dao.AttrDao;
import com.song.gulimall.product.entity.AttrEntity;
import com.song.gulimall.product.service.AttrService;
import com.song.gulimall.product.vo.spu.BaseAttrs;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.product.dao.ProductAttrValueDao;
import com.song.gulimall.product.entity.ProductAttrValueEntity;
import com.song.gulimall.product.service.ProductAttrValueService;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Resource
    AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttrValue(Long spuId, List<BaseAttrs> baseAttrs) {
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            List<ProductAttrValueEntity> productAttrValueEntityList = baseAttrs.stream().map((attr) -> {
                ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
                productAttrValueEntity.setSpuId(spuId);
                productAttrValueEntity.setAttrId(attr.getAttrId());
                AttrEntity attrEntity = attrDao.selectById(attr.getAttrId());
                productAttrValueEntity.setAttrName(attrEntity.getAttrName());
                productAttrValueEntity.setAttrValue(attr.getAttrValues());
                productAttrValueEntity.setQuickShow(attr.getShowDesc());
                return productAttrValueEntity;
            }).collect(Collectors.toList());
            this.saveBatch(productAttrValueEntityList);
        }
    }

    @Override
    public List<ProductAttrValueEntity> getProductAttrValue(Long spuId) {
        List<ProductAttrValueEntity> entityList = this.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return entityList;
    }

}
