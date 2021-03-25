package com.song.gulimall.gulimallauthserver.feign;

import com.song.common.utils.R;
import com.song.gulimall.gulimallauthserver.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-25 17:11
 **/
@FeignClient("gulimall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/register")
     R register(@RequestBody UserRegisterVo registerVo);
}
