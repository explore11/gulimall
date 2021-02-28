package com.song.gulimall.product.service.impl;

import com.song.gulimall.product.vo.AttrGroupRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.song.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.song.gulimall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void addRelation(List<AttrGroupRespVo> attrGroupRespVoList) {
        List<AttrAttrgroupRelationEntity> entityList = attrGroupRespVoList.stream().map((AttrGroupRespVo) -> {
            AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(AttrGroupRespVo, entity);
            return entity;
        }).collect(Collectors.toList());

       this.saveBatch(entityList);
    }

}
