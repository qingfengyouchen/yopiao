package com.zx.stlife.controller.app.order;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.*;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.*;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;
import com.zx.stlife.vo.snatch.SnatchRecordVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.nimbus.State;

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
@RequestMapping("/app/snatch/record")
public class SnatchRecordRestController extends BaseRestController{

	@Autowired
	private SnatchRecordService snatchRecordService;
	
	@Autowired
	private SnatchNumService snatchNumService;
	
	@Autowired
	private SnatchRecordDetailService snatchRecordDetailService;
	
	@Autowired
	private GoodsTimesService goodsTimesService;

	/**
	 * 查看夺宝清单
	 * @return
	 */
	@RequestMapping(value="/list/{userId}",method=RequestMethod.GET)
	public JsonResult getSnatch(
			@PathVariable("userId") Integer userId,
			@RequestParam(value = "timestamp", required = false) String timestamp,
			@RequestParam(value = "state", required = false) Byte state,
			Page<SnatchRecordVo> page)
	{
		Date date = DateUtilsEx.stringToDate(timestamp, DateUtilsEx.FORMAT_YYYYMMDDHHMMSS_SSS);
		if(date == null){
			date = DateUtilsEx.getNow();
		}
		try {
			snatchRecordService.findPageByUser(page, userId, state, date);

			return PageUtils.buildPage(page, "goodsName", "thumbnail", "isTenYuan", "snatchRecordId",
					"goodsTimesId", "state", "goodsTimesName", "totalTimes", "totalBuyTimes", "snatchProgress",
					"buyTimes", "winngUserId", "winngUserName", "winngUserBuyTimes", "luckNum", "openTimeStr");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 查看中奖清单
	 * @return
	 */
	@RequestMapping(value="/winngList/{userId}",method=RequestMethod.GET)
	public JsonResult getWinList(
			@PathVariable("userId") Integer userId,
			@RequestParam(value = "timestamp", required = false) String timestamp,
			@RequestParam(value = "state", required = false) Byte state,
			Page<GoodsTimes> page){
		return BizCommon.getWinListAttr(page, userId, timestamp, state, snatchRecordService);
	}
	
	/**
	 * lxw
	 * * 5.3. 查看用户夺宝号码
	 * @param snatchRecordDetailId  订单明细id
	 * @return
	 */
	@RequestMapping(value="/detail/{snatchRecordDetailId}/num",method=RequestMethod.GET)
	public JsonResult getDetailNum(@PathVariable("snatchRecordDetailId")Integer snatchRecordDetailId){
		try {
			List<SnatchNum> snatchNums=snatchNumService.findSnatchNum(snatchRecordDetailId);
			if(SimpleUtils.isNullList(snatchNums)){
				return buildSuccessResult();
			}
			String nums=ConvertUtils.convertPropertyToString(snatchNums, "num", ",");
			return buildSuccessResult(nums);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * lxw
	 * 5.2. 查看用户夺宝详情 
	 * @param snatchRecordId 夺宝记录id
	 * @return
	 */
	@RequestMapping(value="/{snatchRecordId}/detail",method=RequestMethod.GET)
	public JsonResult detail(@PathVariable()Integer snatchRecordId){
		try {
			SnatchRecord snatchRecord=snatchRecordService.getSnatchRecord(snatchRecordId);
			List<SnatchRecordDetail>  recordDetails=snatchRecordDetailService.findSnatchRecordDetail(snatchRecordId);
			if(snatchRecord==null){
				return buildSuccessResult();
			}else{
				Map<String, Object> data= ConvertUtils.convertEntityToMap(snatchRecord,"goodsName","goodsTimesName","buyTimes");
				List<Map<String, Object>> maps=ConvertUtils.convertCollectionToListMap(recordDetails, "id","createTimeStr","buyTimes");
				data.put("details", maps);
				return buildSuccessResult(data);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	
	/**
	 * lxw
	 * 5.9. 查看用户夺宝号码列表
	 * @param goodsTimesId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/num/list",method=RequestMethod.GET)
	public JsonResult getUserSnatchNum(@RequestParam("goodsTimesId")Integer goodsTimesId,
									   @RequestParam("userId")Integer userId){
		return BizCommon.getUserSnatchNum(goodsTimesId,userId,snatchRecordService,snatchNumService);
	}
	
	/**
	 * 5.5. 获取最新中奖用户列表
	 */
	@RequestMapping(value="/winngUser/lastList",method=RequestMethod.GET)
	public JsonResult winngUser(){
		
		try {
			List<GoodsTimes> list=goodsTimesService.winngUser();
			if(SimpleUtils.isNullList(list)){
				return buildSuccessResult();
			}
			List<Map<String, Object>> maps = ConvertUtils.convertCollectionToListMap(
					list,"id","goodsName","winngUserName","openTimeStr");
			return buildSuccessResult(maps);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

	/**
	 * 5.8. 查看夺宝中奖信息
	 */
	@RequestMapping(value = "/winngInfo", method = RequestMethod.GET)
	public JsonResult goodsTimes(@RequestParam("goodsTimesId") Integer goodsTimesId) {
		try {
			GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
			if (goodsTimes == null) {
				return buildSuccessResult();
			}
			Map<String, Object> maps = ConvertUtils.convertEntityToMap(
					goodsTimes, "goodsName", "times", "winngUserBuyTimes", "luckNum", "openTimeStr");
			return buildSuccessResult(maps);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}

}
