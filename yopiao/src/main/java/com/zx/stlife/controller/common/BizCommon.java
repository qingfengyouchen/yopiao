package com.zx.stlife.controller.common;

import static com.zx.stlife.controller.app.base.JsonResultUtils.buildFailureResult;
import static com.zx.stlife.controller.app.base.JsonResultUtils.buildSuccessResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.LockTimeoutException;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.goods.ShareGoods;
import com.zx.stlife.entity.order.SnatchListItem;
import com.zx.stlife.entity.order.SnatchRecord;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.entity.record.RedPack;
import com.zx.stlife.entity.service.Activity;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.goods.ShareGoodsService;
import com.zx.stlife.service.order.SnatchListItemService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordDetailService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.record.RedPackService;
import com.zx.stlife.service.service.ActivityService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;
import com.zx.stlife.tools.RandomUitls;
import com.zx.stlife.tools.thread.ThreadWorkUtils;
import com.zx.stlife.vo.snatch.SnatchRecordVo;
import com.zx.stlife.vo.snatch.SnatchRequestVo;
import com.zx.stlife.vo.snatch.SnatchVo;

/**
 * Created by micheal on 16/1/15.
 */
public class BizCommon {
	private static Logger logger = LoggerFactory.getLogger(BizCommon.class);

