package com.song.gulimall.product.service.impl;

import com.song.gulimall.product.vo.SkuItemSaleAttrVo;
import com.song.gulimall.product.vo.spu.Attr;
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

import com.song.gulimall.product.dao.SkuSaleAttrValueDao;
import com.song.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.song.gulimall.product.service.SkuSaleAttrValueService;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {
    @Resource
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuSaleAttrValue(Long skuId, List<Attr> skuAttrList) {
        if (!CollectionUtils.isEmpty(skuAttrList)) {
            List<SkuSaleAttrValueEntity> skuValeAttrValueEntities = skuAttrList.stream().map((skuAttr) -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                BeanUtils.copyProperties(skuAttr, skuSaleAttrValueEntity);
                skuSaleAttrValueEntity.setSkuId(skuId);
                skuSaleAttrValueEntity.setAttrId(skuAttr.getAttrId());
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());
            this.saveBatch(skuValeAttrValueEntities);
        }

    }

    @Override
    public List<SkuItemSaleAttrVo> geSkuSaleAttr(Long spuId) {
        List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueDao.geSkuSaleAttr(spuId);
        return saleAttrVos;
    }


    @Override
    public List<String> getSkuSaleAttrValuesAsString(Long skuId) {
        return baseMapper.getSkuSaleAttrValuesAsString(skuId);
    }
}
