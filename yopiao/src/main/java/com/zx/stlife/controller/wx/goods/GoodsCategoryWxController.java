package com.zx.stlife.controller.wx.goods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.jpa.query.Page;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsCategory;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.service.goods.GoodsCategoryService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.tools.DateUtils;

@Controller
@RequestMapping(value = "/wx/goods/goodsCategory")
public class GoodsCategoryWxController extends BaseController<GoodsCategory>{

    @Autowired
    private GoodsTimesService goodsTimesService;
    @Autowired
    private GoodsCategoryService goodsCategoryService;
    
    private static final String VIEW_URI_PREFIX = "wx/goodsTimes/goods-category-";
    
    /**
     * 获取商品分类
     */
    @RequestMapping(value = "/listCategory", method = RequestMethod.GET)
    public String listCategory(Model model, Page<GoodsCategory> page) {
        Date date = DateUtils.getNow();
        String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(date);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss" );
        String now = sdf.format(date);
        page.setPageSize(10);
        List<GoodsCategory> goodsCategoryList = goodsCategoryService.findAllWithCache();
        page.setResult(goodsCategoryList);
        model.addAttribute("page", page);
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("date", date);
        model.addAttribute("now", now);
        return VIEW_URI_PREFIX + "list-content";
    }

    /**
     * 按商品分类查询商品列表
     */
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public String getGoodsTimesByCategory(Model model, 
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            Page<GoodsTimes> page) {
        String timestamp = DateUtils.dateToYYYYMMDDHHMMSSSSSString(DateUtils.getNow());
        Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
        goodsTimesService.findGoodsTimesByCategory(page, date, null, categoryId);
        GoodsCategory tmp =  goodsCategoryService.get(categoryId);
        model.addAttribute("page", page);
        model.addAttribute("categoryName", tmp.getName());
        return VIEW_URI_PREFIX + "times-list-content";
    }
}
