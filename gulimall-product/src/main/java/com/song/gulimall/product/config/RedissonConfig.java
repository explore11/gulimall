package com.song.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-22 21:41
 **/
@Configuration
public class RedissonConfig {
    @Bean(destroyMethod = "shutdown")
    public RedissonClient getRedisson() {
        Config config = new Config();
        // 使用单节点配置
        config.useSingleServer().setAddress("redis://192.168.56.10:6379");
        return Redisson.create(config);
    }

}
