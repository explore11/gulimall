package com.song.gulimall.product.service.impl;

import com.song.gulimall.product.entity.SkuInfoEntity;
import com.song.gulimall.product.vo.spu.Images;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.common.utils.PageUtils;
import com.song.common.utils.Query;

import com.song.gulimall.product.dao.SkuImagesDao;
import com.song.gulimall.product.entity.SkuImagesEntity;
import com.song.gulimall.product.service.SkuImagesService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuImage(Long skuId, List<Images> imagesList) {
        if (!CollectionUtils.isEmpty(imagesList)) {
            List<SkuImagesEntity> skuImagesEntityList = imagesList.stream().map((images) -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setDefaultImg(images.getDefaultImg());
                skuImagesEntity.setImgUrl(images.getImgUrl());
                return skuImagesEntity;
            }).filter((imagesEntity) -> {
                // 返回true 是需要的  返回false是不要的
                return !StringUtils.isEmpty(imagesEntity.getImgUrl());
            }).collect(Collectors.toList());
            this.saveBatch(skuImagesEntityList);
        }
    }

}
