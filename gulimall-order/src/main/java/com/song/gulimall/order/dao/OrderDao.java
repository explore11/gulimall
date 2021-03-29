package com.song.gulimall.order.dao;

import com.song.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-12-07 22:57:02
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    void updateOrderStatus(@Param("orderSn") String orderSn, @Param("code") Integer code, @Param("alipay") Integer alipay);
}
