package com.song.gulimall.product.web;

import com.song.gulimall.product.service.SkuInfoService;
import com.song.gulimall.product.vo.SkuItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-24 15:31
 **/
@Controller
public class ItemController {
    @Resource
    SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws Exception {
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        model.addAttribute("item",skuItemVo);
        return "item";
    }
}
