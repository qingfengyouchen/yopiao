package com.zx.stlife.entity.order;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;

import javax.persistence.*;
import java.util.*;

/**
 * 夺宝记录明细
 */
@Entity
@Table(name = "order_snatch_record_detail")
public class SnatchRecordDetail extends SuperIntEntity {

    /**
     * 支付记录
     */
    private PayRecord payRecord;

    /**
     * 商品
     */
    private GoodsInfo goodsInfo;

    /**
     * 商品期数
     */
    private GoodsTimes goodsTimes;

    /**
     * 购买人次
     */
    private Integer buyTimes;

    /**
     * 购买金额
     */
    private Integer money;

    /**
     * 购买用户
     */
    private User user;

    /**
     * 购买用户头像
     */
    private String userHeadImg;
    /**
     * 购买用户昵称
     */
    private String userNickName;
    /**
     * 购买用户所在地址
     */
    private String address;
    /**
     * 购买用户ip
     */
    private String ip;

    /**
     * 夺宝记录
     */
    private SnatchRecord snatchRecord;

    /**
     * 夺宝时间
     * the number of milliseconds since January 1, 1970, 00:00:00 GMT
     */
    private Long snatchTime;

    // state 表示支付结果，state在基类SuperIntEntity中定义了

    /**
     * Default constructor
     */
    public SnatchRecordDetail() {
        super(Const.PayResult.UNPAY);
        this.snatchTime = getCreateTime().getTime();
    }

    public SnatchRecordDetail(PayRecord payRecord, GoodsInfo goodsInfo, GoodsTimes goodsTimes,
                              Integer buyTimes, Integer money, User user, String userHeadImg,
                              String userNickName, String address, String ip,
                              SnatchRecord snatchRecord, Byte state) {
        super(state);
        this.payRecord = payRecord;
        this.goodsInfo = goodsInfo;
        this.goodsTimes = goodsTimes;
        this.buyTimes = buyTimes;
        this.money = money;
        this.user = user;
        this.userHeadImg = userHeadImg;
        this.userNickName = userNickName;
        this.address = address;
        this.ip = ip;
        this.snatchRecord = snatchRecord;
        this.snatchTime = getCreateTime().getTime();
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "pay_record_id")
    public PayRecord getPayRecord() {
        return payRecord;
    }

    public void setPayRecord(PayRecord payRecord) {
        this.payRecord = payRecord;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    public GoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "goods_times_id")
    public GoodsTimes getGoodsTimes() {
        return goodsTimes;
    }

    public void setGoodsTimes(GoodsTimes goodsTimes) {
        this.goodsTimes = goodsTimes;
    }

    public Integer getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(Integer buyTimes) {
        this.buyTimes = buyTimes;
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

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "snatch_record_id")
    public SnatchRecord getSnatchRecord() {
        return snatchRecord;
    }

    public void setSnatchRecord(SnatchRecord snatchRecord) {
        this.snatchRecord = snatchRecord;
    }

    public Long getSnatchTime() {
        return snatchTime;
    }

    public void setSnatchTime(Long snatchTime) {
        this.snatchTime = snatchTime;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    // #################非持久化属性##############

    private String snatchTimeStr;
    private Integer userId;
    private String createTimeStr;
    
    @Transient
    public String getSnatchTimeStr() {
        if(snatchTimeStr == null){
            snatchTimeStr = DateUtils
                    .millisecondsToYYYYMMDDHHMMSSSSSString(getSnatchTime());
        }
        return snatchTimeStr;
    }

    public void setSnatchTimeStr(String snatchTimeStr) {
        this.snatchTimeStr = snatchTimeStr;
    }

    @Transient
	public Integer getUserId() {
    	if(userId==null && getUser()!=null){
    		userId=getUser().getId();
    	}
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Transient
	public String getCreateTimeStr() {
		if(createTimeStr==null){
			createTimeStr=DateUtils
                    .dateToYYYYMMDDHHMMSSSSSString(getCreateTime());
		}
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
}