package com.zx.stlife.controller.wx.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.service.order.SnatchRecordDetailService;

/**
 * 微信端 夺宝记录列表
 */
@RestController
@RequestMapping("/wx/snatch/snatchRecordDetail")
public class SnatchRecordDetailWxController extends BaseController<SnatchRecordDetail> {
	@Autowired
	private SnatchRecordDetailService snatchRecordDetailService;

	/**
	 * 5.6. 查看商品详情中夺宝记录列表
	 */
	@RequestMapping(value = "{id}/list")
	public @ResponseBody JsonResult findList(@PathVariable("id") Integer goodsTimesId, Page<SnatchRecordDetail> page,
			@RequestParam("timestamp") String timestamp) {
		page.setAutoCount(false);
		return BizCommon.findSnatchRecordDetailByGoodsTimes(snatchRecordDetailService, page, timestamp, goodsTimesId);
	}
}
