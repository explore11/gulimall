package com.song.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.to.HasStockVo;
import com.song.common.to.SkuReductionInfoTo;
import com.song.common.to.SpuBoundsTo;
import com.song.common.to.es.SkuEsModel;
import com.song.common.utils.PageUtils;
import com.song.common.utils.ProductConstant;
import com.song.common.utils.Query;
import com.song.common.utils.R;
import com.song.gulimall.product.dao.SpuInfoDao;
import com.song.gulimall.product.entity.*;
import com.song.gulimall.product.feign.SearchFeignService;
import com.song.gulimall.product.feign.SpuCouponFeignService;
import com.song.gulimall.product.feign.WareFeignService;
import com.song.gulimall.product.service.*;
import com.song.gulimall.product.vo.spu.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Resource
    SpuImagesService spuImagesService;
    @Resource
    SpuInfoDescService spuInfoDescService;
    @Resource
    ProductAttrValueService productAttrValueService;
    @Resource
    SkuInfoService skuInfoService;
    @Resource
    SkuImagesService skuImagesService;
    @Resource
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    SpuCouponFeignService spuCouponFeignService;
    @Resource
    CategoryService categoryService;
    @Resource
    BrandService brandService;
    @Resource
    AttrService attrService;
    @Resource
    WareFeignService wareFeignService;
    @Resource
    SearchFeignService searchFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuInfoVo vo) {
        // 1、保存spu的基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        Long spuId = spuInfoEntity.getId();
        // 3、保存spu的描述信息 pms_spu_info_desc
        List<String> descs = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(String.join(",", descs));
        spuInfoDescService.saveSpuDesc(spuInfoDescEntity);


        // 2、保存spu的图片信息 pms_spu_images
        List<String> spuImages = vo.getImages();
        spuImagesService.saveSpuImage(spuId, spuImages);

        // 4、保存spu的积分信息 gulimall_sms库 sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuId);
        R r = spuCouponFeignService.saveSpuCoupon(spuBoundsTo);
        if (r.getCode() != 0) {
            log.info("saveSpuInfo 调用spuCouponFeignService 服务的saveSpuCoupon 方法失败 ");
        }

        // 5、保存spu的基本属性 pms_product_attr_value
        productAttrValueService.saveProductAttrValue(spuId, vo.getBaseAttrs());

        // 6、保存sku的信息 pms_product_attr_value

        List<Skus> skus = vo.getSkus();
        if (!CollectionUtils.isEmpty(skus)) {
            skus.forEach((sku) -> {
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();

                List<Images> images = sku.getImages();
                for (Images image : images) {
                    if (image.getDefaultImg() == 1) {
                        skuInfoEntity.setSkuDefaultImg(image.getImgUrl());
                    }
                }
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuId);

                // （6.1）、保存sku的基本信息 pms_sku_info
                skuInfoService.saveBaseSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                // （6.2）、保存sku的图片信息 pms_sku_images
                List<Images> imagesList = sku.getImages();
                skuImagesService.saveSkuImage(skuId, imagesList);

                // （6.3）、保存sku的销售属性 pms_sku_sale_attr_value
                List<Attr> skuAttrList = sku.getAttr();
                skuSaleAttrValueService.saveSkuSaleAttrValue(skuId, skuAttrList);


                // （6.4）、保存sku的折扣信息 gulimall_sms库 sms_spu_bounds、sms_sku_ladder、sms_sku_full_reduction、sms_member_price

                SkuReductionInfoTo skuReductionInfoTo = new SkuReductionInfoTo();
                BeanUtils.copyProperties(sku, skuReductionInfoTo);
                skuReductionInfoTo.setSkuId(skuId);
                if (skuReductionInfoTo.getFullCount() > 0 || skuReductionInfoTo.getFullPrice().compareTo(new BigDecimal("0")) > 0) {
                    R r1 = spuCouponFeignService.saveSkuReduction(skuReductionInfoTo);
                    if (r1.getCode() != 0) {
                        log.info("saveSpuInfo 调用spuCouponFeignService 服务的 saveSkuReduction 方法失败 ");
                    }
                }


            });
        }


    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryInfoPage(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

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
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void skuUp(Long spuId) {
        // 根据spuId 查询出所有的sku的信息
        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.getSkuInfoListBySpuId(spuId);

        // 根据spuId查询可以被搜索的规格参数
        // 根据spuId查询所有的规格参数
        List<ProductAttrValueEntity> productAttrValueEntityList = productAttrValueService.getProductAttrValue(spuId);

        // 收集所有的规格ids
        List<Long> productAttrIds = productAttrValueEntityList.stream()
                .map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());

        // 根据ids查询可以被搜索到的规格id
        List<Long> searchIds = attrService.getAttrIdBySearch(productAttrIds);

        List<ProductAttrValueEntity> searchEntityList = productAttrValueEntityList.stream()
                .filter((productAttrValueEntity) -> searchIds.contains(productAttrValueEntity.getAttrId()))
                .collect(Collectors.toList());

        //转换
        List<SkuEsModel.Attrs> attrsList = searchEntityList.stream().map((productAttrValueEntity) -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(productAttrValueEntity, attrs);
            return attrs;
        }).collect(Collectors.toList());

        // 获取所有的skuId
        List<Long> skuIds = skuInfoEntityList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        // 库存查询  //需要一个请求成功的判断
        R r = wareFeignService.hasStockBySkuIds(skuIds);

        String jsonString = JSON.toJSONString(r.get("data"));
        List<HasStockVo> hasStockVoList = JSON.parseArray(jsonString, HasStockVo.class);

//        Map<Long,Boolean> map =new HashMap<Long,Boolean>();
//        for (HasStockVo hasStockVo : hasStockVos) {
//            Long skuId = hasStockVo.getSkuId();
//            Boolean count = hasStockVo.getCount();
//            map.put(skuId,count);
//        }
//        System.out.println(map);

        Map<Long, Boolean> stockMap = hasStockVoList.stream().collect(Collectors.toMap(HasStockVo::getSkuId, HasStockVo::getCount));



        // 封装每个sku信息
        List<SkuEsModel> esModelList = skuInfoEntityList.stream().map((sku) -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);

            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());

            //库存  TODO 查询库存服务
            if (stockMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(stockMap.get(sku));
            }

            // 热点 暂时给一个默认值
            skuEsModel.setHotScore(0L);

            // 根据品牌id 查询图片，名称
            BrandEntity brandEntity = brandService.getById(sku.getBrandId());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            skuEsModel.setBrandName(brandEntity.getName());
            // 根据分类id 查询名称
            CategoryEntity categoryEntity = categoryService.getById(sku.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());

            //规格
            skuEsModel.setAttrs(attrsList);
            return skuEsModel;
        }).collect(Collectors.toList());

        // 将数据发送到es进行保存

        R searchRes = searchFeignService.productSaveElasticSearch(esModelList);
        if (searchRes.getCode() == 0) {
            // 调用成功
            baseMapper.updateStatusBySpuId(spuId, ProductConstant.StatusTypeEnum.SPU_UP.getType());
        } else {
            // 调用失败

        }

    }

    @Override
    public SpuInfoEntity getSpuBySkuId(Long skuId) {
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
        SpuInfoEntity spu = this.getById(skuInfoEntity.getSpuId());
        BrandEntity brandEntity = brandService.getById(spu.getBrandId());
        spu.setBrandName(brandEntity.getName());
        return spu;
    }
}
