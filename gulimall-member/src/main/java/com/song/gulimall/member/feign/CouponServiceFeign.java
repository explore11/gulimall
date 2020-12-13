package com.song.gulimall.member.feign;

import com.song.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: gulimall
 * @description //TODO
 * @author: swq
 * @create: 2020-12-13 22:40
 **/
@FeignClient("gulimall-coupon")
public interface CouponServiceFeign {
    @GetMapping("/coupon/coupon/member/list")
    public R memberList();
}
