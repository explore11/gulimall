package com.song.gulimall.product.feign;

import com.song.common.to.es.SkuEsModel;
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
@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/productSaveElasticSearch")
    R productSaveElasticSearch(@RequestBody List<SkuEsModel> skuEsModelList);
}
