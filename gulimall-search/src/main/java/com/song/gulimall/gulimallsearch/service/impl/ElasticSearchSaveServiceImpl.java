package com.song.gulimall.gulimallsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.song.common.to.es.SkuEsModel;
import com.song.gulimall.gulimallsearch.config.ElasticSearchConfig;
import com.song.gulimall.gulimallsearch.constant.ElasticSearchConstant;
import com.song.gulimall.gulimallsearch.service.ElasticSearchSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-20 21:47
 **/
@Service
@Slf4j
public class ElasticSearchSaveServiceImpl implements ElasticSearchSaveService {
    @Resource
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productSaveElasticSearch(List<SkuEsModel> skuEsModelList) throws IOException {

        // 保存到es中
        // 1、给es中建立索引 product 建立好映射关系

        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModelList) {
            // 添加索引
            IndexRequest indexRequest =new IndexRequest(ElasticSearchConstant.PRODUCT_INDEX);
            // 设置id
            indexRequest.id(skuEsModel.getSkuId().toString());
            String jsonString = JSON.toJSONString(skuEsModel);
            indexRequest.source(jsonString, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }
        // 批量添加数据
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
//        List<String> strings = Arrays.stream(bulkResponse.getItems()).map(BulkItemResponse::getId).collect(Collectors.toList());
        boolean hasFailures = bulkResponse.hasFailures();

        return !hasFailures;
    }
}
