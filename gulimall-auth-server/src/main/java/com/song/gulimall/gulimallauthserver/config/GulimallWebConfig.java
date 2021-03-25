package com.song.gulimall.gulimallauthserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-25 12:57
 **/
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {


    /* *
     * 视图跳转
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/register.html").setViewName("register");
    }
}
