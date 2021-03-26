package com.song.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.song.common.utils.PageUtils;
import com.song.gulimall.member.entity.MemberEntity;
import com.song.gulimall.member.vo.MemberLoginVo;
import com.song.gulimall.member.vo.MemberRegisterVo;
import com.song.gulimall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-12-07 22:54:20
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo registerVo);

    MemberEntity login(MemberLoginVo loginVo);

    MemberEntity authLogin(SocialUser socialUser);
}

