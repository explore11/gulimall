package com.song.gulimall.product.controller;

import com.song.common.utils.PageUtils;
import com.song.common.utils.R;
import com.song.gulimall.product.entity.AttrEntity;
import com.song.gulimall.product.entity.AttrGroupEntity;
import com.song.gulimall.product.service.AttrAttrgroupRelationService;
import com.song.gulimall.product.service.AttrGroupService;
import com.song.gulimall.product.service.AttrService;
import com.song.gulimall.product.service.CategoryService;
import com.song.gulimall.product.vo.AttrGroupRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;


/**
 * 属性分组
 *
 * @author songwenqu
 * @email prefect_start@163.com
 * @date 2020-11-24 22:29:28
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Resource
    private AttrService attrService;

    @Autowired
    private CategoryService categoryService;
    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    ///product/attrgroup/attr/relation


    /* *
     * 添加属性组关联的属性
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRespVo> attrGroupRespVoList) {
        attrAttrgroupRelationService.addRelation(attrGroupRespVoList);
        return R.ok();
    }


    /* *
     * 获取属性分组没有关联的其他属性
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R getNoAttrGroup(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoAttrGroup(attrgroupId, params);
        return R.ok().put("page", page);
    }


    /* *
     * 移除属性组关联的属性
     */
    @PostMapping("/attr/relation/delete")
    public R batchDeleteAttrGroupRelationAttr(@RequestBody List<AttrGroupRespVo> attrGroupRespVoList) {
        attrGroupService.batchDeleteAttrGroupRelationAttr(attrGroupRespVoList);
        return R.ok();
    }

    /* *
     * 属性分组关联查询属性集合 根据分组id
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R list(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> attrEntityList = attrGroupService.getAttrListByAttrgroupId(attrgroupId);
        return R.ok().put("data", attrEntityList);
    }


    /* *
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        //获取分类id
        Long catelogId = attrGroup.getCatelogId();
        //根据分类id 获取分类的三级目录
        Long[] catelogPath = categoryService.getCatelogPath(catelogId);
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
