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
import com.zx.stlife.entity.service.MicroBusiness;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.service.MicroBusinessService;
/**
 * 天台微商
 * @author fan
 *
 */
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(value="/service/microBusiness")
public class MicroBusinessController extends BaseController{
	
	@Autowired
	private MicroBusinessService microBusinessService;
	
	private static final String PERMISSION_PREFIX = "service:microBusiness";
	private static final String VIEW_URI_PREFIX = "service/microBusiness/micro-business-";
	
	/**
	 *  查看
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model,Page<MicroBusiness> page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<MicroBusiness> page, ParamsEntity paramsEntity) {
		microBusinessService.search(page,paramsEntity.getParams());
		model.addAttribute("page", page);
		return VIEW_URI_PREFIX + "list-content";
	}
	
	/**
	 *  创建
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new MicroBusiness());
		return VIEW_URI_PREFIX + "form";
	}
	
	/**
	 *  保存
	 * @param entity
	 * @param paramsEntity
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value="save")
	public String save(@Valid @ModelAttribute("preloadEntity") MicroBusiness entity,
			ParamsEntity paramsEntity, RedirectAttributes redirectAttributes){
		User user = UserUtils.getCurrentUser();
		entity.setCreator(user.getUserName());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		entity.setState(Const.CommonState.ENABLE);
		microBusinessService.save(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return getRedirectUrl(paramsEntity.get("listUrl"));
	}
	/**
	 *  编辑
	 * @param microBusinessId
	 * @param model
	 * @param paramsEntity
	 * @return
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id")Integer microBusinessId,Model model,
			ParamsEntity paramsEntity){
		User user = UserUtils.getCurrentUser();
		MicroBusiness microBusiness = microBusinessService.findOne(microBusinessId);
		microBusiness.setEditor(user.getUserName());
		microBusiness.setEditTime(new Date());
		model.addAttribute("entity",microBusiness);
		bindData(model);
		return VIEW_URI_PREFIX + "form";
	}
	
	private void bindData(Model model) {
		model.addAttribute("microBusinessList",microBusinessService.findByState());
	}
	
	/**
	 *  删除
	 * @param ids
	 * @param response
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
			HttpServletResponse response) {
		String result = "1";
		try {
			microBusinessService.delete(ids);
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
	public MicroBusiness getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return microBusinessService.findOne(id);
		}
		return new MicroBusiness();
	}
	
	/**
	 * 天台微商静态页面列表
	 * @param type
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "micBusinessList", method = RequestMethod.GET)
	public String micBusinessList(Page<MicroBusiness> page, Model model) {
		microBusinessService.searchAll(page);
		model.addAttribute("microBusiness", Const.MICROBUSINESS)
				.addAttribute("page", page);
		return VIEW_URI_PREFIX + "mobile";
	}
}
