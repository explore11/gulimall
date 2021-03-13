package com.song.gulimall.ware.service.impl;

import com.song.common.utils.WareConstant;
import com.song.gulimall.ware.entity.PurchaseDetailEntity;
import com.song.gulimall.ware.service.PurchaseDetailService;
import com.song.gulimall.ware.service.WareSkuService;
import com.song.gulimall.ware.vo.MergeVo;
import com.song.gulimall.ware.vo.PurchaseDoneVo;
import com.song.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.ware.dao.PurchaseDao;
import com.song.gulimall.ware.entity.PurchaseEntity;
import com.song.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    PurchaseDetailService purchaseDetailService;
    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils unReceiveList(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void merge(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();

        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;

        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map((purchaseDetailId) -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(purchaseDetailId);
            detailEntity.setStatus(WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
            detailEntity.setPurchaseId(finalPurchaseId);
            return detailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(purchaseDetailEntityList);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCreateTime(new Date());
        purchaseEntity.setUpdateTime(new Date());
        purchaseEntity.setId(finalPurchaseId);
        this.updateById(purchaseEntity);

    }

    @Override
    @Transactional
    public void received(List<Long> purchaseIds) {

        // 判断采购单的状态

        List<PurchaseEntity> collect = purchaseIds.stream().map((purchaseId) -> {
            return this.baseMapper.selectById(purchaseId);
        }).filter((purchaseEntity) -> {
            if (purchaseEntity.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()
                    || purchaseEntity.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            } else {
                return false;
            }
        }).map((purchaseEntity) -> {
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            purchaseEntity.setUpdateTime(new Date());
            return purchaseEntity;
        }).collect(Collectors.toList());

        // 更改采购单的状态
        this.updateBatchById(collect);

        // 更改采购需求单的状态
        collect.forEach((purchaseEntity) -> {
            List<PurchaseDetailEntity> detailEntityList = purchaseDetailService.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", purchaseEntity.getId()));

            if (!CollectionUtils.isEmpty(detailEntityList)) {
                for (PurchaseDetailEntity purchaseDetailEntity : detailEntityList) {
                    purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                }
                purchaseDetailService.updateBatchById(detailEntityList);
            }
        });
    }

    @Override
    @Transactional
    public void done(PurchaseDoneVo purchaseDoneVo) {
        Long purchaseId = purchaseDoneVo.getId();

        // 判断采购项是否完成
        List<PurchaseItemDoneVo> itemDoneVoList = purchaseDoneVo.getItems();

        List<PurchaseDetailEntity> purchaseDetailEntityList = new ArrayList<>();
        boolean flag = true;
        for (PurchaseItemDoneVo itemDoneVo : itemDoneVoList) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(itemDoneVo.getItemId());
            if (itemDoneVo.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(itemDoneVo.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                // 根据采购需求的id查询
                PurchaseDetailEntity purchaseDetailServiceById = purchaseDetailService.getById(detailEntity.getId());
                // 采购完成增加库存
                wareSkuService.addStock(purchaseDetailServiceById.getSkuId(),
                        purchaseDetailServiceById.getWareId(),
                        purchaseDetailServiceById.getSkuNum());
            }
            purchaseDetailEntityList.add(detailEntity);
        }
        // 批量更新采购需求单
        purchaseDetailService.updateBatchById(purchaseDetailEntityList);

        // 更新采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        if (flag) {
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISH.getCode());
        } else {
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        }
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

}
