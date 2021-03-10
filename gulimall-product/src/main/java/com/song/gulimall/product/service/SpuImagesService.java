package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuImage(Long spuId, List<String> spuImages);
}

