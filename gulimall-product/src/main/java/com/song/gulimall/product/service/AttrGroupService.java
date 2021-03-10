package com.song.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.product.entity.AttrEntity;
import com.song.gulimall.product.entity.AttrGroupEntity;
import com.song.gulimall.product.vo.AttrGroupRespVo;
import com.song.gulimall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
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

    List<AttrEntity> getAttrListByAttrgroupId(Long attrgroupId);

    void batchDeleteAttrGroupRelationAttr(List<AttrGroupRespVo> attrGroupRespVoList);


    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);
}

