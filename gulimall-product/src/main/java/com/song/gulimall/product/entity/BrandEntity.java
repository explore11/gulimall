package com.song.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.song.common.group.AddGroup;
import com.song.common.group.ListValue;
import com.song.common.group.UpdateGroup;
import com.song.common.group.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;


import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * 品牌
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    @NotNull(message = "修改必须指定品牌id", groups = {UpdateGroup.class})
    @Null(message = "新增不能指定品牌id", groups = {AddGroup.class})
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名不能为空", groups = {AddGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @URL(message = "这必须是一个合法的url地址", groups = {AddGroup.class})
    @NotBlank(message = "不能为空", groups = {AddGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @ListValue(vals = {0, 1}, message = "值为[0-不显示；1-显示]", groups = {AddGroup.class, UpdateStatusGroup.class})
    @NotNull(message = "不能为空")
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @Pattern(regexp = "^[a-zA-Z]$", message = "必须是一个字符", groups = {AddGroup.class})
    @NotNull(message = "不能为空", groups = {AddGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @Min(value = 0, message = "必须是大于等于0", groups = {AddGroup.class})
    @NotNull(message = "不能为空", groups = {AddGroup.class})
    private Integer sort;

}
