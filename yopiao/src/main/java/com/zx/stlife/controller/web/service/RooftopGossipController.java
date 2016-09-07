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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.base.jpa.query.Page;
import com.base.modules.util.Encodes;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.service.MicroBusiness;
import com.zx.stlife.entity.service.RooftopGossip;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.service.RooftopGossipService;

/**
 * 天台八卦
 * 
 * @author fan
 *
 */
@Controller
@RequestMapping("/service/rooftopGossip")
public class RooftopGossipController extends BaseController {

	@Autowired
	private RooftopGossipService service;

	private static final String PERMISSION_PREFIX = "service:rooftopGossip";
	private static final String VIEW_URI_PREFIX = "service/rooftopGossip/rooftop-gossip-";

	/**
	 * 查看
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page<RooftopGossip> page,
			ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}

	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<RooftopGossip> page,
			ParamsEntity paramsEntity) {
		service.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		return VIEW_URI_PREFIX + "list-content";
	}

	/**
	 * 创建
	 */
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new MicroBusiness());
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
			@Valid @ModelAttribute("preloadEntity") RooftopGossip entity,
			ParamsEntity paramsEntity, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getCurrentUser();
		entity.setCreator(user.getUserName());
		entity.setContent(Encodes.unescapeHtml(entity.getContent()));
		entity.setState(Const.CommonState.ENABLE);
		service.save(entity);
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
	public String edit(@PathVariable("id") Integer id, Model model,
			ParamsEntity paramsEntity) {
		RooftopGossip rooftopGossip = service.findOne(id);
		User user = UserUtils.getCurrentUser();
		rooftopGossip.setEditor(user.getUserName());
		rooftopGossip.setEditTime(new Date());
		model.addAttribute("entity", rooftopGossip);
		bindData(model);
		return VIEW_URI_PREFIX + "form";
	}

	private void bindData(Model model) {
		model.addAttribute("rooftopGossipList", service.findByState());
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
			service.delete(ids);
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
	public RooftopGossip getEntity(
			@RequestParam(value = "id", required = false) Integer id) {
		if (null != id) {
			return service.findOne(id);
		}
		return new RooftopGossip();
	}
	
	/**
	 * 天台八卦静态页面列表
	 * @param type
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "rooftopGossipList", method = RequestMethod.GET)
	public String rooftopGossipList(Page<RooftopGossip> page, Model model) {
		service.searchAll(page);
		model.addAttribute("rooftopGossip", Const.ROOFTOPGOSSIP)
				.addAttribute("page", page);
		return VIEW_URI_PREFIX + "mobile";
	}
}
