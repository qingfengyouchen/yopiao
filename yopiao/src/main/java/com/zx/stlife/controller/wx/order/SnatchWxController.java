package com.zx.stlife.controller.wx.order;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.Const.GoodsTimesState;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.PayRecord;
import com.zx.stlife.entity.order.SnatchListItem;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.entity.record.RedPack;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.order.SnatchListItemService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.service.record.RedPackService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.vo.snatch.SnatchListVo;
import com.zx.stlife.vo.snatch.SnatchRecordVo;
import com.zx.stlife.vo.snatch.SnatchRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.zx.stlife.controller.app.base.JsonResultUtils.buildFailureResult;

@Controller
@RequestMapping(value = "/wx/order/snatchPay")
public class SnatchWxController extends BaseController {

	@Autowired
	private SnatchListItemService snatchListItemService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private RedPackService redPackService;

	@Autowired
	private SnatchRecordService snatchRecordService;

	/**
	 * 下单页面
	 */
	@RequestMapping(value = "/prepay")
	public String getSnatch(@RequestParam("goodsTimesIds")List<Integer> goodsTimesIds, Model model) {
		User user = UserWxUtils.getCurrUser();

		List<SnatchListVo> snatchListList =
				snatchListItemService.findSnatchListVoByUserAndGoodsTimes(user.getId(), goodsTimesIds);

		int totalBuyTimes = 0;
		if(SimpleUtils.isNotNullList(snatchListList)) {
			Iterator<SnatchListVo> iterator = snatchListList.iterator();
			while (iterator.hasNext()) {
				SnatchListVo snatchListVo = iterator.next();
				if(snatchListVo.getState() != Const.GoodsTimesState.GOING){
					snatchListItemService.deleteEntity(snatchListVo.getSnatchListItemId());
					iterator.remove();
					continue;
				}

				totalBuyTimes += snatchListVo.getBuyTimes();
			}

			Integer balance = memberService.getBalanceByUser(user.getId());
			if (balance == null)
				balance = 0;

			model.addAttribute("balance", balance);

			// 现在就一种红包（后台admin发 RedPackCategory=3）
			List<RedPack> redPackList = redPackService.findCanUseByUser(
					user.getId(), Const.RedPackCategory.ADMIN);
			model.addAttribute("userId", user.getId())
					.addAttribute("redPackList", redPackList)
					.addAttribute("redPackSourceTypeMap", Const.RedPackSourceType.map);
		}

		model.addAttribute("totalBuyTimes", totalBuyTimes)
				.addAttribute("snatchListList", snatchListList);

		return "wx/order/snatch/prepay";
	}


	@RequestMapping(value="/pay", method=RequestMethod.POST)
	@ResponseBody
	public JsonResult prepay(SnatchRequestVo snatchRequestVo, HttpServletRequest request)
	{
		User user = UserWxUtils.getCurrUser();
		snatchRequestVo.setUserId(user.getId());
		return BizCommon.pay(user, snatchRequestVo, snatchListItemService, snatchRecordService, request, true);
	}

	@RequestMapping(value="/cancel",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult cancelPay(@RequestParam("outTradeNo") String outTradeNo) {
		Integer userId = UserWxUtils.getCurrUserId();
		return BizCommon.cancelPay(snatchRecordService, userId, outTradeNo);
	}

	@RequestMapping(value="/getPayResult",method=RequestMethod.GET)
	public String getPayResult(@RequestParam("outTradeNo") String outTradeNo, Model model){
		Integer userId = UserWxUtils.getCurrUserId();
		JsonResult resultMap = snatchRecordService.getPayResult(userId, outTradeNo);
		model.addAttribute("userId", userId)
				.addAttribute("resultMap", resultMap);
		return "wx/order/snatch/pay-result";
	}
}
