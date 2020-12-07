package com.song.gulimall.member.dao;

import com.song.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-12-07 22:54:20
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
