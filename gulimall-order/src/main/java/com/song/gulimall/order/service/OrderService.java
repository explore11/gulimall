package com.song.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.to.mq.SeckillOrderTo;
import com.song.common.utils.PageUtils;
import com.song.gulimall.order.entity.OrderEntity;
import com.song.gulimall.order.vo.*;

import java.util.Map;

/**
 * 订单
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-12-07 22:57:02
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder();


    SubmitOrderResponseVo submitOrder(OrderSubmitVo submitVo);

    void closeOrder(OrderEntity orderEntity);

    OrderEntity getOrderByOrderSn(String orderSn);

    PageUtils getMemberOrderPage(Map<String, Object> params);

    void handlerPayResult(PayAsyncVo payAsyncVo);

    PayVo getOrderPay(String orderSn);

    void createSeckillOrder(SeckillOrderTo orderTo);
}

