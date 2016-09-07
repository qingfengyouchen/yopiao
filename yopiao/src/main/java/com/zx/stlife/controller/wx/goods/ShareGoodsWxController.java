package com.zx.stlife.controller.wx.goods;

import com.base.jpa.query.Page;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.ConstWeixinH5;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.goods.ShareGoods;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.TmpFileService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.goods.ShareGoodsService;
import com.zx.stlife.service.goods.ShareImageService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.RandomUitls;
import com.zx.stlife.tools.ajax.AjaxFormResult;
import com.zx.stlife.tools.validator.ShareGoodsValidator;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.LockTimeoutException;
import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/wx/goods/shareGoods")
public class ShareGoodsWxController extends BaseController {

	@Autowired
	private ShareGoodsService shareGoodsService;
	@Autowired
	private ShareImageService shareImageService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private GoodsTimesService goodsTimesService;
	@Autowired
	private TmpFileService tmpFileService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ShareGoodsValidator shareGoodsValidator;

	private static String REDIRCT_TO_DETAIL = "redirect:/wx/goods/shareGoods/shareGoodsDetails?shareGoodsId=";
	/**
	 * 6.5.首页晒单/用户晒单列表
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model, Page<ShareGoods> page) {
		page.setPageSize(10);
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		shareGoodsService.searchAll(page);
		Map<Integer, List<String>> dataMap = new HashMap<Integer, List<String>>();
		for (ShareGoods shareGoods : page.getResult()) {
			List<String> urlList = shareImageService.findShareImage(shareGoods
					.getId());
			dataMap.put(shareGoods.getId(), urlList);
		}
		model.addAttribute("urlMap", dataMap);
		model.addAttribute("page", page).addAttribute("timestamp", timestamp);
		return "wx/shareGoods/share-goods-list";
	}

	@RequestMapping("/allShareGoodsByJson")
	@ResponseBody
	public JsonResult allShareGoodsByJson(Page<ShareGoods> page,
			@RequestParam("timestamp") String timestamp) {
		page.setAutoCount(false);
		return BizCommon.getShareGoodsAttr(page, timestamp, null,
				shareGoodsService);
	}

	/**
	 * 6.2.查看晒单详情
	 * @param shareGoodsId
	 * @param model
	 * @return
	 */
	@RequestMapping("/shareGoodsDetails")
	public String shareGoodsDetails(
			@RequestParam("shareGoodsId") Integer shareGoodsId, Model model) {
		model.addAttribute("entity", shareGoodsService.detail(shareGoodsId));
		List<String> urlList = shareImageService
				.findAllShareImage(shareGoodsId);
		model.addAttribute("shareImageList", urlList);
		return "wx/shareGoods/share-goods-details";
	}

	/**
	 * 6.1.查看晒单分享列表
	 * @param goodsId
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/{goodsId}/list")
	public String list(@PathVariable("goodsId") Integer goodsId,
			Page<ShareGoods> page, Model model) {
		page.setPageSize(10);
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		shareGoodsService.pageSearch(page, goodsId);
		Map<Integer, List<String>> dataMap = new HashMap<Integer, List<String>>();
		for (ShareGoods shareGoods : page.getResult()) {
			List<String> urlList = shareImageService.findShareImage(shareGoods
					.getId());
			dataMap.put(shareGoods.getId(), urlList);
		}
		model.addAttribute("urlMap", dataMap);
		model.addAttribute("page", page).addAttribute("timestamp", timestamp);
		return "wx/shareGoods/share-goods-list";
	}

	@RequestMapping("/shareGoodsByGoods")
	@ResponseBody
	public JsonResult shareGoodsByGoods(Page<ShareGoods> page,
			@RequestParam("timestamp") String timestamp) {
		page.setAutoCount(false);
		return BizCommon.getShareGoodsAttr(page, timestamp, null,
				shareGoodsService);
	}

	

	/**
	 * 新增或修改晒单
	 * @param model
	 * @param goodsTimesId
	 * @return
	 */
	@RequestMapping(value="/create" , method = RequestMethod.GET)
	public String edit(Model model,
			@RequestParam("goodsTimesId")Integer goodsTimesId,@RequestParam(value="id", required=false)Integer id){
		model.addAttribute("timestamp", DateUtils.dateToYYYYMMDDHHMMSSSSSString(new Date()));
		ShareGoods entity = shareGoodsService.getShareGoodsByGoodsTimes(goodsTimesId);
		Integer userId = UserWxUtils.getCurrUserId();
		if(entity == null){
			entity = new ShareGoods();
		}else{
			if(entity.getState()== Const.ShareGoodsState.AUDIT_PASS){
				return REDIRCT_TO_DETAIL + goodsTimesId;
			}else if(userId != entity.getUser().getId()){
				logger.error("不能编辑非本人的晒单");
				return ConstWeixinH5.REDIRCT_TO_INDEX;
			}
			model.addAttribute("shareImages", shareGoodsService.findByShareGoods(entity.getId()));
		}
		model.addAttribute("entity", entity)
				.addAttribute("goodsTimes", goodsTimesService.get(goodsTimesId));
		return "wx/shareGoods/share-goods-form";
	}

