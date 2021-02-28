package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.song.gulimall.product.vo.AttrGroupRespVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addRelation(List<AttrGroupRespVo> attrGroupRespVoList);

}

