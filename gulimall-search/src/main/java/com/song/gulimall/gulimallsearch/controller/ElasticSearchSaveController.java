package com.song.gulimall.gulimallsearch.controller;

import com.song.common.exception.BizCode;
import com.song.common.to.es.SkuEsModel;
import com.song.common.utils.R;
import com.song.gulimall.gulimallsearch.service.ElasticSearchSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-20 21:46
 **/
@RestController
@RequestMapping("/search/save")
@Slf4j
public class ElasticSearchSaveController {
    @Autowired
    ElasticSearchSaveService elasticSearchSaveService;

    @PostMapping("/productSaveElasticSearch")
    public R productSaveElasticSearch(@RequestBody List<SkuEsModel> skuEsModelList) {
        boolean flag = false;
        try {
            flag = elasticSearchSaveService.productSaveElasticSearch(skuEsModelList);
        } catch (IOException e) {
            log.error("productSaveElasticSearch 商品上架异常 {}", e);
        }

        if (flag) {
            return R.ok();
        } else {
            return R.error(BizCode.PRODUCT_UP_EXCEPTION.getCode(), BizCode.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
