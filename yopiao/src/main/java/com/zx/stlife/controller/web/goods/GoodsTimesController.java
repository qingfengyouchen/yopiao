package com.zx.stlife.controller.web.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.LockTimeoutException;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.base.jpa.query.Page;
import com.base.modules.cache.memcached.SpyMemcachedClient;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.base.exception.BizException;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.ExpressConst;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.notice.Notice;
import com.zx.stlife.entity.notice.NoticeSendRecord;
import com.zx.stlife.entity.sys.ReceiveAddress;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.sys.ReceiveAddressDao;
import com.zx.stlife.service.goods.GoodsCategoryService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.notice.NoticeService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.order.WinngGoodsStateService;
import com.zx.stlife.service.order.WinngUserReceiveInfoService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.tools.RandomUitls;
import com.zx.stlife.tools.jpush.JPushUtils;
import com.zx.stlife.tools.snatch.BuyAllParams;
import com.zx.stlife.tools.snatch.BuyAllUtils;
import com.zx.stlife.tools.thread.ThreadWorkUtils;
import com.zx.stlife.vo.snatch.SnatchRecord4GoodsTimesVo;

@Controller
@RequestMapping(value = "/goods/goodsTimes")
public class GoodsTimesController extends BaseController {
	@Autowired
	private GoodsTimesService service;
	@Autowired
	private GoodsCategoryService goodsCategoryService;
	@Autowired
	private WinngUserReceiveInfoService winngUserReceiveInfoService;
	@Autowired
	private WinngGoodsStateService winngGoodsStateService;
	@Autowired
	private SpyMemcachedClient spyMemcachedClient;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SnatchRecordService snatchRecordService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ReceiveAddressDao receiveAddressDao;

