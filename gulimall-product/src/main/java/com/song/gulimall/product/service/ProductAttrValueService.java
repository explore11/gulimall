package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.ProductAttrValueEntity;
import com.song.gulimall.product.vo.SpuItemAttrGroupVo;
import com.song.gulimall.product.vo.spu.BaseAttrs;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveProductAttrValue(Long spuId, List<BaseAttrs> baseAttrs);

    List<ProductAttrValueEntity> getProductAttrValue(Long spuId);

    List<SpuItemAttrGroupVo> getProductGroupBySpuId(Long spuId, Long catalogId);
}

