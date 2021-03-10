package com.song.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-07 00:21
 **/
@Data
public class SkuReductionInfoTo {
    // skuId
    private Long skuId;
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
