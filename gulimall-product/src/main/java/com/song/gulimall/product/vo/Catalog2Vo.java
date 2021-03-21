package com.song.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-21 19:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog2Vo {
    // 一级分类的id
    private String catalog1Id;
    // 三级分类集合
    private List<Catalog3Vo> catalog3List;
    // 二级分类的id
    private String id;
    // 二级分类的名称
    private String name;
}
