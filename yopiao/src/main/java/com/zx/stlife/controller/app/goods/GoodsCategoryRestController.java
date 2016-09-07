package com.zx.stlife.controller.app.goods;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.goods.GoodsCategory;
import com.zx.stlife.entity.goods.GoodsImage;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.service.goods.GoodsCategoryService;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品分类服务API
 */

@RestController
@RequestMapping("/app/goods/goodsCategory")
public class GoodsCategoryRestController extends BaseRestController {

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list() {
        try {
            List<GoodsCategory> goodsCategoryList = goodsCategoryService.findAllWithCache();
            List<Map<String, Object>> goodsCategoryMapList = ConvertUtils.convertCollectionToListMap(
                    goodsCategoryList, "id", "name", "imgUrl");

            return buildSuccessResult(goodsCategoryMapList);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return buildFailureResult();
        }
    }
}
