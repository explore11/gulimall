package com.song.gulimall.gulimallauthserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.song.common.exception.BizCode;
import com.song.common.utils.AuthServerConstant;
import com.song.common.utils.R;
import com.song.common.vo.MemberResponseVo;
import com.song.gulimall.gulimallauthserver.feign.MemberFeignService;
import com.song.gulimall.gulimallauthserver.feign.ThirdPartyFeignService;
import com.song.gulimall.gulimallauthserver.vo.UserLoginVo;
import com.song.gulimall.gulimallauthserver.vo.UserRegisterVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-25 12:37
 **/
@Controller
public class LoginController {

    @Resource
    ThirdPartyFeignService thirdPartyFeignService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    MemberFeignService memberFeignService;

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {
        // 1、接口防刷
        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);

        if (!StringUtils.isEmpty(redisCode)) {
            long time = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - time < 60000) {
                // 60秒内不能再发
                return R.error(BizCode.SMS_CODE_EXCEPTION.getCode(), BizCode.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        // 2、验证码再次校验
        String code = UUID.randomUUID().toString().substring(0, 5);
        String s = code + "_" + System.currentTimeMillis();
        // 3、缓存
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, s, 10, TimeUnit.MINUTES);

        //发送
        thirdPartyFeignService.sendCode(phone, code);
        return R.ok();
    }


    /* *
     * 登录
     * @param vo
     * @param attributes
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes attributes, HttpSession session){
        R r = memberFeignService.login(vo);
        if (r.getCode() == 0) {
            String jsonString = JSON.toJSONString(r.get("memberEntity"));
            MemberResponseVo memberResponseVo = JSON.parseObject(jsonString, new TypeReference<MemberResponseVo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER, memberResponseVo);
            return "redirect:http://gulimall.com/";
        }else {
            String msg = (String) r.get("msg");
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", msg);
            attributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }


    /* *
     * 注册
     * @param registerVo
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo registerVo, BindingResult result, RedirectAttributes attributes) {
        //1.判断校验是否通过
        Map<String, String> errors = new HashMap<>();
        if (result.hasErrors()) {
            //1.1 如果校验不通过，则封装校验结果
            errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,
                    DefaultMessageSourceResolvable::getDefaultMessage));
            //1.2 将错误信息封装到session中
            attributes.addFlashAttribute("errors", errors);

            //1.2 重定向到注册页
            return "redirect:http://auth.gulimall.com/register.html";
        } else {
            //2.若JSR303校验通过
            //判断验证码是否正确
            String code = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + registerVo.getPhone());
            //2.1 如果对应手机的验证码不为空且与提交上的相等-》验证码正确
            if (!StringUtils.isEmpty(code) && registerVo.getCode().equals(code.split("_")[0])) {
                //2.1.1 使得验证后的验证码失效
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + registerVo.getPhone());

                //2.1.2 远程调用会员服务注册
                R r = memberFeignService.register(registerVo);
                if (r.getCode() == 0) {
                    //调用成功，重定向登录页
                    return "redirect:http://auth.gulimall.com/login.html";
                } else {
                    //调用失败，返回注册页并显示错误信息
                    String msg = (String) r.get("msg");
                    errors.put("msg", msg);
                    attributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.gulimall.com/register.html";
                }
            } else {
                //2.2 验证码错误
                errors.put("code", "验证码错误");
                attributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gulimall.com/register.html";
            }
        }
    }


    /* *
     * 已登录的自动跳回首页
     * @param session
     * @return
     */
    @RequestMapping("/login.html")
    public String loginPage(HttpSession session) {
        if (session.getAttribute(AuthServerConstant.LOGIN_USER) != null) {
            return "redirect:http://gulimall.com/";
        }else {
            return "login";
        }
    }
}
