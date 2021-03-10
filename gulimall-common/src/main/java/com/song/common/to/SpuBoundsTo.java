package com.song.common.to;

import lombok.Data;

import java.math.BigDecimal;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-07 00:07
 **/
@Data
public class SpuBoundsTo {
    // spuId
    private Long spuId;
    //购物积分
    private BigDecimal buyBounds;
    //成长积分
    private BigDecimal growBounds;
}
