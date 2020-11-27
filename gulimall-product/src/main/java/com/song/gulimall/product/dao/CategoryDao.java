package com.song.gulimall.product.dao;

import com.song.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
