package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.SkuInfoEntity;
import com.song.gulimall.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBaseSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryInfoPage(Map<String, Object> params);

    List<SkuInfoEntity> getSkuInfoListBySpuId(Long spuId);

    SkuItemVo  item(Long skuId) throws ExecutionException, InterruptedException;
}

