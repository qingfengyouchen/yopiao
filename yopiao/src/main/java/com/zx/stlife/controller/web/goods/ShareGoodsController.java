package com.zx.stlife.controller.web.goods;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.zx.stlife.entity.sys.User;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.base.jpa.query.Page;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.ShareGoods;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.goods.ShareGoodsService;
import com.zx.stlife.service.goods.ShareImageService;

@Controller
@RequestMapping(value = "/goods/shareGoods")
public class ShareGoodsController extends BaseController {

	private static final String PERMISSION_PREFIX = "goods:shareGoods";
	private static final String VIEW_URI_PREFIX = "goods/shareGoods/shareGoods-";
	@Autowired
	private ShareGoodsService shareGoodsService;
	@Autowired
	private ShareImageService shareImageService;
	@Autowired
	private GoodsTimesService goodsTimesService;
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model,
			ParamsEntity paramsEntity) {
		model.addAttribute("entity", shareGoodsService.detail(id))
				.addAttribute("detailsImageList",
						shareGoodsService.findByShareGoods(id));
		bindData(model);
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(
			@Valid @ModelAttribute("preloadEntity") ShareGoods entity,
			ParamsEntity paramsEntity, RedirectAttributes redirectAttributes) {
		shareGoodsService.save(entity);

		redirectAttributes.addFlashAttribute("message", "保存晒单成功");

		return getRedirectUrl(paramsEntity.get("listUrl"));
	}

	/**
	 * 查看晒单列表
	 * 
	 * @param model
	 * @param page
	 * @param paramsEntity
	 * @return
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page<ShareGoods> page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	/**
	 * 分页查看晒单
	 * 
	 * @param model
	 * @param page
	 * @param paramsEntity
	 * @return
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<ShareGoods> page, ParamsEntity paramsEntity) {
		shareGoodsService.search(page, paramsEntity.getParams());
		bindData(model);
		model.addAttribute("page", page);

		return VIEW_URI_PREFIX + "list-content";
	}

	/**
	 * 构建晒单LIST
	 * 
	 * @param model
	 */
	private void bindData(Model model) {
		model.addAttribute("shareGoodsMap", Const.ShareGoodsState.MAP)
				.addAttribute("shareGoodsList",
						shareGoodsService.findAllWithIdAndName());
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
			HttpServletResponse response) {
		String result = "1";
		try {
			shareGoodsService.deleteShareGoods(ids);
		} catch (Exception ex) {
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "deleteImg")
	@ResponseBody
	public void deleteImg(@RequestParam(value = "imgId", required = false)Integer imgId,HttpServletResponse response){
		String result = "1";
		try{
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(imgId);
			shareImageService.deleteImg(ids);
		}catch (Exception ex) {
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}

	@ModelAttribute("preloadEntity")
	public ShareGoods getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if (null != id) {
			return shareGoodsService.get(id);
		}
		return new ShareGoods();
	}
}
