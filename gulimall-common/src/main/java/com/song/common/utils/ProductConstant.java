package com.song.common.utils;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-02-24 22:14
 **/
public class ProductConstant {
    public enum AttrTypeEnum {
        ATTR_TYPE_BASE(1, "基本属性"),
        ATTR_TYPE_SALE(0, "销售属性");

        AttrTypeEnum(int type, String msg) {
            this.type = type;
            this.msg = msg;
        }

        private int type;
        private String msg;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum StatusTypeEnum {
        NEW_SPU(0, "新建"),
        SPU_UP(1, "商品上架"),
        SPU_DOWN(2, "商品下架");

        StatusTypeEnum(int type, String msg) {
            this.type = type;
            this.msg = msg;
        }

        private int type;
        private String msg;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
