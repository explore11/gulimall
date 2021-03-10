/**
 * Copyright 2021 bejson.com
 */
package com.song.gulimall.product.vo.spu;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class Skus {
    // sku的销售属性
    private List<Attr> attr;
    // sku名称
    private String skuName;
    // sku 价格
    private BigDecimal price;
    // sku 主标题
    private String skuTitle;
    // sku 副标题
    private String skuSubtitle;
    // sku 图片集合
    private List<Images> images;
    // sku 组合
    private List<String> descar;
    // 满几件
    private int fullCount;
    // 打几折
    private BigDecimal discount;
    // 是否叠加其他优惠
    private int countStatus;
    //满多少
    private BigDecimal fullPrice;
    //减多少
    private BigDecimal reducePrice;
    private int priceStatus;
    // 用户价格列表
    private List<MemberPrice> memberPrice;
}
