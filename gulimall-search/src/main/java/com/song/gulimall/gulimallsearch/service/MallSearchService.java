package com.song.gulimall.gulimallsearch.service;

import com.song.gulimall.gulimallsearch.vo.SearchParam;
import com.song.gulimall.gulimallsearch.vo.SearchResult;

/* *
 * @program: gulimall
 * @description //TODO
 * @author: swq
 * @create: 2021-03-23 16:25
 **/
public interface MallSearchService {
    SearchResult search(SearchParam searchParam);
}
