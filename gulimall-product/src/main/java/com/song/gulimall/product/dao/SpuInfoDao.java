package com.song.gulimall.product.dao;

import com.song.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:27
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateStatusBySpuId(@Param("spuId") Long spuId, @Param("type") int type);
}
