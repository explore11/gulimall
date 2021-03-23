package com.song.gulimall.gulimallsearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-23 15:14
 **/
@Controller
public class SearchController {
    @GetMapping("/list.html")
    public String listPage() {
        return "list";
    }
}
