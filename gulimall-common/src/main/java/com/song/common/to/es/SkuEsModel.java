package com.song.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * es中被搜索出的商品模型
 */
@Data
public class SkuEsModel {
    // skuId
    private Long skuId;
    // spuId
    private Long spuId;
    // 商品名称
    private String skuTitle;
    // 商品价格
    private BigDecimal skuPrice;
    // 商品图片
    private String skuImg;
    // 商品售卖总数
    private Long saleCount;
    // 是否有库存
    private Boolean hasStock;
    // 热点分数
    private Long hotScore;
    // 品牌id
    private Long brandId;
    // 分类id
    private Long catalogId;
    // 品牌名称
    private String brandName;
    // 品牌图片
    private String brandImg;
    // 分类名称
    private String catalogName;
    // 规格
    private List<Attrs> attrs;

    @Data
    //为了第三方工具能对它序列化反序列化，设置为可访问的权限
    public static class Attrs {
        //规格id
        private Long attrId;
        // 规格名称
        private String attrName;
        // 规格属性值
        private String attrValue;
    }

}
