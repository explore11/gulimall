package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.SkuImagesEntity;
import com.song.gulimall.product.vo.spu.Images;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuImage(Long skuId, List<Images> imagesList);

    List<SkuImagesEntity> getSkuImagesBySkuId(Long skuId);
}

