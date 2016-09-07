package com.zx.stlife.controller.web.sys;

import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.entity.sys.Permission;
import com.zx.stlife.service.sys.ModuleService;
import com.zx.stlife.service.sys.PermissionService;
import com.zx.stlife.controller.web.BaseController;
import com.base.jpa.query.Page;
import com.base.modules.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/sys/permission")
public class PermissionController extends BaseController {

	@Autowired
	private PermissionService permissionService;
	@Autowired
	private ModuleService moduleService;

	private static final String PERMISSION_PREFIX = "sys:permission";
	private static final String VIEW_URI_PREFIX = "sys/permission/permission-";

	// 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
	//@RequiresRoles(value = { "Admin", "User" }, logical = Logical.OR)
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page page, ParamsEntity paramsEntity) {
		fillFormData(model);
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page page, ParamsEntity paramsEntity) {
		permissionService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		return VIEW_URI_PREFIX + "list-content";
	}

	private void fillFormData(Model model) {
		model.addAttribute("moduleList", moduleService.findAll());
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		fillFormData(model);
		model.addAttribute("entity", new Permission());
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", permissionService.get(id));
		fillFormData(model);
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") Permission entity,
					   ParamsEntity paramsEntity,
					   RedirectAttributes redirectAttributes) {

		permissionService.save(entity);

		redirectAttributes.addFlashAttribute("message", "保存权限成功");

		return getAfterSaveRedirectUrl(paramsEntity);
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
					   HttpServletResponse response) {
		String result = "1";
		try {
			permissionService.delete(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "checkCode")
	@ResponseBody
	public String checkLoginName(
			@RequestParam(value = "oldCode", required = false) String oldCode,
			@RequestParam(value = "code", required = false) String code) {
		if (StringUtils.equals(code, oldCode) ) {
			return "true";
		} else if (!permissionService.isExistsCode(code)) {
			return "true";
		}

		return "false";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadEntity")
	public Permission getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return permissionService.get(id);
		}
		return new Permission();
	}

	/**
	 * 不自动绑定对象中的roleList属性，另行处理。
	 */
	/*@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("roleList");
	}*/
}
