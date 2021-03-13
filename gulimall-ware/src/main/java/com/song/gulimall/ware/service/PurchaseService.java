package com.song.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.ware.entity.PurchaseEntity;
import com.song.gulimall.ware.vo.MergeVo;
import com.song.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-12-07 22:59:07
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils unReceiveList(Map<String, Object> params);

    void merge(MergeVo mergeVo);

    void received(List<Long> purchaseIds);

    void done(PurchaseDoneVo purchaseDoneVo);

}

