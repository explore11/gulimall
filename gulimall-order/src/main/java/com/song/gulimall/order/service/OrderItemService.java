package com.song.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.order.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-12-07 22:57:02
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

