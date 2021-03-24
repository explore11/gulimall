package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.song.gulimall.product.vo.SkuItemSaleAttrVo;
import com.song.gulimall.product.vo.spu.Attr;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuSaleAttrValue(Long skuId, List<Attr> skuAttrList);

    List<SkuItemSaleAttrVo> geSkuSaleAttr(Long spuId);
}

