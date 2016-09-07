package com.zx.stlife.controller.web.sys;

import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.entity.sys.Category;
import com.zx.stlife.service.sys.CategoryService;
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
@RequestMapping(value = "/sys/category")
public class CategoryController extends BaseController {
	@Autowired
	private CategoryService categoryService;
	
	private static final String MODULE = "sys:category";
	private static final String URI_PREFIX = "sys/category/category-";

	@RequiresPermissions(MODULE + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return URI_PREFIX + "list";
	}

	@RequiresPermissions(MODULE + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page page, ParamsEntity paramsEntity) {
		categoryService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		bindData(model);
		return URI_PREFIX + "list-content";
	}

	private void bindData(Model model) {
		//model.addAttribute("categoryMap", Const.categoryMap);
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new Category());
		bindData(model);
		return URI_PREFIX + "form";
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", categoryService.get(id));
		bindData(model);
		return URI_PREFIX + "form";
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") Category entity,
					   ParamsEntity paramsEntity,
					   RedirectAttributes redirectAttributes) {

		categoryService.save(entity);

		redirectAttributes.addFlashAttribute("message", "保存成功");

		return getAfterSaveRedirectUrl(paramsEntity);
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
					   HttpServletResponse response) {
		String result = "1";
		try {
			categoryService.delete(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}

	@RequiresPermissions(MODULE + PERMISSION_VIEW)
	@RequestMapping(value = "checkName")
	@ResponseBody
	public String checkLoginName(
			@RequestParam(value = "oldName", required = false) String oldName,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "category", required = false) Byte category) {
		if (StringUtils.equals(name, oldName) ) {
			return "true";
		} else if (!categoryService.isExistsName(name, category)) {
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
	public Category getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if (null != id) {
			return categoryService.get(id);
		}
		return new Category();
	}
}
