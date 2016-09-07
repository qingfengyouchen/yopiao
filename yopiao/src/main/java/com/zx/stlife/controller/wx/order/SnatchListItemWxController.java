package com.zx.stlife.controller.wx.order;
import static com.zx.stlife.controller.app.base.JsonResultUtils.buildFailureResult;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.LockTimeoutException;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.modules.util.Threads;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.SnatchListItem;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.SnatchListItemService;
import com.zx.stlife.tools.RandomUitls;

/**
 * 微信 清单
 */
@Controller
@RequestMapping("/wx/snatchList")
public class SnatchListItemWxController extends BaseController<SnatchListItem> {
	private static Logger logger = LoggerFactory.getLogger(SnatchListItemWxController.class);
	@Autowired
	private SnatchListItemService snatchListItemService;
	
	@Autowired
	private GoodsTimesService goodsTimesService;
	
	/**
	 * 查看清单列表
	 * @return
	 */
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String getList(Model model){
		Integer userId = UserWxUtils.getCurrUserId();
		logger.info("===userId:" + userId);
		logger.info("===snatchListItemService:" + snatchListItemService);
		logger.info("===goodsTimesService:" + goodsTimesService);
		JsonResult jsonResult= BizCommon.findSnatchListItem(userId, snatchListItemService, goodsTimesService);
		model.addAttribute("jsonResult", jsonResult).addAttribute("userId", userId);
		return "wx/order/snatch-listItem";
	}
	
	/**
	 * 添加购物车
	 * @param goodsTimesId 期号id
	 * @param amount 数量
	 */
	@RequestMapping(value="/editSnatchListItem",method=RequestMethod.POST)
	public @ResponseBody JsonResult editSnatchListItem(Integer goodsTimesId, Integer amount){
		Integer userId = UserWxUtils.getCurrUserId();
		GoodsTimes goodsTimes=goodsTimesService.get(goodsTimesId);
		SnatchListItem snatchListItem=snatchListItemService.findBySnatchListItem(userId, goodsTimes.getId());
		return edit(goodsTimes,snatchListItem,userId, amount, goodsTimesService, snatchListItemService);
	}
	
	/**
	 * 商品首页添加购物车
	 * @param goodsTimesId
	 * @param userId
	 * @param amount
	 */
	@RequestMapping(value="/addSnatchListItem",method=RequestMethod.POST)
	public @ResponseBody JsonResult addSnatchListItem(Integer goodsTimesId,Integer amount){
		Integer userId = UserWxUtils.getCurrUserId();
		GoodsTimes goodsTimes=goodsTimesService.get(goodsTimesId);
		SnatchListItem snatchListItem=snatchListItemService.findBySnatchListItem(userId, goodsTimes.getId());
		if(goodsTimes!=null){
			Byte state=Const.GoodsTimesState.OVER;
			if(goodsTimes.getState()!=state){ //是否已揭晓
				if(snatchListItem==null){
					return edit(goodsTimes,snatchListItem,userId, amount, goodsTimesService, snatchListItemService);
				}
			}
		}
		return buildFailureResult(301);
	}
	
	public JsonResult edit(GoodsTimes goodsTimes,SnatchListItem snatchListItem,Integer userId,Integer amount,
    		GoodsTimesService goodsTimesService,SnatchListItemService snatchListItemService){
		while (true) {
			try {
				return BizCommon.goodsTimesEdit(goodsTimes,snatchListItem,userId, amount, goodsTimesService, snatchListItemService);
			} catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
				logger.info("添加购物车出现并发访问...");
				Threads.sleep(RandomUitls.randomInt(100));
			} catch (CannotAcquireLockException | LockTimeoutException ex) {
				logger.info("添加购物车出现死锁，等待继续执行");
				Threads.sleep(RandomUitls.randomInt(300));
			} catch (Exception ex) {
				logger.error(ex.getClass().getName());
				logger.error("添加购物车出现系统错误：" + ex.getMessage(), ex);
				return buildFailureResult();
			}
		}
	}
	
	/**
	 * 删除清单
	 * @param listItemId
	 */
	
	@RequestMapping(value="/delUserList",method=RequestMethod.POST)
	public @ResponseBody Integer delUserList(Integer listItemId){
		Integer userId = UserWxUtils.getCurrUserId();
		Integer state=0;
		try {
			List<Integer> ids= new ArrayList<Integer>(1);
			ids.add(listItemId);
			snatchListItemService.delUserList(userId,ids);
			state=1;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return state;
	}
}
