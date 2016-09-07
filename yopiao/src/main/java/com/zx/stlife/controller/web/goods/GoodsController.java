package com.zx.stlife.controller.web.goods;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.goods.GoodsImage;
import com.zx.stlife.entity.goods.GoodsInfo;
import com.zx.stlife.service.TmpFileService;
import com.zx.stlife.service.goods.GoodsCategoryService;
import com.zx.stlife.service.goods.GoodsService;

@Controller
@RequestMapping(value = "/goods/goods")
public class GoodsController extends BaseController {
	@Autowired
	private GoodsService service;
	@Autowired
	private TmpFileService tmpFileService;
	@Autowired
	private GoodsCategoryService goodsCategoryService;
	
	private static final String PERMISSION_PREFIX = "goods:goods";
	private static final String VIEW_URI_PREFIX = "goods/goods/goods-";

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page page, ParamsEntity paramsEntity) {
		service.search(page, paramsEntity.getParams());
		bindData(model);
		model.addAttribute("page", page);

		return VIEW_URI_PREFIX + "list-content";
	}

	private void bindData(Model model){
		model.addAttribute("booleanMap", Const.booleanMap)
				.addAttribute("goodsStateMap", Const.GoodsState.MAP)
				.addAttribute("goodsCategoryList", goodsCategoryService.findAllWithIdAndName());
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new GoodsInfo());
		bindData(model);
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", service.get(id));
		
		List<GoodsImage> tImageList = service.findByGoodsAndCategory(id, Const.GoodsImageCategory.TOP_SWITCH);
		int index = 0;
		for (GoodsImage img : tImageList) {
			if (StringUtils.isNotEmpty(img.getUrl())
				&& img.getUrl().indexOf("http") >= 0) {
				index++;
				model.addAttribute("topSwitchUrl_" + index, img.getUrl());
			}
		}
		index = 0;
		List<GoodsImage> dImageList = service.findByGoodsAndCategory(id, Const.GoodsImageCategory.DETAILS);
		for (GoodsImage img : dImageList) {
			if (StringUtils.isNotEmpty(img.getUrl())
				&& img.getUrl().indexOf("http") >= 0) {
				index++;
				model.addAttribute("detailsUrl_" + index, img.getUrl());
			}
		}
		model.addAttribute("topSwitchImageList", tImageList);
		model.addAttribute("detailsImageList", dImageList);
		bindData(model);
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") GoodsInfo entity,
					   ParamsEntity paramsEntity,
					   @RequestParam(value = "tmpThumbId", required = false)Integer tmpThumbId,
					   @RequestParam(value = "tmpTopIds", required = false)List<Integer> tmpTopIdList,
					   @RequestParam(value = "tmpDetailIds", required = false)List<Integer> tmpDetailIdsList,
					   @RequestParam(value = "topSwitchUrl", required = false)List<String> topSwitchUrlList,
					   @RequestParam(value = "detailsUrl", required = false)List<String> detailsUrlList,
					   RedirectAttributes redirectAttributes) {

		service.save(entity, tmpThumbId, tmpTopIdList, tmpDetailIdsList, topSwitchUrlList, detailsUrlList,
				paramsEntity.get("name"), SimpleUtils.stringToByte(paramsEntity.get("oldState")));

		redirectAttributes.addFlashAttribute("message", "保存商品成功");

		return getRedirectUrl(paramsEntity.get("listUrl"));
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "updateState", method = RequestMethod.POST)
	@ResponseBody
	public Integer updateState(@RequestParam("id")Integer id,
					   @RequestParam("oldState")Byte oldState,
					   HttpServletResponse response) {
		Integer result = null;
		try{
			service.updateState(id, oldState);
			result = Const.COMMON_RESULT_SUCCESS;
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
			result = 0;
		}

		return result;
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
					   HttpServletResponse response) {
		String result = "1";
		try {
			service.delete(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "checkName")
	@ResponseBody
	public String checkLoginName(
			@RequestParam(value = "oldName", required = false) String oldName,
			@RequestParam(value = "name", required = false) String name) {
		if (StringUtils.equals(name, oldName) ) {
			return "true";
		} else if (!service.isExistsName(name)) {
			return "true";
		}

		return "false";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "deleteImg")
	@ResponseBody
	public int deleteImg(@RequestParam(value = "imgId", required = false)Integer imgId,
						 @RequestParam(value = "tmpImgId", required = false)Integer tmpFileId,
						 @RequestParam(value = "type", required = false)String type){
		int result = 0;
		if("thumb".equals(type)){
			if ( null != imgId ) {
				result = service.deleteThumbImg(imgId);
			} else if (null != tmpFileId ) {
				result = tmpFileService.deleteById(tmpFileId, true);
			}
		}else {
			if( null != imgId ){
				result = service.deleteImg(imgId);
			}else if(null != tmpFileId ){
				result = tmpFileService.deleteById(tmpFileId, true);
			}
		}

		return result;
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadEntity")
	public GoodsInfo getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return service.get(id);
		}
		return new GoodsInfo();
	}
}
