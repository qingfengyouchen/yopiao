package com.zx.stlife.controller.app.goods;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.goods.GoodsImage;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.order.SnatchNumService;
import com.zx.stlife.service.order.SnatchRecordService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.PageUtils;

/**
 * 商品服务API
 */

@RestController
@RequestMapping("/app/goods/goodsTimes")
public class GoodsTimesRestController extends BaseRestController {

    @Autowired
    private GoodsTimesService goodsTimesService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SnatchNumService snatchNumService;

    @Autowired
    private SnatchRecordService snatchRecordService;
    
    @Autowired
    private MemberService memberService;
    
    
    /**
     * 商品详情
     *
     * @param goodsTimesId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public JsonResult getDetail(@PathVariable("id") Integer goodsTimesId,
                                @RequestParam(value = "userId", required = false) Integer userId) {
        try {
            GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
            return getDetail(userId, goodsTimes);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return buildFailureResult();
        }
    }

    /**
     * 最新一期商品详情
     *
     * @param goodsId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/detail/getLast", method = RequestMethod.GET)
    public JsonResult getLastDetail(@RequestParam("gid") Integer goodsId,
                                    @RequestParam(value = "userId", required = false) Integer userId) {
        try {
            GoodsTimes goodsTimes = goodsTimesService.getLastByGoods(goodsId);
            return getDetail(userId, goodsTimes);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return buildFailureResult();
        }
    }

    private JsonResult getDetail( Integer userId, GoodsTimes goodsTimes) {
        if (goodsTimes == null) {
            return buildSuccessResult();
        }
        Map<String, Object> maps = ConvertUtils.convertEntityToMap(goodsTimes,
                new String[]{"id"},
                new String[]{"times"},
                new String[]{"totalTimes"},
                new String[]{"totalBuyTimes"},
                new String[]{"snatchProgress"},
                new String[]{"goodsInfo.id", "goodsId"},
                new String[]{"goodsName"},
                new String[]{"goodsTip"},
                new String[]{"thumbnail"},
                new String[]{"isTenYuan"},
                new String[]{"state"},
                new String[]{"canGetCqsscno"},
                new String[]{"computeDetailUri"},
                new String[]{"luckNum"},
                new String[]{"winngUserId"},
                new String[]{"winngUserHeadImg"},
                new String[]{"openTimeStr"},
                new String[]{"winngUserIdentity"},
                new String[]{"winngUserName"},
                new String[]{"winngUserBuyTimes"},
                new String[]{"createTimeStr"});

        String currentTimeStr = DateUtils
                .dateToYYYYMMDDHHMMSSSSSString(DateUtils.getNow());
        maps.put("currentTimeStr", currentTimeStr);

        maps.put("detailsHtmlUrl", goodsTimes.getGoodsInfo()
                .getDetailsHtmlUrl());
        List<GoodsImage> goodsImages = goodsService.findByGoodsAndCategory(
                goodsTimes.getGoodsInfo().getId(), Const.GoodsImageCategory.TOP_SWITCH);
        if (goodsImages.size() > 0) {
            List<String> imgList = new ArrayList<>();
            ConvertUtils.convertPropertyToList(goodsImages, "url", imgList);
            maps.put("topSwitchImages", imgList);
        } else {
            maps.put("topSwitchImages", null);
        }
        
        List<GoodsImage> detailsUrlList = goodsService.findByGoodsAndCategory(
        		goodsTimes.getGoodsInfo().getId(), Const.GoodsImageCategory.DETAILS);
        if (detailsUrlList.size() > 0) {
            List<String> imgList = new ArrayList<>();
            ConvertUtils.convertPropertyToList(detailsUrlList, "url", imgList);
            maps.put("detailSwitchImages", imgList);
        } else {
            maps.put("detailSwitchImages", null);
        }

        Integer userBuyTimes = null;
        List<Integer> userNums = null;
        if (userId != null) {
            userBuyTimes = snatchRecordService.getBuyTimesByGoodsTimesAndUser(goodsTimes.getId(), userId);
            if(userBuyTimes != null) {
                userNums = snatchNumService.findTopNumsByGoodsTimesAndUserBuyTimes(
                        userBuyTimes, goodsTimes.getId(), userId);
            }
        }

        if(userBuyTimes == null){
            userBuyTimes = 0;
        }

        maps.put("userBuyTimes", userBuyTimes);
        maps.put("userNums", userNums);

        Boolean hasLastGoodsTimes = false;
        if (goodsTimes.getState() != Const.GoodsTimesState.GOING) {// 非进行中状态才有去判断有没有最新一期
            hasLastGoodsTimes = goodsTimesService.hasLast(goodsTimes.getGoodsInfo().getId());
        }
        maps.put("hasLastGoodsTimes", hasLastGoodsTimes);

        return buildSuccessResult(maps);
    }


    /**
     * 商品往期揭晓
     */
    @RequestMapping(value = "/record/list", method = RequestMethod.GET)
    public JsonResult record(@RequestParam("goodsId") Integer goodsId,
                             @RequestParam("timestamp") String timestamp, Page<GoodsTimes> page) {
        try {
            Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
            goodsTimesService.getRecord(date, page, goodsId);
            if (SimpleUtils.isNullList(page.getResult())) {
                return buildSuccessResult();
            }
            Map<String, Object> data = ConvertUtils.convertEntityToMap(page,
                    "pageNo", "totalPages", "totalCount");
            List<Map<String, Object>> result = ConvertUtils
                    .convertCollectionToListMap(page.getResult(), "id",
                            "times", "state", "openTimeStr", "winngUserIdentity", "winngUserId",
                            "winngUserName", "winngUserHeadImg", "winngUserBuyTimes", "luckNum");
            data.put("result", result);
            return buildSuccessResult(data);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return buildFailureResult();
        }
    }

