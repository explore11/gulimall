
package com.song.gulimall.product.vo.spu;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class SpuInfoVo {
    // spu 名称
    private String spuName;
    // spu 主标题
    private String spuDescription;
    // spu 分类id
    private Long catalogId;
    // spu 品牌id
    private Long brandId;
    // spu 重量
    private BigDecimal weight;
    // spu 发布状态
    private int publishStatus;
    // spu 详情
    private List<String> decript;
    // spu 图片列表
    private List<String> images;
    // 积分
    private Bounds bounds;
    // 基本属性
    private List<BaseAttrs> baseAttrs;
    // sku列表
    private List<Skus> skus;
}
