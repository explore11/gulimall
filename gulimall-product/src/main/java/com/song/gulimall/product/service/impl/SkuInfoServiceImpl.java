package com.song.gulimall.product.service.impl;

import com.song.gulimall.product.entity.SkuImagesEntity;
import com.song.gulimall.product.entity.SpuInfoDescEntity;
import com.song.gulimall.product.service.*;
import com.song.gulimall.product.vo.SkuItemSaleAttrVo;
import com.song.gulimall.product.vo.SkuItemVo;
import com.song.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.product.dao.SkuInfoDao;
import com.song.gulimall.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Resource
    SkuImagesService skuImagesService;
    @Resource
    SpuInfoDescService spuInfoDescService;
    @Resource
    ProductAttrValueService productAttrValueService;
    @Resource
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveBaseSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.save(skuInfoEntity);
    }

    @Override
    public PageUtils queryInfoPage(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((queryWrapper) -> {
                queryWrapper.eq("id", key).or().like("spu_name", key);
            });
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }

        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            wrapper.ge("price", min);
        }

        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(min)) {
            BigDecimal bigDecimal = new BigDecimal(max);
            if (bigDecimal.compareTo(new BigDecimal("0")) > 0) {
                wrapper.le("price", max);
            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkuInfoListBySpuId(Long spuId) {
        return this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
    }

    /* *
     * 商品详情
     * @param skuId
     */
    @Override
    public SkuItemVo item(Long skuId) {
        // 组装数据
        SkuItemVo skuItemVo = new SkuItemVo();

        // 1、sku基本信息的获取
        SkuInfoEntity skuInfoEntity = this.baseMapper.selectById(skuId);
        skuItemVo.setInfo(skuInfoEntity);
        Long spuId = skuInfoEntity.getSpuId();
        Long catalogId = skuInfoEntity.getCatalogId();

        // 2、sku图片
        List<SkuImagesEntity> imagesEntityList = skuImagesService.getSkuImagesBySkuId(skuId);
        skuItemVo.setImages(imagesEntityList);

        // 3、spu的销售属性组合
        List<SkuItemSaleAttrVo> itemSaleAttrVoList = skuSaleAttrValueService.geSkuSaleAttr(spuId);
        skuItemVo.setSaleAttr(itemSaleAttrVoList);

        // 4、spu的介绍
        SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(spuId);
        skuItemVo.setDesc(spuInfoDescEntity);

        // 5、spu的规格参数信息
        List<SpuItemAttrGroupVo> spuItemAttrGroupVoList = productAttrValueService.getProductGroupBySpuId(spuId, catalogId);
        skuItemVo.setGroupAttrs(spuItemAttrGroupVoList);

        return skuItemVo;
    }
}
