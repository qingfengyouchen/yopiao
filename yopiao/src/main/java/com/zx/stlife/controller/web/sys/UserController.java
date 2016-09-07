package com.zx.stlife.controller.web.sys;

import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.RoleService;
import com.zx.stlife.controller.web.BaseController;
import com.base.jpa.query.Page;
import com.base.modules.security.utils.Digests;
import com.base.modules.util.Encodes;
import com.base.modules.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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
@RequestMapping(value = "/sys/user")
public class UserController extends BaseController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private RoleService roleService;

	private static final String PERMISSION_PREFIX = "sys:user";
	private static final String VIEW_URI_PREFIX = "sys/user/user-";

	// 特别设定多个ReuireRoles之间为Or关系，而不是默认的And.
	//@RequiresRoles(value = { "Admin", "User" }, logical = Logical.OR)
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page page, ParamsEntity paramsEntity) {
		accountService.searchUser(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		fillBaseData(model);
		return VIEW_URI_PREFIX + "list-content";
	}

	private void fillBaseData(Model model) {
		model.addAttribute("stateMap", Const.UserState.stateMap);
		model.addAttribute("genderMap", Const.Gender.genderMap);
	}

	private void fillFormData(Model model, Integer userId) {
		fillBaseData(model);
		model.addAttribute("allRoles", roleService.findAllAndFlagSelected(userId));
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		fillFormData(model, UserUtils.getCurrentUserId());
		model.addAttribute("user", new User());
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("user", accountService.getUser(id));
		fillFormData(model, id);
		return VIEW_URI_PREFIX + "form";
	}

	/**
	 * 自行绑定表单中的checkBox roleList到对象中.
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadUser") User user,
					   @RequestParam(value = "roleIds", required = false) List<Integer> checkedRoleList,
					   ParamsEntity paramsEntity, RedirectAttributes redirectAttributes) {

		accountService.saveUser(user, checkedRoleList);

		redirectAttributes.addFlashAttribute("message", "保存用户成功");

		return getRedirectUrl(paramsEntity.get("listUrl"));
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
					   HttpServletResponse response) {
		String result = "1";
		try {
			accountService.deleteUser(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}

	@RequiresPermissions(value = {PERMISSION_PREFIX + PERMISSION_VIEW, "sales:saler:view"}, logical = Logical.OR)
	@RequestMapping(value = "checkUserName")
	@ResponseBody
	public String checkLoginName(
			@RequestParam(value = "oldUserName", required = false) String oldUserName,
			@RequestParam(value = "userName", required = false) String userName) {

		boolean isExists = accountService.isExistsUserName(userName, oldUserName);
		return isExists ? "false" : "true";
	}

	@RequiresPermissions(value = PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "checkMobileNo")
	@ResponseBody
	public String checkMobileNo(
			@RequestParam(value = "oldMobileNo", required = false) String oldMobileNo,
			@RequestParam(value = "mobileNo", required = false) String mobileNo) {

		boolean isExists = accountService.isExistsMobileNo(mobileNo, oldMobileNo);
		return isExists ? "false" : "true";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(value = "id", required = false) Integer id) {
		if (null != id) {
			return accountService.getUserForRefresh(id);
		}
		return new User();
	}

	@RequiresPermissions(PERMISSION_PREFIX + ":changePwd")
	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	@ResponseBody
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
								 @RequestParam("password")String password){
		User user = UserUtils.getCurrentUser(true);
		String result = null;
		if(user.hasId()){
			byte[] hashPassword = Digests.sha1(oldPassword.getBytes(),
					Encodes.decodeHex(user.getSalt()), AccountService.HASH_INTERATIONS);
			String oldPwd = Encodes.encodeHex(hashPassword);
			if(StringUtils.equals(oldPwd, user.getPassword())){
				user.setPlainPassword(password);
				accountService.entryptPassword(user);
				accountService.save(user);
				result = "1";
			}else{
				result = "2";
			}
		}else{
			result = "-1";
		}

		return result;
	}
}
