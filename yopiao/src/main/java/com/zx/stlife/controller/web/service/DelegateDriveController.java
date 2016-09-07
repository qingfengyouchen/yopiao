package com.zx.stlife.controller.web.service;

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
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.service.DelegateDrive;
import com.zx.stlife.entity.service.FlowerArtist;
import com.zx.stlife.service.service.DelegateDriveService;

/**
 * 
 * 顺风代驾 controller
 * @author lxw
 *
 */

@Controller
@RequestMapping(value="/service/delegateDrive")
public class DelegateDriveController extends BaseController<DelegateDrive>{


	private static final String DELEGATEDRIVE = "service:delegateDrive";
	private static final String URI_PREFIX = "service/delegateDrive/delegate-drive-";
	
	@Autowired
	private DelegateDriveService delegateDriveService;
	
	/**
	 * 查看
	 */
	@RequiresPermissions(DELEGATEDRIVE + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page<DelegateDrive> page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return URI_PREFIX + "list";
	}
	
	
	/**
	 * 搜索
	 * @return
	 */
	@RequiresPermissions(DELEGATEDRIVE + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<DelegateDrive> page, ParamsEntity paramsEntity) {
		delegateDriveService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		return URI_PREFIX + "list-content";
	}
	
	
	/**
	 * 创建
	 */
	@RequiresPermissions(DELEGATEDRIVE + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new DelegateDrive());
		return URI_PREFIX + "form";
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequiresPermissions(DELEGATEDRIVE + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") DelegateDrive entity,ParamsEntity paramsEntity,RedirectAttributes redirectAttributes) {
		delegateDriveService.save(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return getAfterSaveRedirectUrl(paramsEntity);
	}
	
	
	/**
	 * 修改
	 * @param id 
	 * @param model
	 * @param paramsEntity
	 * @return
	 */
	@RequiresPermissions(DELEGATEDRIVE + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", delegateDriveService.getByNotice(id));
		return URI_PREFIX + "form";
	}
	
	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadEntity")
	public DelegateDrive getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return delegateDriveService.getByNotice(id);
		}
		return new DelegateDrive();
	}
	
	/**
	 * 删除
	 * @param ids
	 * @param response
	 */
	@RequiresPermissions(DELEGATEDRIVE + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,HttpServletResponse response) {
		String result = "1";
		try {
			delegateDriveService.delete(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}
	/**
	 * 手机查询
	 */
	@RequestMapping(value="selListDelegateDrive",method=RequestMethod.GET)
	public String selListActivity(Model model){
		List<DelegateDrive> drives=delegateDriveService.selListDelegateDrive();
		model.addAttribute("drives", drives).addAttribute("nodeDelegatedrive", Const.NODE_DelegateDrive);
		return URI_PREFIX+"mobile";
	}
}
