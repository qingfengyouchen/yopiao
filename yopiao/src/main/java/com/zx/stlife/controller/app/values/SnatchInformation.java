package com.zx.stlife.controller.app.values;

/**
 * 获取他人夺宝记录
 * @author shengping
 *
 */
public class SnatchInformation {

	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 商品缩略图相对路径
	 */
	private String thumbnail;
	
	/**
	 * 夺宝记录id
	 */
	private int snatchRecordId;
	
	/**
	 * 商品期号id
	 */
	private int goodsTimesId;
	
	/**
	 * 状态
	 */
	private int state;
	
	/**
	 * 参与期号
	 */
	private String goodsTimesName;
	
	/**
	 * 总需人次
	 */
	private int totalTimes;
	
	/**
	 * 已购买人次
	 */
	private int totalBuyTimes;
	
	/**
	 * 获奖者id
	 */
	private int winngUserId;
	
	/**
	 * 获奖者昵称
	 */
	private String winngUserName;
	
	/**
	 * 获奖者本期参与人次
	 */
	private int buyTimes;
	
	/**
	 * 幸运号码
	 */
	private int luckNum;
	
	/**
	 * 揭晓时间
	 */
	private String openTimeStr;

	
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getSnatchRecordId() {
		return snatchRecordId;
	}

	public void setSnatchRecordId(int snatchRecordId) {
		this.snatchRecordId = snatchRecordId;
	}

	public int getGoodsTimesId() {
		return goodsTimesId;
	}

	public void setGoodsTimesId(int goodsTimesId) {
		this.goodsTimesId = goodsTimesId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getGoodsTimesName() {
		return goodsTimesName;
	}

	public void setGoodsTimesName(String goodsTimesName) {
		this.goodsTimesName = goodsTimesName;
	}

	public int getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}

	public int getTotalBuyTimes() {
		return totalBuyTimes;
	}

	public void setTotalBuyTimes(int totalBuyTimes) {
		this.totalBuyTimes = totalBuyTimes;
	}

	public int getWinngUserId() {
		return winngUserId;
	}

	public void setWinngUserId(int winngUserId) {
		this.winngUserId = winngUserId;
	}

	public String getWinngUserName() {
		return winngUserName;
	}

	public void setWinngUserName(String winngUserName) {
		this.winngUserName = winngUserName;
	}

	public int getBuyTimes() {
		return buyTimes;
	}

	public void setBuyTimes(int buyTimes) {
		this.buyTimes = buyTimes;
	}

	public int getLuckNum() {
		return luckNum;
	}

	public void setLuckNum(int luckNum) {
		this.luckNum = luckNum;
	}

	public String getOpenTimeStr() {
		return openTimeStr;
	}

	public void setOpenTimeStr(String openTimeStr) {
		this.openTimeStr = openTimeStr;
	}
	
}
