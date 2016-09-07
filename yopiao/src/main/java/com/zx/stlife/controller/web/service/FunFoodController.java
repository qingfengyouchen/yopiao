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
import com.zx.stlife.entity.service.FunFood;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.service.FunFoodService;
/**
 * 休闲美食
 * @author fan
 *
 */
@Controller
@RequestMapping(value="/service/funFood")
public class FunFoodController extends BaseController{

	@Autowired
	private FunFoodService funFoodService;
	
	private static final String PERMISSION_PREFIX = "service:funFood";
	private static final String VIEW_URI_PREFIX = "service/funFood/fun-food-";

	/**
	 * 查看
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page<FunFood> page,
			ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<FunFood> page,
			ParamsEntity paramsEntity) {
		funFoodService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		return VIEW_URI_PREFIX + "list-content";
	}

	/**
	 * 创建
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new FunFood());
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
			@Valid @ModelAttribute("preloadEntity") FunFood entity,
			ParamsEntity paramsEntity, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getCurrentUser();
		entity.setCreator(user.getUserName());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		entity.setState(Const.CommonState.ENABLE);
		funFoodService.save(entity);
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
	public String edit(@PathVariable("id") Integer funFoodId, Model model,
			ParamsEntity paramsEntity) {
		FunFood funFood = funFoodService.findOne(funFoodId);
		User user = UserUtils.getCurrentUser();
		funFood.setEditor(user.getUserName());
		funFood.setEditTime(new Date());
		model.addAttribute("entity",funFood);
		bindData(model);
		return VIEW_URI_PREFIX + "form";
	}

	private void bindData(Model model) {
		model.addAttribute("funFoodList", funFoodService.findByState());
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
			funFoodService.delete(ids);
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
	public FunFood getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return funFoodService.findOne(id);
		}
		return new FunFood();
	}
	
	/**
	 * 休闲美食静态页面列表
	 * @param type
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "funFoodList", method = RequestMethod.GET)
	public String funFoodList(Page<FunFood> page, Model model) {
		funFoodService.searchAll(page);
		model.addAttribute("funFood", Const.FUNFOOD)
				.addAttribute("page", page);
		return VIEW_URI_PREFIX + "mobile";
	}
}