	private static final String PERMISSION_PREFIX = "goods:goodsTimes";
	private static final String VIEW_URI_PREFIX = "goods/goodsTimes/goods-times-";

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page page, ParamsEntity paramsEntity) {
		service.search(page, paramsEntity.getParams(), false);
		bindData(model);
		model.addAttribute("page", page)
			.addAttribute("waittingState", Const.GoodsTimesState.WAITING);

		return VIEW_URI_PREFIX + "list-content";
	}

	@RequiresPermissions(PERMISSION_PREFIX + ":settingDraw")
	@RequestMapping(value = "list-setting")
	public String listSetting(Model model, Page page, ParamsEntity paramsEntity) {
		listSettingContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list-setting";
	}

	@RequiresPermissions(PERMISSION_PREFIX + ":settingDraw")
	@RequestMapping(value = "list-setting-content")
	public String listSettingContent(Model model, Page page, ParamsEntity paramsEntity) {
		service.search(page, paramsEntity.getParams(), true);
		bindData(model);
		model.addAttribute("page", page)
				.addAttribute("booleanMap", Const.booleanMap)
				.addAttribute("goodsTimesStateMap", Const.GoodsTimesState.SETTING_MAP)
				.addAttribute("goodsCategoryList", goodsCategoryService.findAllWithIdAndName())
				.addAttribute("waittingState", Const.GoodsTimesState.WAITING);

		return VIEW_URI_PREFIX + "list-setting-content";
	}

	@RequiresPermissions("goods:winner:view")
	@RequestMapping(value = "listWinng")
	public String listWinng(Model model, Page page, ParamsEntity paramsEntity) {
		listContentWinng(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list-winng";
	}

	@RequiresPermissions("goods:winner:view")
	@RequestMapping(value = "list-contentWinng")
	public String listContentWinng(Model model, Page page, ParamsEntity paramsEntity) {
		service.searchWinng(page, paramsEntity.getParams());
		bindData(model);
		model.addAttribute("page", page);

		return VIEW_URI_PREFIX + "list-content-winng";
	}

	/**
	 * 查看期号详细信息
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", service.get(id));
		bindData(model);
		return VIEW_URI_PREFIX + "form";
	}

	/**
	 * 打开设置中奖
	 */
	@RequiresPermissions(PERMISSION_PREFIX + ":settingDraw")
	@RequestMapping(value = "editSetting/{id}", method = RequestMethod.GET)
	public String editSetting(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		BuyAllParams buyAllParams = BuyAllUtils.getBuyAllParams(
				id, spyMemcachedClient);
		GoodsTimes entity = service.get(id);
		model.addAttribute("entity", entity)
				.addAttribute("buyAllParams", buyAllParams);
		bindData(model);
		if(entity.getState() == Const.GoodsTimesState.GOING){
			int maxUser = entity.getTotalTimes() - entity.getTotalBuyTimes();
			if(entity.getIsTenYuan()){
				maxUser /= 10;
			}
			maxUser /= 2;
			model.addAttribute("maxUser", maxUser);
		}
		return VIEW_URI_PREFIX + "form-setting";
	}

	@RequiresPermissions(PERMISSION_PREFIX + ":settingDraw")
	@RequestMapping(value = "buyAll", method = RequestMethod.POST)
	@ResponseBody
	public String buyAll(@Valid BuyAllParams entity, BindingResult result) {
		if(result.hasErrors()){
			String errMsg = "";
			for(ObjectError error: result.getAllErrors()){
				errMsg += error.getDefaultMessage();
			}
			return errMsg;
		}		
		BuyAllParams buyAllParams = BuyAllUtils.getBuyAllParams(
				entity.getGoodsTimesId(), spyMemcachedClient);
		if(buyAllParams == null){
			//Byte state = service.getState(entity.getGoodsTimesId());
			GoodsTimes goodsTimes = service.get(entity.getGoodsTimesId());
			if(goodsTimes.getTotalBuyTimes() == 0) {
				return "5"; // 虚拟用户买满提示操作错误
			}
			if(goodsTimes.getState() != Const.GoodsTimesState.GOING) {
				return "3"; // 非进行状态不能买满
			}

			List<User> virtualUsers = accountService.findRandomVirtualUser(entity.getUserAmount());
			int[] buyTimesArr = null;
			try {
				buyTimesArr = RandomUitls.generateNums(goodsTimes.getIsTenYuan(),
						goodsTimes.getTotalTimes() - goodsTimes.getTotalBuyTimes(), virtualUsers.size());
			}catch (BizException ex){
				return "4";
			}

			ThreadWorkUtils.buyGoodsTimesFull(entity, virtualUsers, buyTimesArr);
			BuyAllUtils.saveBuyAllParamsInCache(entity, spyMemcachedClient);
			return "1";
		}else{
			return "2"; // 已设置，不需要重复设置
		}
	}

	/**
	 * 查看、编辑中奖信息
	 */
	@RequiresPermissions(value = {"goods:winner:view", "goods:winner:dispatch"}, logical = Logical.OR)
	@RequestMapping(value = "editWinng/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		GoodsTimes entity = service.get(id);
//		if(entity.getWinngState() > Const.WinngState.CONFIRM_ADDRESS){
//			WinngUserReceiveInfo winngUserReceiveInfo = winngUserReceiveInfoService.getByGoodsTimes(id);
//			model.addAttribute("receiveAddr", winngUserReceiveInfo.getAddress())
//					.addAttribute("receiver", winngUserReceiveInfo.getReceiver())
//					.addAttribute("mobileNo", winngUserReceiveInfo.getMobileNo())
//					.addAttribute("logisticsName", winngUserReceiveInfo.getLogisticsName())
//					.addAttribute("logisticsNo", winngUserReceiveInfo.getLogisticsNo());
//		}
			
		User tmp = accountService.findUserByUserName(entity.getWinngUserIdentity());
		if (tmp != null) {
			ReceiveAddress addr = receiveAddressDao.findByState(tmp.getId());
			if (addr != null) {
				model.addAttribute("receiver", addr.getReceiver());
				model.addAttribute("mobileNo", addr.getTel());
				model.addAttribute("receiveAddr", 
						addr.getProvince()
						+ addr.getCity()
						+ addr.getDistrict()
						+ addr.getDetailAddress());
			}
		}

		model.addAttribute("entity", entity)
				.addAttribute("canDispatchGoods", UserUtils.hasPermission("goods:winner:dispatch") &&
						entity.getWinngState() == Const.WinngState.WAITING_DISPATCH);
		bindData(model);
		return VIEW_URI_PREFIX + "form-winng";
	}

	/**
	 * 发货
	 * @param paramsEntity
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("goods:winner:dispatch")
	@RequestMapping(value = "saveWinng", method = RequestMethod.POST)
	public String saveWinng(ParamsEntity paramsEntity, RedirectAttributes redirectAttributes) {
		/*
		winngGoodsStateService.dispatchGoods(
				SimpleUtils.stringToInteger(paramsEntity.get("gid")),
				paramsEntity.get("logisticsName"), paramsEntity.get("logisticsNo")
		);
		*/
		Integer gid = SimpleUtils.stringToInteger(paramsEntity.get("gid"));
		String logistics = paramsEntity.get("logisticsInfo");
		service.updateGoodsLogistics(gid, (int)Const.exchangeState.DELIVER_GOODS, logistics);
		
		Integer userId = Integer.parseInt(paramsEntity.get("userId"));
		sendMessageByAdmin(userId, logistics);
		redirectAttributes.addFlashAttribute("message", "保存物流信息成功");
		
		// 后台发货更新order_winning_state=5
		winngGoodsStateService.finishFahuo(userId, gid);
		return getRedirectUrl(paramsEntity.get("listUrl"));
	}
	
	public void sendMessageByAdmin(Integer userId, String logistics) {
		User user = UserUtils.getCurrentUser();
		Notice entity = new Notice();
		User userTmp =new User();
		userTmp.setId(userId);
		
		entity.setId(null);
		entity.setUser(userTmp);
		entity.setSender(user);
		entity.setSenderName(user.getName());
		entity.setState(Const.CommonState.ENABLE);
		entity.setTitle("您的商品已发货！");
		entity.setContent("您的商品已发货，具体物流信息：" + logistics);
		noticeService.save(entity);
		NoticeSendRecord noticeSendRecord=new NoticeSendRecord();
		noticeSendRecord.setNotice(entity);

		noticeSendRecord.setToUser(userTmp);
		noticeService.saveNoticeSendRecord(noticeSendRecord);
		List<String> alias=new ArrayList<String>(1);
		alias.add(userId.toString());
		Map<String, String> extras=new HashMap<String,String>(2);
		extras.put("title", "");
		extras.put("content", "");
		JPushUtils.sendPushNotificationNotice(alias, extras);
	}
	
	
	private void bindData(Model model){
		model.addAttribute("goodsTimesStateMap", Const.GoodsTimesState.MAP)
				.addAttribute("goodsTimesStateOver", Const.GoodsTimesState.OVER)
				.addAttribute("winngStateMap", Const.WinngState.MAP)
				.addAttribute("stateMap", Const.commonStateMap)
				.addAttribute("exchangeStateMap", Const.exchangeState.MAP)
				.addAttribute("booleanMap", Const.booleanMap)
				.addAttribute("expressList", ExpressConst.expressList)
				.addAttribute("goodsCategoryList", goodsCategoryService.findAllWithIdAndName());
	}
	
	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadEntity")
	public GoodsTimes getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return service.get(id);
		}
		return new GoodsTimes();
	}

	/**
	 * 获取商品的夺宝记录
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getSnatchRecord/{id}", method = RequestMethod.GET)
	public Map<String, Object> getSnatchRecord(@PathVariable("id")Integer id){
		Map<String, Object> map = new HashMap<>(1);
		List<SnatchRecord4GoodsTimesVo> snatchRecords = snatchRecordService.findByGoodsTimes(id);
		map.put("rows", snatchRecords);
		return map;
	}

	/**
	 * 预设中奖用户
	 * @param userId
	 * @param goodsTimesId
	 * @return
	 */
	@RequiresPermissions(PERMISSION_PREFIX + ":settingDraw")
	@ResponseBody
	@RequestMapping(value = "setWinner", method = RequestMethod.POST)
	public String setWinner(@RequestParam("userId")Integer userId,
							@RequestParam("goodsTimesId")Integer goodsTimesId){
		Byte state = service.getState(goodsTimesId);
		if(state == Const.GoodsTimesState.OVER){
			return "2"; // 不能设置已揭晓的期号
		}

		String result = null;
		User user = accountService.getUser(userId);
		while(true){
			try{
				GoodsTimes goodsTimes = service.get(goodsTimesId);
				goodsTimes.setPresetWinnerUserId(user.getId());
				goodsTimes.setPresetWinnerUserName(user.getUserName());
				service.save(goodsTimes);

				result = "1";
				break;
			} catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
				logger.info("预设中奖用户 - 出现并发，期号：{}", goodsTimesId);
				Threads.sleep(RandomUitls.randomInt(100));
			} catch (CannotAcquireLockException | LockTimeoutException ex) {
				logger.info("预设中奖用户 - 出现死锁，等待继续执行，期号：{}", goodsTimesId);
				Threads.sleep(RandomUitls.randomInt(300));
			} catch (Exception ex) {
				logger.error("预设中奖用户 - 夺宝失败，期号：{}", goodsTimesId);
				logger.error(ex.getMessage(), ex);
				result = "0";
				break;
			}
		}
		return result;
	}

	/**
	 * 手动开奖
	 * @param goodsTimesId
	 * @return
	 */
	@RequiresPermissions(PERMISSION_PREFIX + ":open")
	@ResponseBody
	@RequestMapping(value = "produceLuckNum", method = RequestMethod.POST)
	public int produceLuckNum(@RequestParam("goodsTimesId")Integer goodsTimesId){
		int result = 1;
		while(true){
			try{
				snatchRecordService.produceLuckNum(goodsTimesId);
				break;
			} catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
				logger.info("手动开奖出现并发，商品期号：{}", goodsTimesId);
				Threads.sleep(RandomUitls.randomInt(100));
			} catch(CannotAcquireLockException | LockTimeoutException ex){
				logger.info("手动开奖出现死锁，等待继续执行，商品期号：{}", goodsTimesId);
				Threads.sleep(RandomUitls.randomInt(300));
			} catch (Exception ex) {
				logger.error("手动开奖出错，商品期号：" + goodsTimesId, ex);
				result = 0;
				break;
			}
		}

		return result;
	}
}