	/**
	 * 查询商品期号信息
	 * 
	 * @param page
	 * @param timestamp
	 * @param category
	 * @param order
	 * @param goodsTimesService
	 * @return
	 */
	public static JsonResult getGoodsTimesByAttr(Page<GoodsTimes> page,
			String timestamp, Integer category, String order,
			GoodsTimesService goodsTimesService) {
		Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
		try {
			goodsTimesService.findGoodsTimesByAttr(page, date, category, order);

			return PageUtils.buildPage(page, "id", "goodsId", "goodsName", "thumbnail",
					"isTenYuan", "totalTimes", "totalBuyTimes",
					"snatchProgress", "state");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 查询商品期号信息
	 * 
	 * @param page
	 * @param timestamp
	 * @param category
	 * @param order
	 * @param goodsTimesService
	 * @return
	 */
	public static JsonResult getGoodsTimesByAttrAndName(Page<GoodsTimes> page,
			String timestamp, Integer category, String goodName, String order,
			GoodsTimesService goodsTimesService) {
		Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
		try {
			goodsTimesService.findGoodsTimesByAttrAndName(page, date, category, order, goodName);

			return PageUtils.buildPage(page, "id", "goodsName", "thumbnail",
					"isTenYuan", "totalTimes", "totalBuyTimes",
					"snatchProgress", "state");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 晒单首页/用户晒单列表（查询所有的已审核的晒单信息）
	 * 
	 * @param page
	 * @param timestamp
	 * @param userId
	 * @param shareGoodsService
	 * @return
	 */
	public static JsonResult getShareGoodsAttr(Page<ShareGoods> page,
			String timestamp, Integer userId,
			ShareGoodsService shareGoodsService) {
		try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			shareGoodsService.searchAll(date, page, userId);

			if (SimpleUtils.isNullList(page.getResult())) {
				return buildSuccessResult();
			}
			Map<String, Object> data = ConvertUtils.convertEntityToMap(page,
					"pageNo", "totalPages", "totalCount");
			List<Map<String, Object>> result = shareGoodsService.bindData(page);
			data.put("result", result);
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}

	}

	/**
	 * 查看晒单列表
	 * 
	 * @param page
	 * @param timestamp
	 * @param goodsId
	 * @param shareGoodsService
	 * @return
	 */
	public static JsonResult getShareGoodsAttrByGoods(Page<ShareGoods> page,
			String timestamp, Integer goodsId,
			ShareGoodsService shareGoodsService) {
		try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			shareGoodsService.pageSearch(goodsId, date, page);

			if (SimpleUtils.isNullList(page.getResult())) {
				return buildSuccessResult();
			}
			Map<String, Object> data = ConvertUtils.convertEntityToMap(page,
					"pageNo", "totalPages", "totalCount");
			List<Map<String, Object>> result = shareGoodsService.bindData(page);
			data.put("result", result);
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}

	}

	/**
	 * 查看中奖清单
	 * 
	 * @return
	 */
	public static JsonResult getWinListAttr(Page<GoodsTimes> page,
			Integer userId, String timestamp, Byte state,
			SnatchRecordService snatchRecordService) {
		Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);

		try {
			snatchRecordService
					.findWinListPageByUser(page, userId, date, state);
			if (SimpleUtils.isNullList(page.getResult())) {
				return buildSuccessResult();
			}
			return PageUtils.buildPage(page, "id", "goodsId", "goodsName", "thumbnail",
					"times", "luckNum", "winngUserBuyTimes", "totalTimes",
					"openTimeStr", "winngState", "openTime", "hasShareGoods",
					"exchangeState");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 查询个人夺宝记录
	 * 
	 * @param page
	 * @param userId
	 * @param state
	 * @param timestamp
	 * @param snatchRecordService
	 * @return
	 */
	public static JsonResult getSnatchRecordByAttr(Page<SnatchRecordVo> page,
			Integer userId, Byte state, String timestamp,
			SnatchRecordService snatchRecordService) {
		Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
		try {
			snatchRecordService.findPageByUser(page, userId, state, date);
			return PageUtils.buildPage(page, "state", "thumbnail",
					"goodsTimesId", "goodsName", "goodsTimesName",
					"totalTimes", "buyTimes", "totalBuyTimes",
					"snatchProgress", "winngUserId", "winngUserName",
					"winngUserBuyTimes", "luckNum", "openTime","openTimeStr");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}


	/**
	 * 查询红包列表
	 * @return
	 */
	public static JsonResult getRedPackListByAttr(Page<RedPack> page,
			Integer userId, Byte type, String timestamp,
			RedPackService redPackService) {
		Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
		try {
			redPackService.findList(userId, date, page, type);
			return PageUtils.buildPage(page, "total", "balance",
					"expireTime", "state","category");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
    /**
     * 5.6. 查看商品详情中夺宝记录列表
     * @param snatchRecordDetailService
     * @param page
     * @param timestamp
     * @param goodsTimesId
     * @return
     */
    public static JsonResult findSnatchRecordDetailByGoodsTimes(
			SnatchRecordDetailService snatchRecordDetailService, Page<SnatchRecordDetail> page,
			String timestamp,Integer goodsTimesId){
		try {
			Date date= DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			snatchRecordDetailService.findList(page, goodsTimesId, date);
			return PageUtils.buildPage(page, "userId", "userNickName", "buyTimes", "userHeadImg", "snatchTimeStr");
		} catch (Exception ex) {

			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
    }
	
    /**
     * 5.9. 查看用户夺宝号码列表
     * @param goodsTimesId
     * @param userId
     * @param snatchRecordService
     * @param snatchNumService
     * @return
     */
	public static JsonResult getUserSnatchNum(Integer goodsTimesId,Integer userId,SnatchRecordService snatchRecordService,SnatchNumService snatchNumService){
		try {
			SnatchRecord snatchRecord=snatchRecordService.findByUserAndGoodsTimes(userId,goodsTimesId);
			if(snatchRecord==null){
				return buildSuccessResult();
			}else{
				Map<String, Object> data= ConvertUtils.convertEntityToMap(snatchRecord, "goodsName","goodsTimesId",
				"goodsTimesName","buyTimes", "lastSnatchTime");
				List<Integer> nums = snatchNumService.findNumsByGoodsTimesAndUser(goodsTimesId, userId);
				data.put("nums", nums);
				return buildSuccessResult(data);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
    
	/**
	 * 5.7. 查看往期揭晓
	 */
    public static JsonResult findGoodsTimesRecords(Integer goodsId,String timestamp,
												   Page<GoodsTimes> page, GoodsTimesService goodsTimesService) {
    	try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			goodsTimesService.getRecord(date, page, goodsId);
			return PageUtils.buildPage(page, "id",
					"times", "state", "openTimeStr", "winngUserIdentity", "winngUserId",
					"winngUserName", "winngUserHeadImg", "winngUserBuyTimes", "luckNum");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	public static JsonResult findActivityRecords(Page<Activity> page,String title, 
			ActivityService activityService) {
		try {
			Map<String, String> params = new HashMap<String,String>();
			params.put("title", title);
			params.put("type", null);
			activityService.search(page,params);
			return PageUtils.buildPage(page, "id", "thumbImgUrl", "state",
					"imgUrl", "activityTime", "title",
					"maxJoinAmount", "hasJoinAmount", "address","htmlUrl");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
    
    /**
     * 4.2. 查看清单列表
     * @param userId
     */
    public static JsonResult findSnatchListItem(Integer userId,
    		SnatchListItemService snatchListItemService, GoodsTimesService goodsTimesService){
		try {
			List<SnatchListItem> snatchListItems=snatchListItemService.getList(userId);
			logger.info("=======snatchListItems:" + snatchListItems);
			logger.info("=======snatchListItems.size():" + snatchListItems.size());
			if(SimpleUtils.isNullList(snatchListItems)){
				return buildSuccessResult();
			}
			List<Map<String, Object>> lists=new ArrayList<Map<String, Object>>(snatchListItems.size());
			for (SnatchListItem listItem : snatchListItems) {
				logger.info("listItem:" + listItem);
				logger.info("listItem.getState():" + listItem.getState());
				if(listItem.getState()==Const.GoodsTimesState.GOING){
					GoodsTimes goodsTimes = listItem.getGoodsTimes();
					logger.info("goodsTimes:" + goodsTimes);
					logger.info("goodsTimes.getState():" + goodsTimes.getState());
			        Integer newestTimesid = 0;
			        if (goodsTimes.getState() != Const.GoodsTimesState.GOING) {// 非进行中状态才有去判断有没有最新一期
			        	logger.info("=====goodsTimes.getGoodsInfo().getId():" + goodsTimes.getGoodsInfo().getId());
			        	newestTimesid = goodsTimesService.getNewestTimesid(goodsTimes.getGoodsInfo().getId());
			        } else {
			        	newestTimesid = goodsTimes.getId();
			        }
			        
			        logger.info("newestTimesid:" + newestTimesid);
			        goodsTimes = goodsTimesService.get(newestTimesid);
					
					Map<String, Object> map=new HashMap<String,Object>(10);
					map.put("id", listItem.getId());
					map.put("goodsTimesId", goodsTimes.getId());
					map.put("times", goodsTimes.getTimes());
					map.put("goodsName", goodsTimes.getGoodsName());
					map.put("thumbnail", goodsTimes.getThumbnail());
					map.put("goodsTip", goodsTimes.getGoodsTip());
					map.put("isTenYuan", goodsTimes.getIsTenYuan());
					map.put("totalTimes", goodsTimes.getTotalTimes());
					map.put("totalBuyTimes", goodsTimes.getTotalBuyTimes());
					map.put("buyTimes", listItem.getBuyTimes());
					lists.add(map);
				}else{
					snatchListItemService.deleteEntity(listItem);
				}
			}
			return buildSuccessResult(lists);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
    }
    
    /**
     * 4.1. 加入或修改清单商品数量
     * @param goodsTimes 期号
     * @param userId 用户id
     * @param amount 数量
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public static JsonResult goodsTimesEdit(GoodsTimes goodsTimes,SnatchListItem snatchListItem,Integer userId,Integer amount,
    		GoodsTimesService goodsTimesService,SnatchListItemService snatchListItemService) throws Exception{ 
			Byte state=Const.GoodsTimesState.OVER;
			if(goodsTimes!=null){
				if(goodsTimes.getState()!=state){ //是否已揭晓
					Integer totalTimes =goodsTimes.getTotalTimes(); //总购买人数
					Integer totalBuyTimes=goodsTimes.getTotalBuyTimes();  //已购买人数
					Integer totalBuyRemainTimes=totalTimes-totalBuyTimes; //可购买人数
					if(snatchListItem!=null){
						amount=(totalBuyRemainTimes>amount)?amount:totalTimes-totalBuyTimes;
						snatchListItemService.update(snatchListItem.getId(), amount,amount);
					}else{
						if(totalBuyRemainTimes==0){ //购买人数已满
							return buildFailureResult(301);
						}
						snatchListItem=new SnatchListItem();
						snatchListItem.setGoodsInfo(goodsTimes.getGoodsInfo());
						snatchListItem.setGoodsTimes(goodsTimes);
						snatchListItem.setUser(new User(userId));
						snatchListItem.setBuyTimes(amount);
						snatchListItem.setMoney(amount); //金额
						snatchListItem.setState(Const.CommonState.ENABLE);
						snatchListItemService.save(snatchListItem);
					}
					Map<String, Object> map =new HashMap<String, Object>();
					map.put("amount", amount);
					return buildSuccessResult(map);
				}
				if(snatchListItem!=null){
					snatchListItemService.deleteEntity(snatchListItem);
				}
			}
			return buildFailureResult(301);
    }

    /**
     * 6.3.我的晒单
     */
    public static JsonResult myShareGoodsByJson(Page<GoodsTimes> page,String timestamp,
    		Integer userId ,ShareGoodsService shareGoodsService,
    		GoodsTimesService goodsTimesService){
    	try{
	    	Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			goodsTimesService.searchGoodsTimeList(page, date, userId);
	
			Map<String, Object> data = ConvertUtils.convertEntityToMap(page,
					"pageNo", "totalPages", "totalCount");
			List<Map<String, Object>> result = shareGoodsService.bindDateByMy(page);
			data.put("result", result);
			return buildSuccessResult(data);
    	} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
    }
    

	/**
	 * 查询最新揭晓
	 */
	public static JsonResult getListLastOpenByAttr(Page<GoodsTimes> page,
			String timestamp, GoodsTimesService goodsTimesService) {
		try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			goodsTimesService.findLastOpen(page, date);
			return PageUtils.buildPage(page, "id", "times", "state",
					"openTimeStr", "winngUserIdentity", "winngUserId",
					"winngUserName", "winngUserHeadImg", "winngUserBuyTimes",
					"luckNum", "goodsName", "openTime", "thumbnail",
					"canGetCqsscno");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	public static JsonResult pay(User user, SnatchRequestVo snatchRequestVo,
								 SnatchListItemService snatchListItemService, SnatchRecordService snatchRecordService,
								 HttpServletRequest request, boolean isWeixinH5Pay){
		List<Byte> payWays = snatchRequestVo.getPayWays();
		List<SnatchVo> snatchVoList = snatchRequestVo.getSnatchList();
		List<Integer> goodsTimesIds = snatchRequestVo.getGoodsTimesIds();
		if (SimpleUtils.isNullList(snatchVoList)) {
			ThreadWorkUtils.deleteSnatchList(snatchListItemService, goodsTimesIds);
			return buildFailureResult(301); // 没有夺宝商品
		}

		int totalTimes = 0;
		boolean inValidAmount = false;
		for (SnatchVo snatchVo : snatchVoList) {
			Integer amount = snatchVo.getAmount();
			if (amount == null || amount < 1) {
				inValidAmount = true;
				break;
			}
			totalTimes += amount;
		}
		if (inValidAmount) {
			ThreadWorkUtils.deleteSnatchList(snatchListItemService, goodsTimesIds);
			return buildFailureResult(302); // 购买人次不能小于1
		}

		if (SimpleUtils.isNullList(payWays)) {
			ThreadWorkUtils.deleteSnatchList(snatchListItemService, goodsTimesIds);
			return buildFailureResult(303); // 请选择支付方式
		}


		if(user == null){
			ThreadWorkUtils.deleteSnatchList(snatchListItemService, goodsTimesIds);
			return buildFailureResult(330); // 用户错误
		}

		while (true) {
			try {
				logger.info("=======snatchRecordService.payOrOrders before");
				Map<String, Object> resultMap= snatchRecordService
						.payOrOrders( snatchRequestVo, totalTimes, request, isWeixinH5Pay);
				logger.info("=======snatchRecordService.payOrOrders after");
				List<GoodsTimes> preopenGoodsTimesList =
						(List<GoodsTimes>)resultMap.get("preopenGoodsTimesList");
				//若有待开奖的期号，则开启下一期和产生全站50条购买记录
				ThreadWorkUtils.createNextGoodsTimesAndBuyRecord(preopenGoodsTimesList);

				// 微信端支付后清空购物车（IOS端不删除）
				if (isWeixinH5Pay) {
					ThreadWorkUtils.deleteSnatchList(snatchListItemService, goodsTimesIds);
				}

				return (JsonResult)resultMap.get("result");
			} catch (BizException ex) {
				if(ex.getErrorState() != 0) {
					return buildFailureResult(ex.getErrorState());
				}else{
					logger.error("下单/支付 - 系统错误1：" + ex.getMessage(), ex);
					return buildFailureResult();
				}
			} catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
				logger.info("下单/支付 - 出现并发访问...");
				Threads.sleep(RandomUitls.randomInt(100));
			} catch(CannotAcquireLockException | LockTimeoutException ex){
				logger.info("下单/支付 - 出现死锁，等待继续执行");
				Threads.sleep(RandomUitls.randomInt(300));
			} catch (Exception ex) {
				logger.error(ex.getClass().getName());
				logger.error("下单/支付 - 系统错误2：" + ex.getMessage(), ex);
				ThreadWorkUtils.deleteSnatchList(snatchListItemService, goodsTimesIds);
				return buildFailureResult();
			}
		}
	}

	/**
	 * 取消支付订单
	 * @param snatchRecordService
	 * @param userId
	 * @param outTradeNo
	 * @return
	 */
	public static JsonResult cancelPay(SnatchRecordService snatchRecordService, Integer userId, String outTradeNo){
		while (true) {
			try {
				return snatchRecordService.cancelPay(userId, outTradeNo);
			}catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
				logger.warn("微信/支付宝取消支付 - 出现并发访问...");
				Threads.sleep(RandomUitls.randomInt(100));
			} catch(CannotAcquireLockException | LockTimeoutException ex){
				logger.info("微信/支付宝取消支付 - 出现死锁，等待继续执行");
				Threads.sleep(RandomUitls.randomInt(300));
			} catch (Exception ex) {
				logger.error("微信/支付宝取消支付 - 系统错误：" + ex.getMessage(), ex);
				return buildFailureResult();
			}
		}
	}
}
