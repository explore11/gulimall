package com.song.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.ProductConstant;
import com.song.common.utils.Query;
import com.song.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.song.gulimall.product.dao.AttrDao;
import com.song.gulimall.product.dao.AttrGroupDao;
import com.song.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.song.gulimall.product.entity.AttrEntity;
import com.song.gulimall.product.entity.AttrGroupEntity;
import com.song.gulimall.product.service.AttrGroupService;
import com.song.gulimall.product.vo.AttrGroupRespVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

//    @Override
//    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<AttrGroupEntity> page = this.page(
//                new Query<AttrGroupEntity>().getPage(params),
//                new QueryWrapper<AttrGroupEntity>()
//        );
//
//        return new PageUtils(page);
//    }

    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Resource
    private AttrDao attrDao;
    @Resource
    private AttrGroupDao attrGroupDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        IPage<AttrGroupEntity> page = null;
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        // 根据关键字模糊查询
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if (catelogId == 0) { // 等于0 查询全部
            page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<>());
        } else { // 不等于0 表示查询指定的
            wrapper.eq("catelog_id", catelogId);
            // 查询
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        }
        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> getAttrListByAttrgroupId(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntityList = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrTds = null;
        if (!CollectionUtils.isEmpty(relationEntityList)) {
             attrTds = relationEntityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        }
        return attrDao.selectBatchIds(attrTds);
    }

    @Override
    public void batchDeleteAttrGroupRelationAttr(List<AttrGroupRespVo> attrGroupRespVoList) {
        attrGroupDao.batchDeleteAttrGroupRelationAttr(attrGroupRespVoList);
    }


}
