
package com.song.gulimall.product.vo.spu;

import lombok.Data;

/* *
 * 基本属性
 */
@Data
public class BaseAttrs {
    // 属性id
    private Long attrId;
    // 属性值
    private String attrValues;
    // 快速展示【是否展示在介绍上；0-否 1-是】
    private int showDesc;
}
