package com.song.gulimall.ware.feign;

import com.song.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-13 09:47
 **/
@FeignClient("gulimall-product")
public interface ProductFeign {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}
