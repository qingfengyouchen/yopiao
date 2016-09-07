package com.zx.stlife.controller.wx;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.base.jpa.query.Page;
import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.order.SnatchListItemService;
import com.zx.stlife.service.sys.ImageSettingService;
import com.zx.stlife.tools.DateUtils;

@Controller
@RequestMapping(value = "/wx")
public class IndexWxController extends BaseController<GoodsTimes>{
	
	@Autowired
	private GoodsTimesService goodsTimesService;
	@Autowired
	private ImageSettingService imageSettingService;
	@Autowired
    private SnatchListItemService snatchListItemService;

	@RequestMapping("/url")
	public String url(Model model,
			@RequestParam(value = "url") String url){ 
		// TODO 此方法暂时用--add by liulg
		return "wx/" + url;
	}

	@RequestMapping("/index")
	public String index(Model model){ 
		model.addAttribute("goodsTimeList", goodsTimesService.findTop());
		Date date = DateUtils.getNow();
		Page<GoodsTimes> page=new Page<GoodsTimes>();
		goodsTimesService.findGoodsTimesByAttr(page, date, 1, null);
		model.addAttribute("page",page);
		bindData(model);
		return "wx/index";
	}
	
	private void bindData(Model model) {
		Integer userId = UserWxUtils.getCurrUserId();

		Integer listCount=0;
		if(userId!=null){
			listCount= snatchListItemService.getUserListCount(userId); //获取清单总数（购物车）
		}
		model.addAttribute("userId", userId)
				.addAttribute("listCount", listCount)
				.addAttribute("lastWinngUserList", goodsTimesService.winngUser())
				.addAttribute("imageList", imageSettingService.findAllWithCache(Const.ImageCategory.INDEX_TOP_SWITCH));
	}
}
