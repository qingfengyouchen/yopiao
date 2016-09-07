package com.zx.stlife.service.order;

import com.base.modules.util.ConvertUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.WinngGoodsState;
import com.zx.stlife.entity.order.WinngUserReceiveInfo;
import com.zx.stlife.entity.sys.ReceiveAddress;
import com.zx.stlife.repository.jpa.order.WinngGoodsStateDao;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.sms.SmsSendRecordService;
import com.zx.stlife.service.sys.ReceiveAddressService;
import com.zx.stlife.tools.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.zx.stlife.controller.app.base.JsonResultUtils.*;

import java.util.List;
import java.util.Map;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class WinngGoodsStateService {

	private static Logger logger = LoggerFactory.getLogger(WinngGoodsStateService.class);

	@Autowired
	private WinngGoodsStateDao winngGoodsStateDao;
	@Autowired
	private GoodsTimesService goodsTimesService;
	@Autowired
	private ReceiveAddressService receiveAddressService;
	@Autowired
	private WinngUserReceiveInfoService winngUserReceiveInfoService;
	@Autowired
	private SmsSendRecordService smsSendRecordService;

	@Transactional
	public void save(WinngGoodsState entity){
		entity = winngGoodsStateDao.save(entity);
	}

	@Transactional
	public void initWinngStates(GoodsTimes goodsTimes){

		if(goodsTimes.getWinngState() != Const.WinngState.CONFIRM_ADDRESS){
			goodsTimes.setWinngState(Const.WinngState.CONFIRM_ADDRESS);
			goodsTimesService.save(goodsTimes);
		}

		if(existsByGoodsTimes(goodsTimes.getId())){
			return;
		}

		for(Map.Entry<Byte, String> entry: Const.WinngGoodsState.MAP.entrySet()){
			boolean isFinish = entry.getKey() == Const.WinngGoodsState.GET;
			boolean isCurrState = entry.getKey() ==  Const.WinngGoodsState.CONFIRM_ADDRESS;
			WinngGoodsState winngUserState = new WinngGoodsState(
					entry.getKey(), goodsTimes.getWinngUser(), goodsTimes, isFinish, isCurrState);
			if(isFinish){
				winngUserState.setFinishTime(goodsTimes.getOpenTime());
			}
			save(winngUserState);
		}
	}

	public List<WinngGoodsState> findByGoodsTimes(Integer goodsTimesId){
		return winngGoodsStateDao.findByGoodsTimes(goodsTimesId);
	}

	public boolean existsByGoodsTimes(Integer goodsTimesId){
		Integer amount = winngGoodsStateDao.countByGoodsTimes(goodsTimesId);
		return amount != null && amount > 0;
	}

	public WinngGoodsState getByGoodsTimesAndState(Integer goodsTimesId, Byte state){
		return winngGoodsStateDao.getByGoodsTimesAndState(goodsTimesId, state);
	}

	/**
	 * 查询当前状态的数据
	 * @param goodsTimesId
	 * @return
	 */
	public WinngGoodsState getCurrent(Integer goodsTimesId){
		return winngGoodsStateDao.getCurrent(goodsTimesId);
	}

	/**
	 * 确认收货地址
	 * @param userId
	 * @param goodsTimesId
	 * @param raid
	 * @return
	 */
	@Transactional
	public JsonResult confirmReceiveAddr(Integer userId, Integer goodsTimesId, Integer raid){
		WinngGoodsState winngGoodsState = getCurrent(goodsTimesId);
		if(winngGoodsState == null){
			return buildFailureResult(301);	// 参数错误
		}

		if(winngGoodsState.getState() != Const.WxWinngGoodsState.CONFIRM_RECEIVE){
			return buildFailureResult(302);	// 状态不正确，不能确认收货地址
		}

		if(winngGoodsState.getUser().getId() != userId.intValue()){
			return buildFailureResult(303);	// 不能操作非本人的数据
		}

		ReceiveAddress receiveAddress = receiveAddressService.get(raid);
		if(receiveAddress == null){
			return buildFailureResult(304);	// 请选择收货地址
		}

		if(receiveAddress.getUser().getId() != userId.intValue()){
			return buildFailureResult(305);	// 只能选择自己的收货地址
		}

		winngGoodsState.setFinishTime(DateUtils.getNow());
		winngGoodsState.setIsFinish(true);
		winngGoodsState.setIsCurrState(false);
		save(winngGoodsState);

//		WinngGoodsState nextWinngGoodsState = getByGoodsTimesAndState(
//				goodsTimesId, Const.WinngGoodsState.OVER);
		WinngGoodsState nextWinngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WinngGoodsState.CONFIRM_RECEIVE);
		nextWinngGoodsState.setIsFinish(false);
		nextWinngGoodsState.setIsCurrState(true);
		save(nextWinngGoodsState);

		GoodsTimes goodsTimes = winngGoodsState.getGoodsTimes();
		goodsTimes.setWinngState(Const.WinngState.WAITING_DISPATCH);
		goodsTimes.setExchangeState((int)Const.exchangeState.NO_SHIPPED);
		goodsTimes.setLogisticsInfo(receiveAddress.getProvince()
									+receiveAddress.getCity()
									+receiveAddress.getDistrict()
									+receiveAddress.getDetailAddress());
		goodsTimesService.save(goodsTimes);

		WinngUserReceiveInfo winngUserReceiveInfo = new WinngUserReceiveInfo(
				winngGoodsState.getUser(), goodsTimes,
				receiveAddress.getReceiver(), receiveAddress.getTel(), receiveAddress.getFullAddress()
		);
		winngUserReceiveInfoService.save(winngUserReceiveInfo);

		return buildSuccessResult();
	}

	/**
	 * 确认收货
	 * @param userId
	 * @param goodsTimesId
	 * @return
	 */
	@Transactional
	public JsonResult confirmReceived(Integer userId, Integer goodsTimesId){
		WinngGoodsState winngGoodsState = getCurrent(goodsTimesId);
		if(winngGoodsState == null){
			return buildFailureResult(301);	// 参数错误
		}

		if(winngGoodsState.getState() != Const.WinngGoodsState.CONFIRM_RECEIVE){
			return buildFailureResult(302);	// 状态不正确，不能确认收货
		}

		if(winngGoodsState.getUser().getId() != userId.intValue()){
			return buildFailureResult(303);	// 不能操作非本人的数据
		}

		winngGoodsState.setFinishTime(DateUtils.getNow());
		winngGoodsState.setIsFinish(true);
		winngGoodsState.setIsCurrState(false);
		save(winngGoodsState);

		GoodsTimes goodsTimes = winngGoodsState.getGoodsTimes();
		goodsTimes.setWinngState(Const.WinngState.OVER);
		goodsTimesService.save(goodsTimes);

		WinngGoodsState nextWinngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WinngGoodsState.OVER);
		nextWinngGoodsState.setIsFinish(false); // 未完成，晒单
		nextWinngGoodsState.setIsCurrState(true);
		save(nextWinngGoodsState);

		return buildSuccessResult();
	}

	/**
	 * 派发商品，填写物流信息
	 * @param goodsTimesId
	 * @param logisticsName
	 * @param logisticsNo
	 */
	@Transactional
	public void dispatchGoods(Integer goodsTimesId, String logisticsName, String logisticsNo){
		winngUserReceiveInfoService.saveLogisticsInfo(goodsTimesId, logisticsName, logisticsNo);

		WinngGoodsState winngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WinngGoodsState.DISPATCH);
		winngGoodsState.setFinishTime(DateUtils.getNow());
		winngGoodsState.setIsFinish(true);
		winngGoodsState.setIsCurrState(false);
		save(winngGoodsState);

		WinngGoodsState nextWinngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WinngGoodsState.CONFIRM_RECEIVE);
		nextWinngGoodsState.setIsFinish(false);
		nextWinngGoodsState.setIsCurrState(true);
		save(nextWinngGoodsState);

		GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
		goodsTimes.setWinngState(Const.WinngState.CONFIRM_RECEIVE);
		goodsTimesService.save(goodsTimes);

		String mobileNo = winngUserReceiveInfoService.getMobileNoByGoodsTimes(goodsTimesId);
		smsSendRecordService.sendSms4DispatchGoods(mobileNo, logisticsName, logisticsNo);
	}

	/**
	 * 完成（晒单成功操作的）
	 * @param goodsTimesId
	 */
	@Transactional
	public void finish(Integer goodsTimesId){
		WinngGoodsState winngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WinngGoodsState.OVER);
		winngGoodsState.setIsFinish(true);
		winngGoodsState.setFinishTime(DateUtils.getNow());
		save(winngGoodsState);

		/*GoodsTimes goodsTimes = winngGoodsState.getGoodsTimes();
		goodsTimes.setWinngState(Const.WinngState.OVER);
		goodsTimesService.save(goodsTimes);*/
	}
	
	/**
	 * 选择兑换方式
	 * @param userId
	 * @param goodsTimesId
	 * @return
	 */
	@Transactional
	public String confirmChangeType(Integer userId, Integer goodsTimesId){
		WinngGoodsState winngGoodsState = getCurrent(goodsTimesId);
		if(winngGoodsState == null){
			return "0";
		}

		// 原来状态不是 选择领取方式 的话报错
		if(winngGoodsState.getState() != Const.WxWinngGoodsState.CONFIRM_ADDRESS){
			return "0";
		}

		if(winngGoodsState.getUser().getId() != userId.intValue()){
			return "0";
		}

		winngGoodsState.setFinishTime(DateUtils.getNow());
		winngGoodsState.setIsFinish(true);
		winngGoodsState.setIsCurrState(false);
		save(winngGoodsState);

		WinngGoodsState nextWinngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WxWinngGoodsState.OVER);
		nextWinngGoodsState.setIsFinish(true); // 未完成，晒单
		nextWinngGoodsState.setIsCurrState(true);
		save(nextWinngGoodsState);

		return "1";
	}
	
	/**
	 * 确认收货地址
	 * @param userId
	 * @param goodsTimesId
	 * @return
	 */
	@Transactional
	public String confirmAddr(Integer userId, Integer goodsTimesId){
		WinngGoodsState winngGoodsState = getCurrent(goodsTimesId);
		if(winngGoodsState == null){
			return "0";
		}

		// 原来状态不是 选择领取方式 的话报错
		if(winngGoodsState.getState() != Const.WxWinngGoodsState.CONFIRM_ADDRESS){
			return "0";
		}

		if(winngGoodsState.getUser().getId() != userId.intValue()){
			return "0";
		}

		winngGoodsState.setFinishTime(DateUtils.getNow());
		winngGoodsState.setIsFinish(true);
		winngGoodsState.setIsCurrState(false);
		save(winngGoodsState);

		WinngGoodsState nextWinngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WxWinngGoodsState.CONFIRM_RECEIVE);
		nextWinngGoodsState.setIsFinish(false); // 未完成，晒单
		nextWinngGoodsState.setIsCurrState(true);
		save(nextWinngGoodsState);

		return "1";
	}
	
	/**
	 * 商品派发
	 * @param userId
	 * @param goodsTimesId
	 * @return
	 */
	@Transactional
	public String confirmSend(Integer userId, Integer goodsTimesId){
		WinngGoodsState winngGoodsState = getCurrent(goodsTimesId);
		if(winngGoodsState == null){
			return "0";
		}

		// 原来状态不是 选择领取方式 的话报错
		if(winngGoodsState.getState() != Const.WxWinngGoodsState.CONFIRM_RECEIVE){
			return "0";
		}

		if(winngGoodsState.getUser().getId() != userId.intValue()){
			return "0";
		}

		winngGoodsState.setFinishTime(DateUtils.getNow());
		winngGoodsState.setIsFinish(true);
		winngGoodsState.setIsCurrState(false);
		save(winngGoodsState);

		WinngGoodsState nextWinngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WxWinngGoodsState.DISPATCH);
		nextWinngGoodsState.setIsFinish(false); // 未完成，晒单
		nextWinngGoodsState.setIsCurrState(true);
		save(nextWinngGoodsState);

		return "1";
	}
	
	/**
	 * 后台发货更新order_winning_state
	 * @param userId
	 * @param goodsTimesId
	 * @return
	 */
	@Transactional
	public String finishFahuo(Integer userId, Integer goodsTimesId){
		WinngGoodsState winngGoodsState = getCurrent(goodsTimesId);
		if(winngGoodsState == null){
			return "0";
		}

		// 原来状态不是 选择领取方式 的话报错
		if(winngGoodsState.getState() != Const.WxWinngGoodsState.DISPATCH){
			return "0";
		}

		winngGoodsState.setFinishTime(DateUtils.getNow());
		winngGoodsState.setIsFinish(true);
		winngGoodsState.setIsCurrState(false);
		save(winngGoodsState);

		WinngGoodsState nextWinngGoodsState = getByGoodsTimesAndState(
				goodsTimesId, Const.WxWinngGoodsState.OVER);
		nextWinngGoodsState.setIsFinish(true); // 未完成，晒单
		nextWinngGoodsState.setIsCurrState(true);
		save(nextWinngGoodsState);

		return "1";
	}
}
