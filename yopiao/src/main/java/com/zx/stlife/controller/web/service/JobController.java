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
import com.zx.stlife.entity.service.FlowerArtist;
import com.zx.stlife.entity.service.Job;
import com.zx.stlife.service.service.JobService;

/**
 * 
 * 求职工作 controller
 * @author lxw
 *
 */

@Controller
@RequestMapping(value="/service/job")
public class JobController extends BaseController<Job>{


	private static final String JOB = "service:job";
	private static final String URI_PREFIX = "service/job/job-";
	
	@Autowired
	private JobService jobService;
	
	/**
	 * 查看
	 */
	@RequiresPermissions(JOB + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page<Job> page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		fillBaseData(model);
		return URI_PREFIX + "list";
	}
	
	private void fillBaseData(Model model) {
		model.addAttribute("genderMap", Const.Gender.genderMap);
	}
	
	/**
	 * 搜索
	 * @return
	 */
	@RequiresPermissions(JOB + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<Job> page, ParamsEntity paramsEntity) {
		jobService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		fillBaseData(model);
		return URI_PREFIX + "list-content";
	}
	
	
	/**
	 * 创建
	 */
	@RequiresPermissions(JOB + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new Job());
		fillBaseData(model);
		return URI_PREFIX + "form";
	}
	
	/**
	 * 保存
	 * @return
	 */
	@RequiresPermissions(JOB + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") Job entity,ParamsEntity paramsEntity,RedirectAttributes redirectAttributes) {
		jobService.save(entity);
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
	@RequiresPermissions(JOB + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", jobService.getByNotice(id));
		fillBaseData(model);
		return URI_PREFIX + "form";
	}
	
	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadEntity")
	public Job getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if ( null != id ) {
			return jobService.getByNotice(id);
		}
		return new Job();
	}
	
	/**
	 * 删除
	 * @param ids
	 * @param response
	 */
	@RequiresPermissions(JOB + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,HttpServletResponse response) {
		String result = "1";
		try {
			jobService.delete(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}
	
	@RequestMapping(value="selListJob",method=RequestMethod.GET)
	public String selListActivity(Model model){
		List<Job> jobs=jobService.selListJob();
		model.addAttribute("jobs", jobs).addAttribute("nodeJob", Const.NODE_Job).addAttribute("gender", Const.Gender.genderMap);
		return URI_PREFIX+"mobile";
	}
}
