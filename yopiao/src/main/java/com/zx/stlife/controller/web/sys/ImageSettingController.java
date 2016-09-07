package com.zx.stlife.controller.web.sys;

import com.base.jpa.query.Page;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.sys.ImageSetting;
import com.zx.stlife.service.TmpFileService;
import com.zx.stlife.service.sys.ImageSettingService;
import com.zx.stlife.controller.web.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/sys/imageSetting")
public class ImageSettingController extends BaseController {
	@Autowired
	private ImageSettingService imageSettingService;
	@Autowired
	private TmpFileService tmpFileService;

	private static final String MODULE = "sys:imageSetting";
	private static final String URI_PREFIX = "sys/imageSetting/image-setting-";

	@RequiresPermissions(MODULE + PERMISSION_VIEW)
	@RequestMapping(value = "{category}/list")
	public String list(@PathVariable("category")Byte category,
					   Model model, Page page, ParamsEntity paramsEntity) {
		listContent(category, model, page, paramsEntity);
		return URI_PREFIX + "list";
	}

	@RequiresPermissions(MODULE + PERMISSION_VIEW)
	@RequestMapping(value = "{category}/list-content")
	public String listContent(@PathVariable("category") Byte category,
							  Model model, Page page, ParamsEntity paramsEntity) {
		List<ImageSetting> list = imageSettingService.findAllByCategory(category);
		model.addAttribute("list", list);
		bindData(model, category);
		return URI_PREFIX + "list-content";
	}

	private void bindData(Model model, Byte category) {
		model.addAttribute("category", category)
				.addAttribute("categoryName", Const.ImageCategory.MAP.get(category))
				.addAttribute("imageActionTypeMap", Const.ImageActionType.MAP);
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "{category}/create", method = RequestMethod.GET)
	public String create(@PathVariable("category")Byte category,
						 Model model, ParamsEntity paramsEntity) {
		ImageSetting entity = new ImageSetting();
		entity.setCategory(category);
		model.addAttribute("entity", entity);
		bindData(model, category);
		return URI_PREFIX + "form";
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "{category}/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("category")Byte category,
					   @PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", imageSettingService.get(id));
		bindData(model, category);
		return URI_PREFIX + "form";
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save( @Valid @ModelAttribute("preloadEntity") ImageSetting entity,
					   ParamsEntity paramsEntity, HttpServletRequest request,
					   RedirectAttributes redirectAttributes) {

		Integer tmpFiled = SimpleUtils.stringToInteger(request.getParameter("tmpFileId"));
		imageSettingService.save(entity, tmpFiled);
		redirectAttributes.addFlashAttribute("message", "保存成功");

		return getAfterSaveRedirectUrl(paramsEntity);
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "{category}/delete", method = RequestMethod.DELETE)
	public void delete(@PathVariable("category")Byte category,
					   @RequestParam(value = "ids") List<Integer> ids,
					   HttpServletResponse response) {
		String result = "1";
		try {
			imageSettingService.delete(ids, category);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}

	@RequiresPermissions(MODULE + PERMISSION_EDIT)
	@RequestMapping(value = "deleteImg")
	@ResponseBody
	public int deleteImg(@RequestParam(value = "imgId", required = false)Integer imgId,
						 @RequestParam(value = "tmpFileId", required = false)Integer tmpFileId){
		int result = 0;
		if ( null != imgId) {
			result = imageSettingService.deleteThumbImg(imgId);
		} else if ( null !=tmpFileId ) {
			result = tmpFileService.deleteById(tmpFileId, true);
		}

		return result;
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadEntity")
	public ImageSetting getEntity(@RequestParam(value = "id", required = false) Integer id) {
		if (null != id) {
			return imageSettingService.get(id);
		}
		return new ImageSetting();
	}
}
