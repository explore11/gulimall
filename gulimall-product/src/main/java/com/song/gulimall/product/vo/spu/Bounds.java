
package com.song.gulimall.product.vo.spu;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class Bounds {
    //购物积分
    private BigDecimal buyBounds;
    //成长积分
    private BigDecimal growBounds;
}
