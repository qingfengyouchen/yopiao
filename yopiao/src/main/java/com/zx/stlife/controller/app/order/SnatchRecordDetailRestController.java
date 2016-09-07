package com.zx.stlife.controller.app.order;

import com.zx.stlife.tools.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.service.order.SnatchRecordDetailService;

/**
 * 夺宝记录明细app
 *
 * @author lxw
 */
@RestController
@RequestMapping("/app/snatch/snatchRecordDetail")
public class SnatchRecordDetailRestController extends BaseRestController {

    @Autowired
    private SnatchRecordDetailService snatchRecordDetailService;

    /**
     * @param goodsTimesId 商品期号id
     */
    @RequestMapping(value = "/{id}/list", method = RequestMethod.GET)
    public JsonResult findSnatchRecordDetailByGoodsTimes(@PathVariable("id") Integer goodsTimesId,
                                                         @RequestParam(value = "timestamp") String timestamp,
                                                         Page<SnatchRecordDetail> page) {
        return BizCommon.findSnatchRecordDetailByGoodsTimes(
                snatchRecordDetailService, page, timestamp, goodsTimesId);
    }
}
