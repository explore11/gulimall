package com.song.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.song.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-06 09:28
 **/
@Data
public class AttrGroupWithAttrsVo {

    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 属性集合
     */
    private List<AttrEntity> attrs;
}
