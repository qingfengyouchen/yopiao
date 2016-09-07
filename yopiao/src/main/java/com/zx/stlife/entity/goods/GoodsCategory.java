package com.zx.stlife.entity.goods;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品类别
 */
@Entity
@Table(name = "goods_category")
public class GoodsCategory extends SuperIntEntity {

    /**
     * 商品类别名称
     */
    private String name;

    /**
     * 
     */
    private String imgUrl;

    /**
     * 排序号
     */
    private Byte sortNum;

    public GoodsCategory() {
        super(Const.CommonState.ENABLE);
    }

    public GoodsCategory(Integer id) {
        super(id, Const.CommonState.ENABLE);
    }

    public GoodsCategory(Integer id, String name) {
        super(id, Const.CommonState.ENABLE);
        this.name = name;
    }

    public GoodsCategory(Integer id, String name, String imgUrl) {
        super(id);
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Byte getSortNum() {
        return sortNum;
    }

    public void setSortNum(Byte sortNum) {
        this.sortNum = sortNum;
    }
}