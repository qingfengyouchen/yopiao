package com.zx.stlife.entity.order;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.SuperIntVersion;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.vo.snatch.SnatchVo;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.*;

/**
 * 夺宝记录
 */
@Entity
@Table(name = "order_snatch_record")
public class SnatchRecord extends SuperIntVersion {

    /**商品名称*/
    private String goodsName;

    /**
     * 期号
     */
    private GoodsTimes goodsTimes;
    /**
     * 期号名称
     */
    private Integer goodsTimesName;

    /**
     * 总需人次
     */
    private Integer totalTimes;

    /**
     * 购买人次
     */
    private Integer buyTimes;

    /**
     * 金额
     */
    private Integer money;

    /**
     * 购买用户
     */
    private User user;
    /**
     * 修改时间，用在夺宝记录中排序，同一期购买多次
     */
    private Date editTime;

    // state 表示支付结果，state在基类SuperIntEntity中定义了

    /**
     * Default constructor
     */
    public SnatchRecord() {
        super(Const.PayResult.UNPAY);
        setEditTime(getCreateTime());
    }

    public SnatchRecord(String goodsName, GoodsTimes goodsTimes, Integer goodsTimesName,
                        Integer totalTimes, Integer buyTimes, Integer money, User user, Byte state) {
        super(state);
        this.goodsName = goodsName;
        this.goodsTimes = goodsTimes;
        this.goodsTimesName = goodsTimesName;
        this.totalTimes = totalTimes;
        this.buyTimes = buyTimes;
        this.money = money;
        this.user = user;
        setEditTime(getCreateTime());
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_times_id")
    public GoodsTimes getGoodsTimes() {
        return goodsTimes;
    }

    public void setGoodsTimes(GoodsTimes goodsTimes) {
        this.goodsTimes = goodsTimes;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getGoodsTimesName() {
        return goodsTimesName;
    }

    public void setGoodsTimesName(Integer goodsTimesName) {
        this.goodsTimesName = goodsTimesName;
    }

    public Integer getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(Integer buyTimes) {
        this.buyTimes = buyTimes;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    // ################非持久化###########

    private Integer goodsTimesId;
    private String lastSnatchTime;
    /**清单，用于下单时封装数据*/
    private List<SnatchVo> snatchList;

    @Transient
	public Integer getGoodsTimesId() {
    	if(goodsTimes!=null){
    		goodsTimesId=getGoodsTimes().getId();
    	}
		return goodsTimesId;
	}

	public void setGoodsTimesId(Integer goodsTimesId) {
		this.goodsTimesId = goodsTimesId;
	}

    @Transient
    public String getLastSnatchTime() {
        if(StringUtils.isBlank(lastSnatchTime)){
            lastSnatchTime = DateUtils.dateToYYYYMMDDHHMMSSSSSString(getEditTime());
        }
        return lastSnatchTime;
    }

    public void setLastSnatchTime(String lastSnatchTime) {
        this.lastSnatchTime = lastSnatchTime;
    }

    @Transient
    public List<SnatchVo> getSnatchList() {
        return snatchList;
    }

    public void setSnatchList(List<SnatchVo> snatchList) {
        this.snatchList = snatchList;
    }


    public int addBuyTimesAndMoney(int buyTimes){
        if(buyTimes > 0) {
            buyTimes = (getBuyTimes() == null ? 0 : getBuyTimes()) + buyTimes;
            setBuyTimes(buyTimes);
            setMoney(buyTimes);
        }
        return getBuyTimes();
    }

    public int subtractBuyTimesAndMoney(int buyTimes){
        buyTimes = (getBuyTimes() == null ? 0 : getBuyTimes()) - buyTimes;
        if(buyTimes < 0){
            throw new BizException("购买数量不能小于1");
        }
        setBuyTimes(buyTimes);
        setMoney(buyTimes);
        return buyTimes;
    }
}