    /**
     * 按商品分类查询商品列表
     */
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public JsonResult getGoodsTimesByCategory(
            @RequestParam(value = "isTenYuan", required = false) Integer tenYuan,
            @RequestParam(value = "goodsCategoryId", required = false) Integer goodsCategoryId,
            @RequestParam("timestamp") String timestamp, Page<GoodsTimes> page) {

        Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
        try {
            goodsTimesService.findGoodsTimesByCategory(page, date, tenYuan,
                    goodsCategoryId);

            return PageUtils.buildPage(page, "id", "goodsName", "thumbnail", "isTenYuan",
                    "totalTimes", "totalBuyTimes", "snatchProgress");
        } catch  (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return buildFailureResult();
        }
    }

    /**
     * 按人气/最新/进度/总需人次查询商品列表
     */
    @RequestMapping(value = "/attr/list", method = RequestMethod.GET)
    public JsonResult getGoodsTimesByAttr(
            @RequestParam("category") Integer category,
            @RequestParam("timestamp") String timestamp,
            @RequestParam(value = "order", required = false) String order,
            Page<GoodsTimes> page) {

        return BizCommon.getGoodsTimesByAttr(
                page, timestamp, category, order, goodsTimesService);
    }

    /**
     * 按人气/最新/进度/总需人次查询商品列表
     */
    @RequestMapping(value = "/attr/listbyname", method = RequestMethod.GET)
    public JsonResult getGoodsTimesByAttr(
            @RequestParam("category") Integer category,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("goodsname") String goodsname,
            @RequestParam(value = "order", required = false) String order,
            Page<GoodsTimes> page) {

        try {
            //app客户端需要先做一次url的encode处理
            goodsname = URLDecoder.decode(goodsname, "UTF-8");
        } catch (Exception e) {
            goodsname = "";
        }

        return BizCommon.getGoodsTimesByAttrAndName(
                page, timestamp, category, goodsname, order, goodsTimesService);
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public JsonResult queryResult(@RequestParam("gid") Integer goodsTimesId){
        GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
        if(goodsTimes == null || goodsTimes.getState() < 1){
            return buildFailureResult(301); // 商品期号不存在
        }

        Map<String, Object> data = ConvertUtils.convertEntityToMap(goodsTimes,
                "state", "canGetCqsscno", "computeDetailUri", "luckNum", "winngUserId",
                "winngUserHeadImg", "winngUserIdentity", "winngUserName", "winngUserBuyTimes", "openTimeStr");

        return buildSuccessResult(data);
    }

    /**
     * 3.6. 最新揭晓
     * 根据揭晓时间，显示最新的100条数据
     * @param timestamp
     * @param page
     * @return
     */
    @RequestMapping(value = "/listLastOpen", method = RequestMethod.GET)
    public JsonResult listLastOpen(@RequestParam("timestamp") String timestamp, Page<GoodsTimes> page) {
        if(page.getPageNo() > 10){
            return buildSuccessResult();
        }

        page.setPageSize(10);
        Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
        try {
            goodsTimesService.findLastOpen(page, date);
            JsonResult jsonResult = PageUtils.buildPage(page, "id", "goodsName", "thumbnail", "isTenYuan",
                    "times", "state", "openTimeStr", "canGetCqsscno", "winngUserId",
                    "winngUserName", "winngUserBuyTimes", "luckNum");

            String currentTimeStr = DateUtils
                    .dateToYYYYMMDDHHMMSSSSSString(DateUtils.getNow());
            ((Map<String, Object>)jsonResult.getData()).put("currentTimeStr", currentTimeStr);
            return jsonResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return buildFailureResult();
        }
    }

    /**
     * 按商品ID查询积分
     */
    @RequestMapping(value = "/points", method = RequestMethod.GET)
    public JsonResult getGoodsPointsByID(@RequestParam("id") Integer goodsId){
        GoodsInfo goodsInfo = goodsService.get(goodsId);
        if(goodsInfo == null){
            return buildFailureResult(301); // 商品期号不存在
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", goodsId.toString());
        if (goodsInfo.getChangeJifen() == null) {
            data.put("points", "0");
        } else {
            data.put("points", goodsInfo.getChangeJifen().toString());
        }

        return buildSuccessResult(data);
    }
    
    /**
     * 按商品GoodsTimesId查询兑奖状态
     */
    @RequestMapping(value = "/getExchangeState", method = RequestMethod.GET)
    public JsonResult getExchangeState(@RequestParam("gid") Integer goodsTimesId){
        GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
        if(goodsTimes == null || goodsTimes.getState() < 1){
            return buildFailureResult(301); // 商品期号不存在
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("gid", goodsTimesId.toString());
        if (goodsTimes.getExchangeState() == null) {
            data.put("exchangeState", "0");
        } else {
            data.put("exchangeState", goodsTimes.getExchangeState().toString());
        }

        return buildSuccessResult(data);
    }
    
    /**
     * 按商品GoodsTimesId进行兑奖
     */
    @RequestMapping(value = "/setExchangeState", method = RequestMethod.GET)
    public JsonResult setExchangeState(@RequestParam("gid") Integer goodsTimesId,
    		@RequestParam("state") Integer exchangeState,
    		@RequestParam("userId") Integer userId){
        GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
        if(goodsTimes == null || goodsTimes.getState() < 1){
            return buildFailureResult(301); // 商品期号不存在
        }

        try {
            goodsTimes.setExchangeState(exchangeState);
            Map<String, Object> data = new HashMap<String, Object>();
            System.out.println("goodsTimesId: " + goodsTimesId.toString());
            goodsTimesService.updateGoodsState(goodsTimesId, exchangeState);
            if (1 == exchangeState) {
            	memberService.recordJifen(userId, 
                		goodsTimes.getGoodsInfo().getChangeJifen(), "中奖兑换积分");
            }
            data.put("gid", goodsTimesId.toString());
            data.put("result", "OK");
            
            return buildSuccessResult(data);
        } catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
    }
    
    /**
     * 按商品GoodsTimes的Timesid获取最新的gid
     */
    @RequestMapping(value = "/getGidByTimes", method = RequestMethod.GET)
    public JsonResult getGidByTimes(@RequestParam("times") Integer times){
    	GoodsTimes goodsTimes = goodsTimesService.getByTimes(times);
        if(goodsTimes == null || goodsTimes.getState() < 1){
            return buildFailureResult(301); // 商品期号不存在
        }
        
        Integer newestTimesid = 0;
        if (goodsTimes.getState() != Const.GoodsTimesState.GOING) {// 非进行中状态才有去判断有没有最新一期
        	newestTimesid = goodsTimesService.getNewestTimesid(goodsTimes.getGoodsInfo().getId());
        } else {
        	newestTimesid = goodsTimes.getId();
        }
        
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("gid", newestTimesid.toString());
            data.put("result", "OK");
            return buildSuccessResult(data);
        } catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
    }
}
