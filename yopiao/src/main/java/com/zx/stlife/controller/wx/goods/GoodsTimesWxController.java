package com.zx.stlife.controller.wx.goods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zx.stlife.base.UserWxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.base.jpa.query.Page;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.app.base.JsonResultUtils;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsImage;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.SnatchListItem;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.SnatchListItemService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordDetailService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.sys.ImageSettingService;
import com.zx.stlife.tools.DateUtils;

@Controller
@RequestMapping(value = "/wx/goods/goodsTimes")
public class GoodsTimesWxController extends BaseController<GoodsTimes>{
	
	@Autowired
	private GoodsTimesService goodsTimesService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SnatchNumService snatchNumService;
    @Autowired
    private SnatchRecordService snatchRecordService;
    @Autowired
	private SnatchRecordDetailService snatchRecordDetailService;
	@Autowired
    private SnatchListItemService snatchListItemService;
    
    private static final String VIEW_URI_PREFIX = "wx/goodsTimes/goods-times-";
    
	@RequestMapping("all")
	public String all(Model model,Page<GoodsTimes> page){
		page.setPageSize(10);
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		goodsTimesService.findGoodsTimesByAttr(page, date, null, null);
		Integer listCount=0;
		Integer userId = UserWxUtils.getCurrUserId();
		if(userId!=null){
			listCount= snatchListItemService.getUserListCount(userId); //获取清单总数（购物车）
		}
		model.addAttribute("page", page)
				.addAttribute("userId", userId)
				.addAttribute("timestamp", timestamp).addAttribute("listCount", listCount);
		return VIEW_URI_PREFIX+"all";
	}

	@RequestMapping("allByJson")
	@ResponseBody
	public JsonResult allGoodsByJson(Page<GoodsTimes> page,
									 @RequestParam("timestamp") String timestamp){
		page.setAutoCount(false);
		return BizCommon.getGoodsTimesByAttr(
				page, timestamp, null, null, goodsTimesService);
	}
	
