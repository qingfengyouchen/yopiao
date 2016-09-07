package com.zx.stlife.controller.web.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.service.member.MemberLevelService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.record.RechargeRecordService;
import com.zx.stlife.service.record.RedPackService;

@Controller
@RequestMapping(value= "/member/member")
public class MemberController extends BaseController {
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberLevelService memberLevelService;
	
	@Autowired
	private RechargeRecordService rechargeRecordService;
	
	@Autowired
	private RedPackService redpackService;
	
	private static final String PERMISSION_PREFIX = "member:member";
	private static final String VIEW_URI_PREFIX = "member/member/member-";
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page page, ParamsEntity paramsEntity){
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "recharge")
	public String recharge(Model model, Page page, ParamsEntity paramsEntity){
		rechargeContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "recharge";
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "redpack")
	public String redpack(Model model, Page page, ParamsEntity paramsEntity){
		redpackContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "redpack";
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page page, ParamsEntity paramsEntity) {
		memberService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		bindModel(model);
		return VIEW_URI_PREFIX + "list-content";
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "recharge-content")
	public String rechargeContent(Model model, Page page, ParamsEntity paramsEntity) {
		rechargeRecordService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		bindModel(model);
		return VIEW_URI_PREFIX + "recharge-content";
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "redpack-content")
	public String redpackContent(Model model, Page page, ParamsEntity paramsEntity) {
		redpackService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		bindModel(model);
		return VIEW_URI_PREFIX + "redpack-content";
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}",method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id,Model model,ParamsEntity paramsEntity){
		model.addAttribute("entity",memberService.get(id));
		bindModel(model);
		return VIEW_URI_PREFIX + "form";
	}
	
	/*@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "view/{id}",method = RequestMethod.GET)
	public String view(@PathVariable("id") Integer id,Model model, Page page,ParamsEntity paramsEntity){
		Map<String,String> params= new HashMap<String,String>(1);
		params.put("id", id.toString());
		params.put("createTime", "");
		paramsEntity.setParams(params);;
		listWithraw(model,page,paramsEntity);
		return VIEW_URI_PREFIX + "withdraw-list";
	}
	
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-withdraw")
	public String listWithraw(Model model, Page page, ParamsEntity paramsEntity) {
		
		memberService.searchWithdraw(page, paramsEntity);
		model.addAttribute("page", page);
		bindModel(model);
		return VIEW_URI_PREFIX + "withdraw";
	}*/
	
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") Member entity,
			ParamsEntity paramsEntity, RedirectAttributes redirectAttributes) {
		entity.setEditTime(new Date());
		entity.setTrueName(entity.getUser().getTrueName());
		memberService.saveMember(entity);
		
		redirectAttributes.addFlashAttribute("message", "保存会员信息成功");

		return getRedirectUrl(paramsEntity.get("listUrl"));
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
			   HttpServletResponse response) {
		String result = "1";
		try {
			memberService.delete(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}
	
	public void bindModel(Model model){
		model.addAttribute("stateMap",Const.commonStateMap);
		model.addAttribute("genderMap",Const.Gender.genderMap);
		model.addAttribute("booleanMap",Const.booleanMap);
		model.addAttribute("memberLevel",memberLevelService.findAll());
	}
	
	
	
	
	@ModelAttribute("preloadEntity")
	public Member getEntity(@RequestParam(value = "id",required = false) Integer id){
		if ( null != id ) {
			return memberService.get(id);
		}
		return new Member();
	}
}