	/**
	 * 晒单分享
	 * @param entity
	 * @param goodsTimesId
	 * @param fileIds
	 * @param result
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public AjaxFormResult save(@Valid @ModelAttribute("preloadEntity") ShareGoods entity,
					   @RequestParam("goodsTimesId") Integer goodsTimesId,
					   @RequestParam("fileIds") List<Integer> fileIds,
					   BindingResult result){
		User user = UserWxUtils.getCurrUser();
		try {

			if(!goodsTimesService.isWinner(goodsTimesId, user.getId())){
				return AjaxFormResult.getFailResult(308); // 只能发布本人的晒单
			}

			if (!entity.hasId() &&
					shareGoodsService.checkGoodsTimes(goodsTimesId)) {
				return AjaxFormResult.getFailResult(307);
			}

			shareGoodsValidator.validate(entity, result);// 添加验证
			if (result.hasErrors()) {
				int errorCode = Integer.valueOf(result.getFieldError()
						.getCode());
				return AjaxFormResult.getFailResult(errorCode);
			} else {
				if (SimpleUtils.isNullList(fileIds)) {
					return AjaxFormResult.getFailResult(305);
				} else {
					if (fileIds.size() > 8) {
						return AjaxFormResult.getFailResult(306);
					}
				}
			}

			boolean isEdit = entity.hasId();
			boolean isSuccess = false;
			while (true) {
				try {
					if(isEdit ||
							!shareGoodsService.checkGoodsTimes(goodsTimesId)){

						shareGoodsService.saveShareGoods(user, goodsTimesId, entity);
						isSuccess = true;
					}
					break;
				} catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
					logger.info("晒单出现并发访问...");
					Threads.sleep(RandomUitls.randomInt(100));
				} catch (CannotAcquireLockException | LockTimeoutException ex) {
					logger.info("晒单出现死锁，等待继续执行");
					Threads.sleep(RandomUitls.randomInt(300));
				} catch (Exception ex) {
					logger.error(ex.getClass().getName());
					logger.error("晒单出现系统错误：" + ex.getMessage(), ex);
					return AjaxFormResult.getFailResult(0);
				}
			}

			if(isSuccess){
				shareGoodsService.buildImgs(entity, isEdit, fileIds);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return AjaxFormResult.getFailResult(0);
		}

		return AjaxFormResult.getSuccessResult(1);
	}
	
	@RequestMapping(value =  "/deleteImg", method = RequestMethod.POST)
	@ResponseBody
	public Integer deleteImg(@RequestParam("id") Integer id,
						  @RequestParam("isTempFile") String isTempFile){
		Integer result = null;
		try {
			if ("true".equals(isTempFile)) {
				tmpFileService.deleteById(id, true);
			} else {
				shareGoodsService.deleteShareImg(id);
			}
			return 1;
		}catch (Exception ex){
			result = 0;
			logger.error(ex.getMessage(), ex);
		}

		return result;
	}
	
	/**
	 * 查询个人晒单列表
	 */
	@RequestMapping(value="/my")
	public String my(Model model,Page<GoodsTimes> page){
		page.setPageSize(10);
		Integer userId = UserWxUtils.getCurrUserId();
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		goodsTimesService.searchGoodsTimeList(page, date, userId);
		List<Map<String, Object>> shareGoodsList = shareGoodsService.bindDateByMy(page);

		model.addAttribute("timestamp", timestamp)
				.addAttribute("userId", userId)
				.addAttribute("shareGoodsList", shareGoodsList)
				.addAttribute("shareStateMap", Const.ShareGoodsState.MAP);
		return "wx/shareGoods/my-share-goods";
	}
	
	/**
	 * 查询个人晒单列表（分页）
	 */
	@RequestMapping("/myByJson")
	@ResponseBody
	public JsonResult myByJson(@RequestParam("userId") Integer userId,
			@RequestParam("timestamp") String timestamp, Page<GoodsTimes> page){
		page.setAutoCount(false);
		return BizCommon.myShareGoodsByJson(page, timestamp, userId, shareGoodsService, goodsTimesService);
	}
	
	/**
	 * 查询他人晒单列表
	 */
	@RequestMapping("/listByPersonal")
	public String listByPersonal(Model model, Page<ShareGoods> page,
			@RequestParam("userId") Integer userId) {
		page.setPageSize(10);
		Date date =new Date();
		User user = accountService.getUser(userId);
		shareGoodsService.searchAll(date, page, user.getId());
		Map<Integer, List<String>> dataMap = new HashMap<Integer, List<String>>();
		for (ShareGoods shareGoods : page.getResult()) {
			List<String> urlList = shareImageService.findShareImage(shareGoods
					.getId());
			dataMap.put(shareGoods.getId(), urlList);
		}
		model.addAttribute("urlMap", dataMap)
				.addAttribute("page", page)
				.addAttribute("timestamp", DateUtils.dateToYYYYMMDDHHMMSSSSSString(date))
				.addAttribute("user", user)
				.addAttribute("headImg", memberService.getHeadImgByUser(user.getId()));
		return "wx/shareGoods/share-goods-personal";
	}
	
	/**
	 * 查询他人晒单列表（分页）
	 */
	@RequestMapping("/otherAllShareGoodsByJson")
	@ResponseBody
	public JsonResult otherAllShareGoodsByJson(Page<ShareGoods> page,
			@RequestParam("timestamp") String timestamp) {
		page.setAutoCount(false);
		Integer userId = UserWxUtils.getCurrUserId();
		return BizCommon.getShareGoodsAttr(page, timestamp, userId,
				shareGoodsService);
	}
}
