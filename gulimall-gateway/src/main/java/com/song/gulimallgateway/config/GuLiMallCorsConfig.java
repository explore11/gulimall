package com.song.gulimallgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

/* *
 * @program: gulimall
 * @description 跨域问题
 * @author: swq
 * @create: 2021-01-02 23:04
 **/
@Configuration
public class GuLiMallCorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();

        CorsConfiguration config =new CorsConfiguration();
        //参数配置
        config.setAllowCredentials(true); // 携带cookies
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        source.registerCorsConfiguration("/**",config);
        return new CorsWebFilter(source);
    }
}
