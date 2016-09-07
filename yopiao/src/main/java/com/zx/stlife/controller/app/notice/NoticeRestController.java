package com.zx.stlife.controller.app.notice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.DateUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.member.MemberMessage;
import com.zx.stlife.entity.notice.Notice;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.notice.NoticeService;

/**
 * 通知服务API
 */
@RestController
@RequestMapping("/app/notice")
public class NoticeRestController extends BaseRestController{

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private MemberService memberService;
	/**
	 * 查看通知列表
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public JsonResult getNotice(@RequestParam(value = "timestamp", required = false) String timestamp,Page<Notice> page){
		Date date = DateUtilsEx.stringToDate(timestamp, DateUtilsEx.FORMAT_YYYYMMDDHHMMSS_SSS);
		try {
			noticeService.getAllNotice(date,Const.CommonState.ENABLE,page);
			if(SimpleUtils.isNullList(page.getResult())){
				return buildSuccessResult();
			}
			Map<String, Object> data= ConvertUtils.convertEntityToMap(page,"pageNo","totalPages","totalCount");
			List<Map<String, Object>>  result= ConvertUtils.convertCollectionToListMap(page.getResult(),"id","title","content","createTimeStr");
			data.put("result", result);
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 查看通知列表
	 */
	@RequestMapping(value="/listMessage",method=RequestMethod.GET)
	public JsonResult getNoticeMessage(
			@RequestParam(value = "timestamp", required = false) String timestamp,
			@RequestParam(value = "userId", required = false) Integer userId,
			Page<Notice> page){
		Date date = DateUtilsEx.stringToDate(timestamp, DateUtilsEx.FORMAT_YYYYMMDDHHMMSS_SSS);
		try {
			List<Integer> useridList = new ArrayList<Integer>();
			useridList.add(userId);
			//useridList.add(new Integer(0));
			noticeService.getAllNoticeMessage(useridList, date,Const.CommonState.ENABLE,page);
			if(SimpleUtils.isNullList(page.getResult())){
				return buildSuccessResult();
			}
			Map<String, Object> data= ConvertUtils.convertEntityToMap(page,"pageNo","totalPages","totalCount");
			List<Map<String, Object>>  result= ConvertUtils.convertCollectionToListMap(page.getResult(),"id","title","content","createTimeStr");
			data.put("result", result);
			return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 删除通知列表
	 */
	@RequestMapping(value="/delMessage",method=RequestMethod.GET)
	public JsonResult delNoticeMessage(
			@RequestParam(value = "id") Integer id,
			@RequestParam(value = "userId", required = false) Integer userId){
		try {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(id);			
			noticeService.delete(ids);
			
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("noticeid", id.toString());
            data.put("state", noticeService.getByNotice(id).getState().toString());
            data.put("result", "OK");
            return buildSuccessResult(data);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * 查看通知详情
	 * @param noticeId 
	 * @return
	 */
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public JsonResult getByNotice(@PathVariable("id")Integer noticeId){
		try {
			Notice notice=noticeService.getByNotice(noticeId);
			if(notice==null){
				return buildSuccessResult();
			}
			Map<String, Object> map =ConvertUtils.convertEntityToMap(notice,"id","title","content","createTimeStr");
			return buildSuccessResult(map);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return buildFailureResult();
		}
	}
	
	/**
	 * created by superT 2016/5/9
	 * 保存客服记录
	 */
	@RequestMapping(value="/saveMessage",method=RequestMethod.GET)
	public JsonResult saveMessage(@RequestParam("userId") Integer userId,@RequestParam("message") String message) {
		try {
				Member member = memberService.findMember(userId);
				//app客户端需要先做一次url的encode处理
				message = URLDecoder.decode(message, "UTF-8");
				MemberMessage memberMessage=new MemberMessage(member,message);
				noticeService.saveMessage(memberMessage);
				System.out.println("UserId : " + String.valueOf(userId));
				System.out.println("Message : " + message);
				System.out.println("Decode Message : " + URLDecoder.decode(message, "UTF-8"));				
				return buildSuccessResult();
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				return buildFailureResult();
			}
	}
}