	/**
	 * 商品详情
	 * @param goodsTimesId 商品id
	 * @return
	 */
	@RequestMapping(value = "detail/{id}",method=RequestMethod.GET)
	public String detail(Model model,
						 @PathVariable("id") Integer goodsTimesId,Page<SnatchRecordDetail> page){
		GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId); //
		List<GoodsImage> goodsImages = goodsService.findByGoodsAndCategory(goodsTimes.getGoodsInfo().getId(), Const.GoodsImageCategory.TOP_SWITCH);
		Integer userBuyTimes=0;
        List<Integer> userNums = null;
        Integer listCount=0; //清单总数
        boolean buyListBoolean=false; //是否以添加
		Integer userId = UserWxUtils.getCurrUserId();
        if (userId != null) {
            userBuyTimes = snatchRecordService.getBuyTimesByGoodsTimesAndUser(goodsTimes.getId(), userId);
			if(userBuyTimes == null){
				userBuyTimes = 0;
			}else {
				userNums = snatchNumService.findTopNumsByGoodsTimesAndUserBuyTimes(
						userBuyTimes, goodsTimes.getId(), userId);
			}
            listCount= snatchListItemService.getUserListCount(userId); //获取清单总数（购物车）
            SnatchListItem snatchListItem= snatchListItemService.findBySnatchListItem(userId, goodsTimesId);
            if(snatchListItem!=null)
            	buyListBoolean=true;

			model.addAttribute("userId", userId);
        }
        userBuyTimes=userBuyTimes!=null?userBuyTimes:0;
		model.addAttribute("goodsTimes", goodsTimes);
		model.addAttribute("goodsImages", goodsImages); //顶部图片
		model.addAttribute("userNums", userNums); //中奖号码 查询6条
		model.addAttribute("userBuyTimes", userBuyTimes)
				.addAttribute("listCount", listCount)
				.addAttribute("buyListBoolean", buyListBoolean); //购买次数
        Boolean hasLastGoodsTimes = false;
        if (goodsTimes.getState() != Const.GoodsTimesState.GOING) {// 非进行中状态才有去判断有没有最新一期
            hasLastGoodsTimes = goodsTimesService.hasLast(goodsTimes.getGoodsInfo().getId());
        }
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		JsonResult jsonResult= BizCommon.findSnatchRecordDetailByGoodsTimes(
				snatchRecordDetailService, page, timestamp, goodsTimesId);
		model.addAttribute("timestamp", timestamp)
				.addAttribute("jsonResult", jsonResult)
			.addAttribute("hasLastGoodsTimes", hasLastGoodsTimes); //查看商品详情中夺宝记录列表
		return VIEW_URI_PREFIX+"detail";
	}
	
	/**
	 * 商品详情
	 * @param goodsTimesId 商品id
	 * @return
	 */
	@RequestMapping(value = "detailbytimes/{id}",method=RequestMethod.GET)
	public String detailByTimeid(Model model,
						 @PathVariable("id") Integer goodsTimesId,Page<SnatchRecordDetail> page){
		GoodsTimes tmp = goodsTimesService.getByTimes(goodsTimesId);
		GoodsTimes goodsTimes = goodsTimesService.getLastByGoods(tmp.getGoodsInfo().getId());
		List<GoodsImage> goodsImages = goodsService.findByGoodsAndCategory(goodsTimes.getGoodsInfo().getId(), Const.GoodsImageCategory.TOP_SWITCH);
		Integer userBuyTimes=0;
        List<Integer> userNums = null;
        Integer listCount=0; //清单总数
        boolean buyListBoolean=false; //是否以添加
		Integer userId = UserWxUtils.getCurrUserId();
        if (userId != null) {
            userBuyTimes = snatchRecordService.getBuyTimesByGoodsTimesAndUser(goodsTimes.getId(), userId);
			if(userBuyTimes == null){
				userBuyTimes = 0;
			}else {
				userNums = snatchNumService.findTopNumsByGoodsTimesAndUserBuyTimes(
						userBuyTimes, goodsTimes.getId(), userId);
			}
            listCount= snatchListItemService.getUserListCount(userId); //获取清单总数（购物车）
            SnatchListItem snatchListItem= snatchListItemService.findBySnatchListItem(userId, goodsTimesId);
            if(snatchListItem!=null)
            	buyListBoolean=true;

			model.addAttribute("userId", userId);
        }
        userBuyTimes=userBuyTimes!=null?userBuyTimes:0;
		model.addAttribute("goodsTimes", goodsTimes);
		model.addAttribute("goodsImages", goodsImages); //顶部图片
		model.addAttribute("userNums", userNums); //中奖号码 查询6条
		model.addAttribute("userBuyTimes", userBuyTimes)
				.addAttribute("listCount", listCount)
				.addAttribute("buyListBoolean", buyListBoolean); //购买次数
        Boolean hasLastGoodsTimes = false;
        if (goodsTimes.getState() != Const.GoodsTimesState.GOING) {// 非进行中状态才有去判断有没有最新一期
            hasLastGoodsTimes = goodsTimesService.hasLast(goodsTimes.getGoodsInfo().getId());
        }
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		JsonResult jsonResult= BizCommon.findSnatchRecordDetailByGoodsTimes(
				snatchRecordDetailService, page, timestamp, goodsTimesId);
		model.addAttribute("timestamp", timestamp)
				.addAttribute("jsonResult", jsonResult)
			.addAttribute("hasLastGoodsTimes", hasLastGoodsTimes); //查看商品详情中夺宝记录列表
		return VIEW_URI_PREFIX+"detail";
	}
	
	/**
	 * 获取最新一期商品详情
	 * @param goodsId 商品id
	 */
	@RequestMapping(value="detail/getLast/{id}",method=RequestMethod.GET)
	public String getLast(@PathVariable("id")Integer goodsId, RedirectAttributes attr){
		Integer userId = UserWxUtils.getCurrUserId();
		attr.addAttribute("userId", userId);
		GoodsTimes goodsTimes = goodsTimesService.getLastByGoods(goodsId);
		return "redirect:/wx/goods/goodsTimes/detail/"+goodsTimes.getId();
	}
	
	/**
	 * 5.7. 查看往期揭晓
	 */
	@RequestMapping(value="record/list/{goodsId}",method=RequestMethod.GET)
	public String allGoodsTimes(@PathVariable("goodsId") Integer goodsId,
								Page<GoodsTimes> page,Model model) {
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		JsonResult jsonResult= BizCommon.findGoodsTimesRecords(goodsId, timestamp, page, goodsTimesService);
		model.addAttribute("jsonResult", jsonResult).addAttribute("timestamp", timestamp).addAttribute("goodsId", goodsId);
		return VIEW_URI_PREFIX+"record-list";
	}
	
	@RequestMapping(value="record/listByJson/{goodsId}",method=RequestMethod.GET)
	public @ResponseBody JsonResult allGoodsTimesByJson(@PathVariable("goodsId") Integer goodsId,
														@RequestParam("timestamp")String timestamp,
														Page<GoodsTimes> page){
		page.setAutoCount(false);
		return BizCommon.findGoodsTimesRecords(goodsId, timestamp, page, goodsTimesService);
	}
	
	/**
	 * 获取最新揭晓列表
	 */
	@RequestMapping(value = "/listLastOpen", method = RequestMethod.GET)
	public String listLastOpen(Model model,Page<GoodsTimes> page) {
		Date date = DateUtils.getNow();
		String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
		String now = sdf.format(date);
		page.setPageSize(10);
		goodsTimesService.findLastOpen(page, date);
		model.addAttribute("page", page);
		model.addAttribute("timestamp", timestamp);
		model.addAttribute("date", date);
		model.addAttribute("now", now);
		return VIEW_URI_PREFIX+"list-last-open";
	}
	
	/**
	 * 
	 */
	@RequestMapping(value = "lastOpen/detail")
	@ResponseBody
    public JsonResult getDetail(@RequestParam("id") Integer goodsTimesId) {
        try {
            GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("id", goodsTimesId);
			data.put("state", goodsTimes.getState());
			data.put("canGetCqsscno", goodsTimes.getCanGetCqsscno());
			data.put("winngUserId", goodsTimes.getWinngUserId());
			data.put("winngUserName", goodsTimes.getWinngUserName());
			data.put("winngUserBuyTimes", goodsTimes.getWinngUserBuyTimes());
			data.put("luckNum", goodsTimes.getLuckNum());
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			String time = sdf.format(goodsTimes.getOpenTime());
			data.put("openTime", time);
			return JsonResultUtils.buildSuccessResult(data);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return JsonResultUtils.buildFailureResult();
        }
    }
	
	/**
	 * 查询他人夺宝记录（分页）
	 */
	@RequestMapping("/lastOpenByJson")
	@ResponseBody
	public JsonResult allLastOpenByJson(Page<GoodsTimes> page,
			@RequestParam("timestamp") String timestamp) {
		page.setAutoCount(false);
		JsonResult result = BizCommon.getListLastOpenByAttr(page,timestamp,
				goodsTimesService);
		return result;
	}
}
