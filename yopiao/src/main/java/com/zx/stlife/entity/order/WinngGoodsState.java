package com.zx.stlife.entity.order;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.SuperIntVersion;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 *  中奖用户状态
 */
@Entity
@Table(name = "order_winng_goods_state")
public class WinngGoodsState extends SuperIntEntity{

    /**
     * 用户
     */
    private User user;

    /**
     * 商品期号
     */
    private GoodsTimes goodsTimes;

    /**
     * 是否为当前状态
     */
    private Boolean isCurrState;

    /**是否已完成*/
    private Boolean isFinish;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * Default constructor
     */
    public WinngGoodsState() {

    }

    public WinngGoodsState(Byte state, User user,
                           GoodsTimes goodsTimes, Boolean isFinish, Boolean isCurrState) {
        super(state);
        this.user = user;
        this.goodsTimes = goodsTimes;
        this.isFinish = isFinish;
        this.isCurrState = isCurrState;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_times_id")
    public GoodsTimes getGoodsTimes() {
        return goodsTimes;
    }

    public void setGoodsTimes(GoodsTimes goodsTimes) {
        this.goodsTimes = goodsTimes;
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Boolean isFinish) {
        this.isFinish = isFinish;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean getIsCurrState() {
        return isCurrState;
    }

    public void setIsCurrState(Boolean isCurrState) {
        this.isCurrState = isCurrState;
    }

    // #################非持久化属性##############
    private String finishTimeStr;
    private Integer goodsStateId;

    @Transient
    public String getFinishTimeStr() {
        if(StringUtils.isBlank(finishTimeStr)){
            finishTimeStr = DateUtils.dateToYYYYMMDDHHMMSSSSSString(getFinishTime());
        }
        return finishTimeStr;
    }

    public void setFinishTimeStr(String finishTimeStr) {
        this.finishTimeStr = finishTimeStr;
    }

    @Transient
    public Integer getGoodsStateId() {
        return getId();
    }

    public void setGoodsStateId(Integer goodsStateId) {
        this.goodsStateId = goodsStateId;
    }
}