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
public class Catalog3Vo {
    // 二级分类的id
    private String catalog2Id;
    // 三级分类的id
    private String id;
    // 三级分类的名称
    private String name;
}
