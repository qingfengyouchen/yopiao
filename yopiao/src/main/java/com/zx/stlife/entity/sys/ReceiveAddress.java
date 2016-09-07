package com.zx.stlife.entity.sys;

import com.base.jpa.model.SuperIntEntity;

import javax.persistence.*;
import java.util.*;

/**
 * 收货地址
 */
@Entity
@Table(name = "sys_receive_address")
public class ReceiveAddress extends SuperIntEntity {

    /**
     * Default constructor
     */
    public ReceiveAddress() {
    }

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 手机号码
     */
    private String tel;

    /**
     * 省
     */
    private String province;

    /**
     * 
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 邮政编号
     */
    private String zip;

    private User user;

    /**是否默认地址*/
    private Boolean isDefault;


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    //#################非持久化属性
    private String fullAddress;

    @Transient
    public String getFullAddress() {
        if(fullAddress == null) {
            fullAddress = province + city + district + detailAddress;
        }
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}