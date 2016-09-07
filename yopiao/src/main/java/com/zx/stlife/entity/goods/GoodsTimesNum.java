package com.zx.stlife.entity.goods;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * 商品期号号码
 */
@Entity
@Table(name = "goods_times_num")
public class GoodsTimesNum extends SuperIntEntity {

    /**商品期号*/
    private GoodsTimes goodsTimes;
    /**号码*/
    private Integer num;

    /**
     * Default constructor
     */
    public GoodsTimesNum() {
        super(Const.CommonState.ENABLE);
    }

    public GoodsTimesNum(GoodsTimes goodsTimes, Integer num) {
        this.goodsTimes = goodsTimes;
        this.num = num;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_times_id")
    public GoodsTimes getGoodsTimes() {
        return goodsTimes;
    }

    public void setGoodsTimes(GoodsTimes goodsTimes) {
        this.goodsTimes = goodsTimes;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}