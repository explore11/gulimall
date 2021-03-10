package com.song.gulimall.product.feign;

import com.song.common.to.SkuReductionInfoTo;
import com.song.common.to.SpuBoundsTo;
import com.song.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-07 00:05
 **/
@FeignClient("gulimall-coupon")
public interface SpuCouponFeignService {
    @PostMapping("/coupon/spubounds/save")
    R saveSpuCoupon(@RequestBody SpuBoundsTo spuBoundsTo);

    @PostMapping("/coupon/spubounds/saveSkuReduction")
    R saveSkuReduction(@RequestBody SkuReductionInfoTo skuReductionInfoTo);
}
