package com.zx.stlife.controller.web.member;

import com.base.jpa.query.Page;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.member.MemberLevel;
import com.zx.stlife.service.member.MemberLevelService;
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
@RequestMapping(value = "/member/memberLevel")
public class MemberLevelController extends BaseController {
	@Autowired
	private MemberLevelService service;
	
	private static final String PERMISSION_PREFIX = "member:memberLevel";
	private static final String VIEW_URI_PREFIX = "member/memberLevel/member-level-";

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page page, ParamsEntity paramsEntity) {
		service.search(page, paramsEntity.get("name"));
		model.addAttribute("page", page);
		return VIEW_URI_PREFIX + "list-content";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new MemberLevel());
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", service.get(id));
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") MemberLevel entity,
					   ParamsEntity paramsEntity,
					   RedirectAttributes redirectAttributes) {

		service.save(entity);

		redirectAttributes.addFlashAttribute("message", "保存会员等级成功");

		return getRedirectUrl(paramsEntity.get("listUrl"));
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

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadEntity")
	public MemberLevel getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return service.get(id);
		}
		return new MemberLevel();
	}
}
