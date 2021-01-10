package com.song.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.song.gulimall.product.entity.CategoryEntity;
import com.song.gulimall.product.service.CategoryService;
import com.song.common.utils.PageUtils;
import com.song.common.utils.R;


/* *
 * 商品三级分类
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 返回分类列表的树形结构
     */
    @RequestMapping("/list/tree")
    public R list() {
        List<CategoryEntity> categoryEntityList = categoryService.getCategoryTree();
        return R.ok().put("categoryEntityList", categoryEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 修改并排序
     * @param categorys
     * @return
     */
    @RequestMapping("/update/sort")
    //@RequiresPermissions("product:category:update")
    public R updateSort(@RequestBody CategoryEntity[] categorys) {
        categoryService.updateBatchById(Arrays.asList(categorys));
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds) {
//        categoryService.removeByIds(Arrays.asList(catIds));
        //批量逻辑删除
        categoryService.removeBatchIds(catIds);

        return R.ok();
    }

}
