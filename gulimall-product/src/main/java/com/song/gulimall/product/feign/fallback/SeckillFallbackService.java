package com.song.gulimall.product.feign.fallback;


import com.song.common.exception.BizCode;
import com.song.common.utils.R;
import com.song.gulimall.product.feign.SeckillFeignService;
import org.springframework.stereotype.Component;

@Component
public class SeckillFallbackService implements SeckillFeignService {
    @Override
    public R getSeckillSkuInfo(Long skuId) {
        return R.error(BizCode.READ_TIME_OUT_EXCEPTION.getCode(), BizCode.READ_TIME_OUT_EXCEPTION.getMsg());
    }
}
