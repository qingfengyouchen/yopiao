package com.zx.stlife.entity.goods;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;

import javax.persistence.*;
import java.util.*;

/**
 * 晒单图片
 */
@Entity
@Table(name = "goods_share_image")
public class ShareImage extends SuperIntEntity {

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private ShareGoods shareGoods;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Default constructor
     */
    public ShareImage() {
        super(Const.CommonState.ENABLE);
    }

    public ShareImage(String url, ShareGoods shareGoods) {
        super(Const.CommonState.ENABLE);
        this.url = url;
        this.shareGoods = shareGoods;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_share_id")
    public ShareGoods getShareGoods() {
        return shareGoods;
    }

    public void setShareGoods(ShareGoods shareGoods) {
        this.shareGoods = shareGoods;
    }
}