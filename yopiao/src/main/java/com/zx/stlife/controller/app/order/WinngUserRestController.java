package com.zx.stlife.controller.app.order;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.*;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.*;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;
import com.zx.stlife.vo.snatch.SnatchRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author shengping
 *	夺宝相关API
 *
 */
@RestController
@RequestMapping("/app/winng")
public class WinngUserRestController extends BaseRestController{

	@Autowired
	private GoodsTimesService goodsTimesService;

	@Autowired
	private WinngGoodsStateService winngGoodsStateService;

	@Autowired
	private WinngUserReceiveInfoService winngUserReceiveInfoService;

	/**
	 * 5.21. 查看中奖记录明细
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public JsonResult winngDetail(@RequestParam("gid") Integer goodsTimesId) {
		try {
			GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
			if (goodsTimes == null) {
				return buildFailureResult(301); // 参数错误
			}

			Map<String, Object> data = new HashMap<>();
			Map<String, Object> goodsTimesMap = ConvertUtils.convertEntityToMap(
					goodsTimes,
					new String[]{"id", "goodsTimesId"},
					new String[]{"goodsName"},
					new String[]{"thumbnail"},
					new String[]{"totalTimes"},
					new String[]{"times", "goodsTimesName"},
					new String[]{"luckNum"},
					new String[]{"winngUserBuyTimes"},
					new String[]{"openTimeStr"},
					new String[]{"winngState"});
			data.put("goodsTimes", goodsTimesMap);

			List<WinngGoodsState> winngGoodsStateList =
					winngGoodsStateService.findByGoodsTimes(goodsTimesId);
			List<Map<String, Object>> winngGoodsStateMapList =
					ConvertUtils.convertCollectionToListMap(winngGoodsStateList,
							"goodsStateId", "state", "isFinish", "finishTimeStr", "isCurrState"
					);
			data.put("goodsStates", winngGoodsStateMapList);

			WinngGoodsState currWinngGoodsState = winngGoodsStateService.getCurrent(goodsTimesId);
			Map<String, Object> receiveAddressMap = null;
			WinngUserReceiveInfo winngUserReceiveInfo = null;
			Byte currentState = currWinngGoodsState.getState();
			if(currentState > Const.WinngGoodsState.CONFIRM_ADDRESS ||
					( currWinngGoodsState.getIsFinish() &&
							currentState == Const.WinngGoodsState.CONFIRM_ADDRESS ))
			{
				winngUserReceiveInfo = winngUserReceiveInfoService.getByGoodsTimes(goodsTimesId);
				receiveAddressMap = ConvertUtils.convertEntityToMap(winngUserReceiveInfo,
						"receiver", "mobileNo", "address");
			}
			data.put("receiveAddress", receiveAddressMap);

			Map<String, Object> logisticsMap = null;
			if(currentState > Const.WinngGoodsState.DISPATCH ||
					( currWinngGoodsState.getIsFinish() &&
							currentState == Const.WinngGoodsState.DISPATCH ))
			{
				logisticsMap = ConvertUtils.convertEntityToMap(winngUserReceiveInfo,
						"logisticsName", "logisticsNo");
			}
			data.put("logistics", logisticsMap);

			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 5.22. 确认收货地址
	 * @param userId
	 * @param goodsTimesId
	 * @param raid 收货地址id
	 * @return
	 */
	@RequestMapping(value = "/confirmReceiveAddr", method = RequestMethod.POST)
	public JsonResult confirmReceiveAddr(@RequestParam("userId") Integer userId,
									 @RequestParam("gid") Integer goodsTimesId,
									 @RequestParam("raid") Integer raid)
	{
		return winngGoodsStateService.confirmReceiveAddr(userId, goodsTimesId, raid);
	}

	/**
	 * 5.23. 确认收货
	 * @param userId
	 * @param goodsTimesId
	 * @return
	 */
	@RequestMapping(value = "/confirmReceived", method = RequestMethod.POST)
	public JsonResult confirmReceived(@RequestParam("userId") Integer userId,
									 @RequestParam("gid") Integer goodsTimesId)
	{
		return winngGoodsStateService.confirmReceived(userId, goodsTimesId);
	}
}
