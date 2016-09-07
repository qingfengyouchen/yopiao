package com.zx.stlife.controller.web.notice;

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
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.notice.Notice;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.notice.NoticeService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.tools.thread.ThreadWorkUtils;

/**
 *消息管理
 */
@Controller
@RequestMapping(value="/notice/notice")
public class NoticeController extends BaseController<Notice> {

	
	private static final String NOTICE = "notice:notice";
	private static final String URI_PREFIX = "notice/notice-";

	@Autowired
	private NoticeService noticeService;
	@Autowired
	private AccountService accountService;
	
	/**
	 * 查看
	 */
	@RequiresPermissions(NOTICE + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page<Notice> page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return URI_PREFIX + "list";
	}
	
	@RequiresPermissions(NOTICE + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<Notice> page, ParamsEntity paramsEntity) {
		noticeService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		return URI_PREFIX + "list-content";
	}
	
	/**
	 * 
	 */
	@RequiresPermissions(NOTICE + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new Notice());
		return URI_PREFIX + "form";
	}
	
	@RequiresPermissions(NOTICE + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") Notice entity,ParamsEntity paramsEntity,RedirectAttributes redirectAttributes) {
		User user = UserUtils.getCurrentUser();
		entity.setSender(user);
		
		//群发消息，给所有用户都插入一条记录
		Integer maxId = accountService.getMaxId();
		ThreadWorkUtils.addNoticeToAllUser(maxId, entity, user);
		User tmp = new User();
		tmp.setId(new Integer(0));
		entity.setUser(tmp);
		entity.setSenderName(user.getName());
		entity.setState(Const.CommonState.ENABLE);
		noticeService.save(entity);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return getAfterSaveRedirectUrl(paramsEntity);
	}
	
	@RequiresPermissions(NOTICE + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", noticeService.getByNotice(id));
		return URI_PREFIX + "form";
	}
	
	/**
	 * 删除
	 * @param ids
	 * @param response
	 */
	@RequiresPermissions(NOTICE + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,HttpServletResponse response) {
		String result = "1";
		try {
			noticeService.delete(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}
	
	@ModelAttribute("preloadEntity")
	public Notice getEntity(@RequestParam(value = "id",required = false) Integer id){
		if ( null != id ) {
			return noticeService.getByNotice(id);
		}
		return new Notice();
	}
	
}
