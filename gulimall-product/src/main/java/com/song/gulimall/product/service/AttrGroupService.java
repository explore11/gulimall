package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

//    PageUtils queryPage(Map<String, Object> params);
    PageUtils queryPage(Map<String, Object> params,Long catelogId);
}

