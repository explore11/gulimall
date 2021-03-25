package com.song.gulimall.gulimallthirdparty.controller;

import com.song.common.utils.R;
import com.song.gulimall.gulimallthirdparty.utils.SmsComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-25 15:43
 **/
@RestController
@RequestMapping("/sms")
public class SmsSendController {
    @Resource
    SmsComponent smsComponent;

    /* *
     * 供其他服务发送验证码使用
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) throws Exception {
        smsComponent.sendCode(phone,code);
        return R.ok();
    }
}
