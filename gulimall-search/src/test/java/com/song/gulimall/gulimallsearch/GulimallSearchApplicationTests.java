package com.song.gulimall.gulimallsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.song.gulimall.gulimallsearch.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
class GulimallSearchApplicationTests {
    @Resource
    private RestHighLevelClient client;


    @Test
    void contextLoads1() throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(response);
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            String asString = hit.getSourceAsString();
            System.out.println(asString);
            System.out.println(JSON.parseObject(asString,User.class));
        }
        System.out.println();
    }


    @Test
    void contextLoads() throws IOException {
        IndexRequest request = new IndexRequest("user");
        request.id("1");

        User user = new User();
        user.setName("zhangsan");
        user.setGender("男");
        user.setAge(18);
        String jsonString = JSON.toJSONString(user);
        request.source(jsonString, XContentType.JSON);


        IndexResponse response = client.index(request, ElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(response);
    }

    @Data
   static class User {
        private String name;
        private String gender;
        private Integer age;
    }

}
