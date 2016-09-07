package com.zx.stlife.controller.app.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.LockTimeoutException;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.SnatchListItem;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.SnatchListItemService;
import com.zx.stlife.tools.RandomUitls;

/**
 * 夺宝清单app
 * @author lxw
 *
 */
@RestController
@RequestMapping("/app/snatchList")
public class SnatchListItemRestController extends BaseRestController{

	@Autowired
	private SnatchListItemService snatchListItemService;
	
	@Autowired
	private GoodsTimesService goodsTimesService;
	
	/**
	 * 查看清单列表
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public JsonResult getList(@RequestParam("userId")Integer userId){
		return BizCommon.findSnatchListItem(userId, snatchListItemService, goodsTimesService);
	}
	
	/**
	 * 加入修改清单
	 * @param goodsTimesId 商品期数id
	 * @param userId 用户id
	 * @param amount 数量
	 * @return
	 */
	@RequestMapping(value="/edit/{goodsTimesId}",method=RequestMethod.POST)
	public JsonResult edit(@PathVariable("goodsTimesId")Integer goodsTimesId,@RequestParam("userId")Integer userId,@RequestParam("amount")Integer amount){
		GoodsTimes goodsTimes=goodsTimesService.get(goodsTimesId);
		SnatchListItem snatchListItem=snatchListItemService.findBySnatchListItem(userId, goodsTimes.getId());
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
	 * @param userId
	 * @param ids
	 */
	@RequestMapping(value="delete",method=RequestMethod.GET)
	//public JsonResult delete(Integer userId,String ids){
	public JsonResult delete(@RequestParam("userId")Integer userId,
			@RequestParam("ids")String ids){
		try {
			logger.info(userId.toString() + " : " + ids);
			List<Integer> list =new ArrayList<Integer>();
			String strIds[]=ids.split(",");
			for (String id : strIds) {
				list.add(Integer.parseInt(id));
			}
			snatchListItemService.delUserList(userId, list);
			return buildSuccessResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}
	
	
	/**
	 * 删除用户所有清单
	 * @param userId
	 */
	@RequestMapping(value="deleteUserAllList",method=RequestMethod.POST)
	public JsonResult delUserAllList(Integer userId){
		try {
			snatchListItemService.delUserAllList(userId);
			return buildSuccessResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}
	
}
