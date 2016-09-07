package com.zx.stlife.controller.wx.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.modules.util.ConvertUtils;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.WinngGoodsState;
import com.zx.stlife.entity.order.WinngUserReceiveInfo;
import com.zx.stlife.entity.sys.ReceiveAddress;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.order.WinngGoodsStateService;
import com.zx.stlife.service.order.WinngUserReceiveInfoService;
import com.zx.stlife.service.sys.ReceiveAddressService;

@Controller
@RequestMapping(value="wx/winng")
public class WinngUserWxController extends BaseController{

	@Autowired
	private GoodsTimesService goodsTimesService;
	@Autowired
	private WinngGoodsStateService winngGoodsStateService;
	@Autowired
	private WinngUserReceiveInfoService winngUserReceiveInfoService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ReceiveAddressService addressService;
	/**
	 * 查看中奖记录明细
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String winngUserDetail(@RequestParam(value="goodsTimesId",required=false) Integer goodsTimesId,
			Model model,HttpServletRequest request){
		HttpSession session = request.getSession();
		if(goodsTimesId==null){
			goodsTimesId = (Integer)session.getAttribute("goodsTimesId");
		}
		GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
		model.addAttribute("goodsTimes", goodsTimes);

		List<WinngGoodsState> winngGoodsStateList = winngGoodsStateService
				.findByGoodsTimes(goodsTimesId);
		model.addAttribute("goodsStates", winngGoodsStateList)
				.addAttribute("winngGoodsStateMap", Const.WxWinngGoodsState.MAP);
		
		WinngGoodsState currWinngGoodsState = winngGoodsStateService
				.getCurrent(goodsTimesId);
		Map<String, Object> receiveAddressMap = null;
		WinngUserReceiveInfo winngUserReceiveInfo = null;
		Byte currentState = currWinngGoodsState.getState();
		if (currentState > Const.WinngGoodsState.CONFIRM_ADDRESS
				|| (currWinngGoodsState.getIsFinish() && currentState == Const.WinngGoodsState.CONFIRM_ADDRESS)) {
			winngUserReceiveInfo = winngUserReceiveInfoService
					.getByGoodsTimes(goodsTimesId);
			receiveAddressMap = ConvertUtils
					.convertEntityToMap(winngUserReceiveInfo, "receiver",
							"mobileNo", "address");
		}
		model.addAttribute("receiveAddress", receiveAddressMap);
		
		Map<String, Object> logisticsMap = null;
		if (currentState > Const.WinngGoodsState.DISPATCH
				|| (currWinngGoodsState.getIsFinish() && currentState == Const.WinngGoodsState.DISPATCH)) {
			logisticsMap = ConvertUtils.convertEntityToMap(
					winngUserReceiveInfo, "logisticsName", "logisticsNo");
		}
		model.addAttribute("logistics", logisticsMap);
		session.setAttribute("goodsTimesId", goodsTimes.getId());
		
		model.addAttribute("curWinState", currWinngGoodsState.getState());
		
		return "wx/order/win-detail";
	}
	
	/**
	 * 5.23. 确认收货
	 * @param goodsTimesId
	 * @return
	 */
	@RequestMapping(value = "/confirmReceived", method = RequestMethod.GET)
	public String confirmReceived(@RequestParam("goodsTimesId") Integer goodsTimesId)
	{
		Integer userId = UserWxUtils.getCurrUserId();
		winngGoodsStateService.confirmReceived(userId, goodsTimesId);
		return getRedirectUrl("/wx/winng/detail?goodsTimesId=" + goodsTimesId);
	}
	
	/**
	 * 5.22. 确认收货地址
	 * @param raid 收货地址id
	 * @return
	 */
	@RequestMapping(value = "/confirmReceiveAddr", method = RequestMethod.GET)
	public String confirmReceiveAddr(@RequestParam("raid") Integer raid,HttpServletRequest request){
		HttpSession session = request.getSession();
		Integer goodsTimesId = (Integer)session.getAttribute("goodsTimesId");
		Integer userId = UserWxUtils.getCurrUserId();
		winngGoodsStateService.confirmReceiveAddr(userId, goodsTimesId, raid);
		
		// 中奖记录里面确认收货地址后，把选择的地址设置成默认地址
		List<ReceiveAddress> addresses = addressService.findByUserId(userId);
		List<Integer> ids = new ArrayList<Integer>();
		for (ReceiveAddress address : addresses) {
			ids.add(address.getId());
		}
		addressService.updateSetDefault(ids, raid);
		
		return getRedirectUrl("/wx/winng/detail?goodsTimesId=" + goodsTimesId);
	}
	
    /**
     * 按商品GoodsTimesId进行兑奖
     */
    @RequestMapping(value = "/setExchangeState", method = RequestMethod.POST)
    @ResponseBody
    public String setExchangeState(@RequestParam("gid") Integer goodsTimesId,
    		@RequestParam("state") Integer exchangeState){
        GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
        if(goodsTimes == null || goodsTimes.getState() < 1){
        	return "0";
        }
		Integer userId = UserWxUtils.getCurrUserId();
        try {
            goodsTimes.setExchangeState(exchangeState);
            goodsTimesService.updateGoodsState(goodsTimesId, exchangeState);
            // exchangeState=4 已兑换积分
            if (1 == exchangeState.intValue()) {
            	memberService.recordJifen(userId, 
                		goodsTimes.getGoodsInfo().getChangeJifen(), "中奖兑换积分");
            	// winngGoodsState=5
        		winngGoodsStateService.confirmChangeType(userId, goodsTimesId);
            }

            // exchangeState=2 点击领取奖品
            if (2 == exchangeState.intValue()) {
            	// winngGoodsState=3 确认收货地址
        		winngGoodsStateService.confirmAddr(userId, goodsTimesId);
            }
            
            // exchangeState=3 确认完收货地址后
            if (3 == exchangeState.intValue()) {
            	// winngGoodsState=4 确认收货地址
        		winngGoodsStateService.confirmSend(userId, goodsTimesId);
            }
            return "1";
        } catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return "0";
		}
    }
    
}
