package com.zx.stlife.entity.goods;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.base.jpa.model.SuperIntEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.SuperIntVersion;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.tools.DateUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 商品期号
 */
@Entity
@Table(name = "goods_times")
public class GoodsTimes extends SuperIntVersion {

    /**
     * 期号，9位数，规则：8+MM+dd+四位流水号
     */
    private Integer times;

    /**
     * 商品
     */
    private GoodsInfo goodsInfo;

    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品提示信息
     */
    private String goodsTip;

    /**
     * 是否10元夺宝；当为false时，表示1元夺宝
     */
    private Boolean isTenYuan;

    /**
     * 总需人次
     */
    private Integer totalTimes;

    /**
     * 已购买总人次
     */
    private Integer totalBuyTimes = 0;

    /**
     * 夺宝进度，省略"%"
     */
    private Integer snatchProgress = 0;

    /**
     * a值
     */
    private Long avalue;

    /**
     * b值，时时彩开奖结果
     */
    private Long bvalue;

    /**
     * 幸运号码
     */
    private Integer luckNum;

    /**
     * 重庆时时彩期号
     */
    private String cqsscPeriodNo;

    /**
     * 买满时间
     */
    private Long fullTime;

    /**
     * 揭晓时间
     */
    private Date openTime;

    /**
     * 中奖用户
     */
    private User winngUser;

    /**
     * 中奖用户唯一标识，就是用户的userName
     */
    private String winngUserIdentity;

    /**
     * 中奖用户昵称
     */
    private String winngUserName;

    /**
     * 中奖用户头像
     */
    private String winngUserHeadImg;

    /**
     * 中奖用户购买次数
     */
    private Integer winngUserBuyTimes;

    /**中奖状态*/
    private Byte winngState;

    /**是否已晒单*/
    private Boolean hasShareGoods = false;

    /**是否第一次开奖*/
    private Boolean isFirstOpen = true;
    /**失败的开奖截至日期,到了就设置bvalue=0进行开奖*/
    private Date failDeadlineDate;
    /**能否获取时时彩开奖结果*/
    private Boolean canGetCqsscno = true;

    /**计算详情页面uri*/
    private String computeDetailUri;

    /**
     * 预设中奖用户id
     */
    private Integer presetWinnerUserId;
    /**
     * 预设中奖用户名
     */
    private String presetWinnerUserName;

    private Integer goodsId;

    /**
	0－未兑换
	1－已兑换积分
	2－未确认地址（已选择兑换商品）
	3－未发货（已确认地址）
	4－发货
	5 - 已收货（App端确认）
	6 - 已晒单 
     */
    private Integer exchangeState;
    
    /**
     * 物流信息
     */
    private String logisticsInfo;
    
    /**
     * Default constructor
     */
    public GoodsTimes() {
        super(Const.GoodsTimesState.GOING);
    }

    public GoodsTimes(Integer id) {
        super(id, Const.GoodsTimesState.GOING);
    }

