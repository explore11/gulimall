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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

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
    @Resource
    ThreadPoolExecutor executor;

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
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {
        // 组装数据
        SkuItemVo skuItemVo = new SkuItemVo();

        // 使用异步编排优化代码
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            // 1、sku基本信息的获取
            SkuInfoEntity skuInfoEntity = this.baseMapper.selectById(skuId);
            skuItemVo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);


        CompletableFuture<Void> saleFuture = infoFuture.thenAcceptAsync((result) -> {
            // 3、spu的销售属性组合
            List<SkuItemSaleAttrVo> itemSaleAttrVoList = skuSaleAttrValueService.geSkuSaleAttr(result.getSpuId());
            skuItemVo.setSaleAttr(itemSaleAttrVoList);
        }, executor);


        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((result) -> {
            // 4、spu的介绍
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(result.getSpuId());
            skuItemVo.setDesc(spuInfoDescEntity);
        }, executor);


        CompletableFuture<Void> spuAttr = infoFuture.thenAcceptAsync((result) -> {
            // 5、spu的规格参数信息
            List<SpuItemAttrGroupVo> spuItemAttrGroupVoList = productAttrValueService.getProductGroupBySpuId(result.getSpuId(), result.getCatalogId());
            skuItemVo.setGroupAttrs(spuItemAttrGroupVoList);
        }, executor);


        CompletableFuture<Void> skuImgFuture = CompletableFuture.runAsync(() -> {
            // 2、sku图片
            List<SkuImagesEntity> imagesEntityList = skuImagesService.getSkuImagesBySkuId(skuId);
            skuItemVo.setImages(imagesEntityList);
        });



        // 所有任务完成后在执行
        CompletableFuture.allOf(saleFuture,descFuture,spuAttr,skuImgFuture).get();

        return skuItemVo;
    }
}
