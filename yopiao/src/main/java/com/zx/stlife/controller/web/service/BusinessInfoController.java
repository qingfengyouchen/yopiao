package com.zx.stlife.controller.web.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.base.jpa.query.Page;
import com.base.modules.util.Encodes;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.service.BusinessInfo;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.service.BusinessInfoService;

/**
 * 天台商业
 * 
 * @author fan
 *
 */
@Controller
@RequestMapping(value = "/service/businessInfo")
public class BusinessInfoController extends BaseController {
	@Autowired
	private BusinessInfoService businessInfoService;

	private static final String PERMISSION_PREFIX = "service:businessInfo";
	private static final String VIEW_URI_PREFIX = "service/businessInfo/business-info-";

	/**
	 * 查看
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page<BusinessInfo> page,
			ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<BusinessInfo> page,
			ParamsEntity paramsEntity) {
		businessInfoService.search(page, paramsEntity.getParams());
		model.addAttribute("businessInfoMap", Const.BusinessInfoState.MAP)
				.addAttribute("page", page);
		return VIEW_URI_PREFIX + "list-content";
	}

	/**
	 * 创建
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new BusinessInfo()).addAttribute(
				"businessInfoMap", Const.BusinessInfoState.MAP);
		return VIEW_URI_PREFIX + "form";
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 * @param paramsEntity
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "save")
	public String save(
			@Valid @ModelAttribute("preloadEntity") BusinessInfo entity,
			ParamsEntity paramsEntity, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getCurrentUser();
		entity.setCreator(user.getUserName());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		entity.setState(Const.CommonState.ENABLE);
		businessInfoService.save(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return getRedirectUrl(paramsEntity.get("listUrl"));
	}

	/**
	 * 编辑
	 * 
	 * @param microBusinessId
	 * @param model
	 * @param paramsEntity
	 * @return
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer businessInfoId, Model model,
			ParamsEntity paramsEntity) {
		BusinessInfo businessInfo = businessInfoService.findOne(businessInfoId);
		User user = UserUtils.getCurrentUser();
		businessInfo.setEditor(user.getUserName());
		businessInfo.setEditTime(new Date());
		model.addAttribute("entity", businessInfo).addAttribute(
				"businessInfoMap", Const.BusinessInfoState.MAP);
		return VIEW_URI_PREFIX + "form";
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 * @param response
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
			HttpServletResponse response) {
		String result = "1";
		try {
			businessInfoService.delete(ids);
		} catch (Exception ex) {
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadEntity")
	public BusinessInfo getEntity(
			@RequestParam(value = "id", required = false) Integer id) {
		if (null != id) {
			return businessInfoService.findOne(id);
		}
		return new BusinessInfo();
	}

	/**
	 * 天台商业出售静态页面列表
	 * @param type
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "businessVendList", method = RequestMethod.GET)
	public String businessVendList(
			@RequestParam(value = "type", required = false, defaultValue = "1") Byte type,
			Page<BusinessInfo> page, Model model) {
		businessInfoService.searchAll(page, type);
		model.addAttribute("businessInfo", Const.BUSINESSINFO)
				.addAttribute("page", page);
		return VIEW_URI_PREFIX +"vend-"+ "mobile";
	}
	/**
	 * 天台商业出租静态页面列表
	 * @param type
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "businessLendList", method = RequestMethod.GET)
	public String businessLendList(
			@RequestParam(value = "type", required = false, defaultValue = "2") Byte type,
			Page<BusinessInfo> page, Model model) {
		businessInfoService.searchAll(page, type);
		model.addAttribute("businessInfo", Const.BUSINESSINFO)
				.addAttribute("page", page);
		return VIEW_URI_PREFIX + "lend-" + "mobile";
	}
}