    public GoodsTimes(Integer times, GoodsInfo goodsInfo, String goodsName,
            String goodsTip, String thumbnail, Boolean isTenYuan,
            Integer totalTimes, Integer totalBuyTimes, Integer snatchProgress) {
        super(Const.GoodsTimesState.GOING);
        this.times = times;
        this.goodsInfo = goodsInfo;
        this.goodsName = goodsName;
        this.goodsTip = goodsTip;
        this.thumbnail = thumbnail;
        this.isTenYuan = isTenYuan;
        this.totalTimes = totalTimes;
        this.totalBuyTimes = totalBuyTimes;
        this.snatchProgress = snatchProgress;
        }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "good_id")
    public GoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public Long getAvalue() {
        return avalue;
    }

    public void setAvalue(Long avalue) {
        this.avalue = avalue;
    }

    public Long getBvalue() {
        return bvalue;
    }

    public void setBvalue(Long bvalue) {
        this.bvalue = bvalue;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "winng_user_id")
    public User getWinngUser() {
        return winngUser;
    }

    public void setWinngUser(User winngUser) {
        this.winngUser = winngUser;
    }

    public String getWinngUserName() {
        return winngUserName;
    }

    public void setWinngUserName(String winngUserName) {
        this.winngUserName = winngUserName;
    }

    public Integer getWinngUserBuyTimes() {
        return winngUserBuyTimes;
    }

    public void setWinngUserBuyTimes(Integer winngUserBuyTimes) {
        this.winngUserBuyTimes = winngUserBuyTimes;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsTip() {
        return goodsTip;
    }

    public void setGoodsTip(String goodsTip) {
        this.goodsTip = goodsTip;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getTotalBuyTimes() {
        return totalBuyTimes;
    }

    public void setTotalBuyTimes(Integer totalBuyTimes) {
        this.totalBuyTimes = totalBuyTimes;
    }

    public Integer getSnatchProgress() {
        return snatchProgress;
    }

    public void setSnatchProgress(Integer snatchProgress) {
        this.snatchProgress = snatchProgress;
    }

    public Byte getWinngState() {
        return winngState;
    }

    public void setWinngState(Byte winngState) {
        this.winngState = winngState;
    }

    public Boolean getIsTenYuan() {
        return isTenYuan;
    }

    public void setIsTenYuan(Boolean isTenYuan) {
        this.isTenYuan = isTenYuan;
    }

    public Integer getLuckNum() {
        return luckNum;
    }

    public void setLuckNum(Integer luckNum) {
        this.luckNum = luckNum;
    }

    public String getWinngUserIdentity() {
        return winngUserIdentity;
    }

    public void setWinngUserIdentity(String winngUserIdentity) {
        this.winngUserIdentity = winngUserIdentity;
    }

    public String getWinngUserHeadImg() {
        return winngUserHeadImg;
    }

    public void setWinngUserHeadImg(String winngUserHeadImg) {
        this.winngUserHeadImg = winngUserHeadImg;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getHasShareGoods() {
        return hasShareGoods;
    }

    public void setHasShareGoods(Boolean hasShareGoods) {
        this.hasShareGoods = hasShareGoods;
    }

    public String getCqsscPeriodNo() {
        return cqsscPeriodNo;
    }

    public void setCqsscPeriodNo(String cqsscPeriodNo) {
        this.cqsscPeriodNo = cqsscPeriodNo;
    }

    public Long getFullTime() {
        return fullTime;
    }

    public void setFullTime(Long fullTime) {
        this.fullTime = fullTime;
    }

    public Boolean getIsFirstOpen() {
        return isFirstOpen;
    }

    public void setIsFirstOpen(Boolean isFirstOpen) {
        this.isFirstOpen = isFirstOpen;
    }

    public String getComputeDetailUri() {
        return computeDetailUri;
    }

    public void setComputeDetailUri(String computeDetailUri) {
        this.computeDetailUri = computeDetailUri;
    }

    public Date getFailDeadlineDate() {
        return failDeadlineDate;
    }

    public void setFailDeadlineDate(Date failDeadlineDate) {
        this.failDeadlineDate = failDeadlineDate;
    }

    public Boolean getCanGetCqsscno() {
        return canGetCqsscno;
    }

    public void setCanGetCqsscno(Boolean canGetCqsscno) {
        this.canGetCqsscno = canGetCqsscno;
    }

    public Integer getPresetWinnerUserId() {
        return presetWinnerUserId;
    }

    public void setPresetWinnerUserId(Integer presetWinnerUserId) {
        this.presetWinnerUserId = presetWinnerUserId;
    }

    public String getPresetWinnerUserName() {
        return presetWinnerUserName;
    }

    public void setPresetWinnerUserName(String presetWinnerUserName) {
        this.presetWinnerUserName = presetWinnerUserName;
    }

    // #################非持久化属性##############
    private String createTimeStr;
    private String openTimeStr;
    private Integer winngUserId;
    private Integer goodsTimesName;

    @Transient
    public String getCreateTimeStr() {
        if(StringUtils.isBlank(createTimeStr)){
            createTimeStr = DateUtils.dateToYYYYMMDDHHMMSSSSSString(getCreateTime());
        }
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    @Transient
    public String getOpenTimeStr() {
        if(StringUtils.isBlank(openTimeStr)){
            openTimeStr = DateUtils.dateToYYYYMMDDHHMMSSSSSString(getOpenTime());
        }
        return openTimeStr;
    }

    public void setOpenTimeStr(String openTimeStr) {
        this.openTimeStr = openTimeStr;
    }

    @Transient
    public Integer getWinngUserId() {
        if(winngUserId== null && getWinngUser()!=null){
            winngUserId=getWinngUser().getId();
        }
        return winngUserId;
    }

    public void setWinngUserId(Integer winngUserId) {
        this.winngUserId = winngUserId;
    }

    @Transient
    public Integer getGoodsTimesName() {
        return getTimes();
    }

    public void setGoodsTimesName(Integer goodsTimesName) {
        this.goodsTimesName = goodsTimesName;
    }

    @Transient
    public Integer getGoodsId() {
        if(goodsId== null && getGoodsInfo()!=null){
            goodsId=getGoodsInfo().getId();
        }
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
    
    public Integer getExchangeState() {
        return exchangeState;
    }

    public void setExchangeState(Integer exchangeState) {
        this.exchangeState = exchangeState;
    }
    
    public String getLogisticsInfo() {
        return logisticsInfo;
    }

    public void setLogisticsInfo(String logisticsInfo) {
        this.logisticsInfo = logisticsInfo;
    }
}