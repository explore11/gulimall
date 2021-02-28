package com.song.gulimall.product.dao;

import com.song.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.song.gulimall.product.vo.AttrGroupRespVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    void batchDeleteAttrGroupRelationAttr(@Param("attrGroupRespVoList") List<AttrGroupRespVo> attrGroupRespVoList);
}
