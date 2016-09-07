package com.zx.stlife.entity.order;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 *  中奖用户收货信息, 包括收货地址和物流信息
 */
@Entity
@Table(name = "order_winng_user_receive_info")
public class WinngUserReceiveInfo extends SuperIntEntity{

    /**
     * 用户
     */
    private User user;

    /**
     * 商品期号
     */
    private GoodsTimes goodsTimes;

    /**
     * 收货人
     */
    private String receiver;

    /**手机号码*/
    private String mobileNo;

    /**
     * 收货地址
     */
    private String address;

    /**物流公司*/
    private String logisticsName;

    /**
     * 运单号码
     */
    private String logisticsNo;


    /**
     * Default constructor
     */
    public WinngUserReceiveInfo() {

    }

    public WinngUserReceiveInfo(User user, GoodsTimes goodsTimes,
                                String receiver, String mobileNo, String address) {
        this.user = user;
        this.goodsTimes = goodsTimes;
        this.receiver = receiver;
        this.mobileNo = mobileNo;
        this.address = address;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }


    public void addLogistics(String logisticsName, String logisticsNo){
        this.setLogisticsName(logisticsName);
        this.setLogisticsNo(logisticsNo);
    }
}