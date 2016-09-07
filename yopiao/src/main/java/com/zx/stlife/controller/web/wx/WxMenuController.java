package com.zx.stlife.controller.web.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.constant.ConstWeixinH5;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.wx.WxMenu;
import com.zx.stlife.entity.wx.menu.*;
import com.zx.stlife.service.wx.WxAccountService;
import com.zx.stlife.service.wx.WxMenuService;
import com.zx.stlife.tools.HttpUtils;
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
@RequestMapping(value = "/wxweb/menu")
public class WxMenuController extends BaseController{
	@Autowired
	private WxMenuService wxMenuService;
	@Autowired
	private WxAccountService wxAccountService;

	private static final String PERMISSION_PREFIX = "wx:menu";
	private static final String VIEW_URI_PREFIX = "wxweb/menu/menu-";
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model) {
		List<WxMenu> list =  wxMenuService.findAllSuper();
		model.addAttribute("list", list);
		fillBaseData(model);
		return VIEW_URI_PREFIX + "list";
	}

	private void fillBaseData(Model model) {
		model.addAttribute("wxMenuTypeMap", ConstWeixinH5.WXMenuType.wxMenuTypeMap)
				.addAttribute("wxMsgTypeMap", ConstWeixinH5.WXMsgType.wxMsgTypeMap);
	}

	public void fillFormData(Model model) {
		fillBaseData(model);
		model.addAttribute("superMenuList", wxMenuService.findAllSuper());
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		fillFormData(model);
		model.addAttribute("entity", new WxMenu());
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		fillFormData(model);
		model.addAttribute("entity", wxMenuService.get(id));
		return VIEW_URI_PREFIX + "form";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") WxMenu entity,
					   ParamsEntity paramsEntity,
					   RedirectAttributes redirectAttributes) {

		wxMenuService.save(entity);

		redirectAttributes.addFlashAttribute("message", "保存模块成功");

		return getAfterSaveRedirectUrl(paramsEntity);
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<String> ids,
					   HttpServletResponse response) {
		String result = "1";
		try {
			wxMenuService.delete(ids);
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
			@RequestParam("oldName") String oldName,
			@RequestParam("name") String name) {
		if (StringUtils.equals(name, oldName) ) {
			return "true";
		} else if (!wxMenuService.isExistsName(name)) {
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
	public WxMenu getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return wxMenuService.get(id);
		}
		return new WxMenu();
	}

	@RequiresPermissions(PERMISSION_PREFIX + ":sync")
	@RequestMapping(value = "sync", method = RequestMethod.POST)
	public void snyc(HttpServletResponse response) {
		List<WxMenu> menuList = wxMenuService.findAllSuper();
		Menu menu = new Menu();
		Button firstButtons[] = new Button[menuList.size()];
		for (int i = 0; i < menuList.size(); i++) {
			WxMenu entity = menuList.get(i);
			List<WxMenu> childList = entity.getChildrens();
			if (childList.size() == 0) {// 如果没有子菜单
				if (ConstWeixinH5.WXMenuType.VIEW.equals(entity.getType())) {// 链接菜单
					ViewButton viewButton = new ViewButton();
					viewButton.setName(entity.getName());
					viewButton.setType(entity.getType());
					viewButton.setUrl(entity.getUrl());
					firstButtons[i] = viewButton;
				}
				else if (ConstWeixinH5.WXMenuType.CLICK.equals(entity.getType())) {// 事件响应菜单
					CommonButton cb = new CommonButton();
					cb.setKey(entity.getMkey());
					cb.setName(entity.getName());
					cb.setType(entity.getType());
					firstButtons[i] = cb;
				}
			}
			else {// 如果有子菜单，证明是复杂菜单
				ComplexButton complexButton = new ComplexButton();
				complexButton.setName(entity.getName());
				Button[] secondButtons = new Button[childList.size()];
				for (int j = 0; j < childList.size(); j++) {
					WxMenu children = childList.get(j);
					String type = children.getType();
					if (ConstWeixinH5.WXMenuType.VIEW.equals(type)) {
						ViewButton viewButton = new ViewButton();
						viewButton.setName(children.getName());
						viewButton.setType(children.getType());
						viewButton.setUrl(children.getUrl());
						secondButtons[j] = viewButton;
					}
					else if (ConstWeixinH5.WXMenuType.CLICK.equals(type)) {
						CommonButton cb1 = new CommonButton();
						cb1.setName(children.getName());
						cb1.setType(children.getType());
						cb1.setKey(children.getMkey());
						secondButtons[j] = cb1;
					}
				}
				complexButton.setSub_button(secondButtons);
				firstButtons[i] = complexButton;
			}
		}
		menu.setButton(firstButtons);

		String accessToken = wxAccountService.getValidAccessToken();
		String url = ConstWeixinH5.MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = HttpUtils.httpRequest(url, "POST", JSON.toJSONString(menu));

		String msg = null;
		// 判断菜单创建结果
		if (null != jsonObject && 0 == jsonObject.getIntValue("errcode")) {
			logger.info(jsonObject.toJSONString());
			msg = "同步菜单信息数据成功！将在2-24小时内对所有用户生效！";
		}
		else {
			msg = "同步菜单信息数据失败！";
			if(null != jsonObject){
				msg += "详细：" + jsonObject.toJSONString();
			}
		}

		WebUtils.renderText(response, msg);
	}

	@RequiresPermissions(PERMISSION_PREFIX + ":sync")
	@RequestMapping(value = "queryMenu", method = RequestMethod.POST)
	public void queryMenu(HttpServletResponse response) {
		String accessToken = wxAccountService.getValidAccessToken();
		String url = ConstWeixinH5.MENU_QUERY_URL.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject = HttpUtils.httpRequest(url, "GET", null);
		String msg = null;
		if(jsonObject == null)
			msg = "查询失败";
		else
			msg = jsonObject.toJSONString();

		WebUtils.renderText(response, msg);
	}
}
