package com.song.gulimall.product.feign;

import com.song.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-07 00:05
 **/
@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasStockBySkuIds")
    R hasStockBySkuIds(@RequestBody List<Long> skuIds);
}
