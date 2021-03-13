package com.song.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PurchaseDoneVo {


    private Long id;//采购单id

    private List<PurchaseItemDoneVo> items;
}
