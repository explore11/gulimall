package com.song.gulimall.gulimallsearch.service;

import com.song.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-20 21:47
 **/
public interface ElasticSearchSaveService {
    boolean productSaveElasticSearch(List<SkuEsModel> skuEsModelList) throws IOException;
}
