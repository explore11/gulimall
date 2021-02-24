package com.song.gulimall.product.vo;

import com.song.gulimall.product.entity.AttrEntity;
import lombok.Data;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-02-24 20:53
 **/
@Data
public class AttrRespVo extends AttrVo {
    //所属分组名
    private String groupName;
    //所属分类名字
    private String catelogName;
    //分类完整路径
    private Long[] catelogPath;
}
