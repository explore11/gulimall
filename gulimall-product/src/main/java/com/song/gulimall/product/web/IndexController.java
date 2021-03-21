package com.song.gulimall.product.web;

import com.song.gulimall.product.entity.CategoryEntity;
import com.song.gulimall.product.service.CategoryService;
import com.song.gulimall.product.vo.Catalog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-21 18:50
 **/
@Controller
public class IndexController {
    @Resource
    CategoryService categoryService;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        List<CategoryEntity> categoryEntityList = categoryService.getCategoryOneLevel();
        model.addAttribute("categoryList", categoryEntityList);
        return "index";
    }

    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<Long, List<Catalog2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }

}
