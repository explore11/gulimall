package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:27
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuDesc(SpuInfoDescEntity spuInfoDescEntity);
}

