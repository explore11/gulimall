package com.song.gulimall.ware.dao;

import com.song.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-12-07 22:59:07
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    long hasStockBySkuId(@Param("skuId") Long skuId);
}
