package com.song.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.to.HasStockVo;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;
import com.song.common.utils.R;
import com.song.gulimall.ware.dao.WareSkuDao;
import com.song.gulimall.ware.entity.WareSkuEntity;
import com.song.gulimall.ware.feign.ProductFeign;
import com.song.gulimall.ware.service.WareSkuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Resource
    WareSkuDao wareSkuDao;
    @Autowired
    ProductFeign productFeign;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq("sku_id", skuId);
        }

        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> skuEntityList = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("ware_id", wareId).eq("sku_id", skuId));
        if (CollectionUtils.isEmpty(skuEntityList)) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStockLocked(0);
            R r = productFeign.info(skuId);
            try {
                if (r.getCode() == 0) {
                    Map<String, Object> skuInfoMap = (Map<String, Object>) r.get("skuInfo");
                    wareSkuEntity.setSkuName((String) skuInfoMap.get("skuName"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<HasStockVo> hasStockBySkuIds(List<Long> skuIds) {
        List<HasStockVo> collect = skuIds.stream().map((skuId) -> {
            HasStockVo hasStockVo = new HasStockVo();
            hasStockVo.setSkuId(skuId);
            long count = this.baseMapper.hasStockBySkuId(skuId);
            hasStockVo.setCount(count > 0);
            return hasStockVo;
        }).collect(Collectors.toList());

        return collect;
    }

}
