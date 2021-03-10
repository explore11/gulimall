package com.song.gulimall.coupon.service.impl;

import com.song.common.to.MemberPrice;
import com.song.common.to.SkuReductionInfoTo;
import com.song.gulimall.coupon.entity.MemberPriceEntity;
import com.song.gulimall.coupon.entity.SkuFullReductionEntity;
import com.song.gulimall.coupon.entity.SkuLadderEntity;
import com.song.gulimall.coupon.service.MemberPriceService;
import com.song.gulimall.coupon.service.SkuFullReductionService;
import com.song.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.coupon.dao.SpuBoundsDao;
import com.song.gulimall.coupon.entity.SpuBoundsEntity;
import com.song.gulimall.coupon.service.SpuBoundsService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {
    @Resource
    SkuLadderService skuLadderService;
    @Resource
    SkuFullReductionService skuFullReductionService;
    @Resource
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<SpuBoundsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSkuReduction(SkuReductionInfoTo skuReductionInfoTo) {

        // （6.4）、保存sku的折扣信息 gulimall_sms库
        // sms_sku_ladder、sms_sku_full_reduction、sms_member_price
        // 保存折扣信息
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionInfoTo, skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionInfoTo.getCountStatus());
        if (skuLadderEntity.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }
        // 保存满减信息
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionInfoTo, skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(skuReductionInfoTo.getPriceStatus());
        if (skuFullReductionEntity.getReducePrice().compareTo(new BigDecimal("0")) > 0) {
            skuFullReductionService.save(skuFullReductionEntity);
        }

        // 批量保存会员价格
        List<MemberPrice> memberPriceList = skuReductionInfoTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntityList = memberPriceList.stream().map((memberPrice) -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionInfoTo.getSkuId());
            memberPriceEntity.setMemberLevelName(memberPrice.getName());
            memberPriceEntity.setMemberPrice(memberPrice.getPrice());
            return memberPriceEntity;

        }).filter(memberPriceEntity -> {
            return memberPriceEntity.getMemberPrice().compareTo(new BigDecimal("0")) > 0;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntityList);

    }

}
