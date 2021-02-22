package com.song.gulimall.product.service.impl;

import com.song.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.song.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.song.gulimall.product.entity.BrandEntity;
import com.song.gulimall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.product.dao.AttrDao;
import com.song.gulimall.product.entity.AttrEntity;
import com.song.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        // 关联保存属性、属性组表
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    public PageUtils queryBaseAttrListPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });

        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

}
