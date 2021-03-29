package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.to.es.SkuEsModel;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.SpuInfoEntity;
import com.song.gulimall.product.vo.spu.SpuInfoVo;

import java.util.List;
import java.util.Map;

/**
 * spu信息
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:27
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuInfoVo vo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

    PageUtils queryInfoPage(Map<String, Object> params);

    void skuUp(Long spuId);

    SpuInfoEntity getSpuBySkuId(Long skuId);
}

