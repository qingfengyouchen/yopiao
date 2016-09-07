package com.zx.stlife.controller.web.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
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
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.notice.Notice;
import com.zx.stlife.entity.notice.NoticeSendRecord;
import com.zx.stlife.entity.sys.Config;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.member.MemberLevelService;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.notice.NoticeService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.jpush.JPushUtils;

/**
 *消息管理
 */
@Controller
@RequestMapping(value="/notice/noticeMessage")
public class NoticeMessageController extends BaseController {

	private static final String NOTICE = "notice:notice";
	private static final String PERMISSION_PREFIX = "notice:noticeMessage";
	private static final String VIEW_URI_PREFIX = "notice/member-";

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberLevelService memberLevelService;
	@Autowired
	private ConfigService configService;
	/**
	 * 查看
	 */
	@RequiresPermissions(value = {"notice:noticeMessage:view", "notice:noticeWithdraw:view", "notice:noticeOnlySend:view"}, logical = Logical.OR)
	@RequestMapping(value = "list")
	public String list(Model model, Page page, ParamsEntity paramsEntity) {
		listContent(model, page, paramsEntity);
		return VIEW_URI_PREFIX + "list";
	}
	
	@RequiresPermissions(value = {"notice:noticeMessage:view", "notice:noticeWithdraw:view", "notice:noticeOnlySend:view"}, logical = Logical.OR)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page page, ParamsEntity paramsEntity) {
		memberService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page);
		return VIEW_URI_PREFIX + "list-content";
	}
	
	@RequiresPermissions(value = {"notice:noticeMessage:view", "notice:noticeWithdraw:view", "notice:noticeOnlySend:view"}, logical = Logical.OR)
	@RequestMapping(value = "view/{id}",method = RequestMethod.GET)
	public String view(@PathVariable("id") Integer id,Model model, Page page,ParamsEntity paramsEntity){
		Map<String,String> params= new HashMap<String,String>(1);
		params.put("id", id.toString());
		params.put("createTime", "");
		paramsEntity.setParams(params);;
		listWithraw(model,page,paramsEntity);
		return VIEW_URI_PREFIX + "withdraw-list";
	}
	
	
	@RequiresPermissions("notice:noticeWithdraw:view")
	@RequestMapping(value = "list-withdraw")
	public String listWithraw(Model model, Page page, ParamsEntity paramsEntity) {
		
		memberService.searchWithdraw(page, paramsEntity);
		int fee = 5;
		Config config = configService.get(ConfigService.WITHDRAW_FEE);
		if (config != null && StringUtils.isNotEmpty(config.getValue())) {
			fee = Integer.parseInt(config.getValue());
		}
		double feeFloat = (100.00-fee)/100;
		model.addAttribute("page", page);
		model.addAttribute("withDrawFee", feeFloat);
		bindModel(model);
		return VIEW_URI_PREFIX + "withdraw";
	}
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "edit/{id}",method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id,Model model, Page page,ParamsEntity paramsEntity){
		Map<String,String> params= new HashMap<String,String>(1);
		params.put("id", id.toString());
		params.put("createTime", "");
		paramsEntity.setParams(params);;
		listMessage(model,page,paramsEntity);
		return VIEW_URI_PREFIX + "message-list";
	}
	
	@RequiresPermissions("notice:noticeOnlySend:view")
	@RequestMapping(value = "view1/{id}", method = RequestMethod.GET)
	public String create(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		Member member =memberService.get(id);
		Notice notice=new Notice();
		notice.setId(id);
		notice.setSenderName(member.getUser().getUserName());
		model.addAttribute("entity", notice);
		return "notice/notice-onlysend";
	}
	
	
	@SuppressWarnings("null")
	@RequiresPermissions("notice:noticeOnlySend:view")
	@RequestMapping(value = "sendMessage", method = RequestMethod.POST)
	public String sendMessage(@Valid @ModelAttribute("preloadEntity") Notice entity,ParamsEntity paramsEntity,RedirectAttributes redirectAttributes) {
		User user = UserUtils.getCurrentUser();
		Integer touserid=entity.getId();
		
		Member member=memberService.get(touserid);
		User userTmp =new User();
		userTmp.setId(member.getUser().getId());
		
		entity.setId(null);
		entity.setUser(userTmp);
		entity.setSender(user);
		entity.setSenderName(user.getName());
		entity.setState(Const.CommonState.ENABLE);
		noticeService.save(entity);
		NoticeSendRecord noticeSendRecord=new NoticeSendRecord();
		noticeSendRecord.setNotice(entity);

		Integer userid = member.getUser().getId();
		noticeSendRecord.setToUser(userTmp);
		noticeService.saveNoticeSendRecord(noticeSendRecord);
		List<String> alias=new ArrayList<String>(1);
		//alias.add(touserid.toString());
		alias.add(userid.toString());
		 Map<String, String> extras=new HashMap<String,String>(2);
		 String message = "您的userid为" + userid.toString() + "，您的消息为：";
		 extras.put("title", entity.getTitle());
		 extras.put("content", message + entity.getContent());
		 JPushUtils.sendPushNotificationNotice(alias, extras);
		 redirectAttributes.addFlashAttribute("message", "发送成功");
		return getAfterSaveRedirectUrl(paramsEntity);
	}
	
	public void sendMessageByAdmin(Integer id) {
		User user = UserUtils.getCurrentUser();
		Notice entity = new Notice();
		Integer touserid=noticeService.getMemberId(id);
		Member member=memberService.get(touserid);
		User userTmp =new User();
		userTmp.setId(member.getUser().getId());
		
		entity.setId(null);
		entity.setUser(userTmp);
		entity.setSender(user);
		entity.setSenderName(user.getName());
		entity.setState(Const.CommonState.ENABLE);
		entity.setTitle("您的提现已处理！");
		entity.setContent("您的提现申请已处理，资金将很快到账，"
		 		+ "请注意查收！（一般情况2小时内到账，"
		 		+ "若您的提现24小时未到账，请及时与我们联系）");
		noticeService.save(entity);
		NoticeSendRecord noticeSendRecord=new NoticeSendRecord();
		noticeSendRecord.setNotice(entity);

		Integer userid = member.getUser().getId();
		noticeSendRecord.setToUser(userTmp);
		noticeService.saveNoticeSendRecord(noticeSendRecord);
		List<String> alias=new ArrayList<String>(1);
		alias.add(userid.toString());
		Map<String, String> extras=new HashMap<String,String>(2);
		extras.put("title", "");
		extras.put("content", "");
		JPushUtils.sendPushNotificationNotice(alias, extras);
	}
	
	
	@RequiresPermissions(PERMISSION_PREFIX + PERMISSION_VIEW)
	@RequestMapping(value = "list-message")
	public String listMessage(Model model, Page page, ParamsEntity paramsEntity) {
		
		memberService.searchMessage(page, paramsEntity);
		model.addAttribute("page", page);
		bindModel(model);
		return VIEW_URI_PREFIX + "message";
	}
	
	
	/**
	 * 修改留言已读状态
	 * @param ids
	 * @param response
	 */
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,
			@RequestParam(value = "type") String type,
			HttpServletResponse response) {
		String result = "已读";
		for (Integer id : ids) {
			try {
				if (StringUtils.isNotEmpty(type) && "1".equals(type)) {
					noticeService.updateIsDone(id);
					sendMessageByAdmin(id);
					result = "已处理";
				} else {
					noticeService.updateIsRead(id);
				}
			}catch (Exception ex){
				result = ex.getMessage();
				logger.error(ex.getMessage(), ex);
			}
		}
		WebUtils.renderText(response, result);
	}
	
	public void bindModel(Model model){
		model.addAttribute("stateMap",Const.commonStateMap);
		model.addAttribute("genderMap",Const.Gender.genderMap);
		model.addAttribute("booleanMap",Const.booleanMap);
		model.addAttribute("memberLevel",memberLevelService.findAll());
	}
	
}
