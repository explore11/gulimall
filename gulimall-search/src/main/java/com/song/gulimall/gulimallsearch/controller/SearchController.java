package com.song.gulimall.gulimallsearch.controller;

import com.song.gulimall.gulimallsearch.service.MallSearchService;
import com.song.gulimall.gulimallsearch.vo.SearchParam;
import com.song.gulimall.gulimallsearch.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-23 15:14
 **/
@Controller
public class SearchController {
    @Resource
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {
        String queryString = request.getQueryString();
        param.set_queryString(queryString);
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);
        return "list";
    }
}
