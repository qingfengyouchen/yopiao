package com.zx.stlife.controller.web.service;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.base.jpa.query.Page;
import com.base.modules.util.WebUtils;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.service.Activity;
import com.zx.stlife.entity.service.ActivityUser;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.service.ActivityService;
import com.zx.stlife.service.service.ActivityUserService;

/**
 * 天台活动controller
 *@author LXW
 */
@Controller
@RequestMapping(value="/service/activity")
public class ActivityController extends BaseController<Activity>{

	private static final String ACTIVITY = "service:activity";
	private static final String URI_PREFIX = "service/activity/activity-";

	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivityUserService activityUserService;
	/**
	 * 查看
	 */
	@RequiresPermissions(ACTIVITY + PERMISSION_VIEW)
	@RequestMapping(value = "list")
	public String list(Model model, Page<Activity> page, ParamsEntity paramsEntity) {
		Map<String,String> params=new HashMap<String,String>(1);
		params.put("type", "1");
		paramsEntity.setParams(params);
		listContent(model, page, paramsEntity);
		return URI_PREFIX + "list";
	}
	
	@RequiresPermissions(ACTIVITY + PERMISSION_VIEW)
	@RequestMapping(value = "list-content")
	public String listContent(Model model, Page<Activity> page, ParamsEntity paramsEntity) {
		activityService.search(page, paramsEntity.getParams());
		model.addAttribute("page", page).addAttribute("activityState", Const.ActivityState.MAP);
		return URI_PREFIX + "list-content";
	}
	
	/**
	 * 
	 */
	@RequiresPermissions(ACTIVITY + PERMISSION_EDIT)
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", new Activity());
		return URI_PREFIX + "form";
	}
	
	@RequiresPermissions(ACTIVITY + PERMISSION_EDIT)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("preloadEntity") Activity entity,ParamsEntity paramsEntity,RedirectAttributes redirectAttributes,Integer thumbImgUrlId,Integer imgUrlId) {

		activityService.save(entity,thumbImgUrlId,imgUrlId);
		redirectAttributes.addFlashAttribute("message", "保存成功");
		return getAfterSaveRedirectUrl(paramsEntity);
	}
	
	@RequiresPermissions(ACTIVITY + PERMISSION_EDIT)
	@RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Integer id, Model model, ParamsEntity paramsEntity) {
		model.addAttribute("entity", activityService.getByNotice(id));
		return URI_PREFIX + "form";
	}
	
	/**
	 * 删除
	 * @param ids
	 * @param response
	 */
	@RequiresPermissions(ACTIVITY + PERMISSION_EDIT)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public void delete(@RequestParam(value = "ids") List<Integer> ids,HttpServletResponse response) {
		String result = "1";
		try {
			activityService.delete(ids);
		}catch (Exception ex){
			result = ex.getMessage();
			logger.error(ex.getMessage(), ex);
		}
		WebUtils.renderText(response, result);
	}
	
	@RequiresPermissions(ACTIVITY + PERMISSION_EDIT)
	@RequestMapping(value="update",method=RequestMethod.POST)
	public @ResponseBody Integer updateState(@RequestParam("id") Integer id){
		Integer result = null;
		try {
			activityService.updateState(id);
			result = Const.COMMON_RESULT_SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = 0;
		}
		return result;
	}
	
	@ModelAttribute("preloadEntity")
	public Activity getEntity(@RequestParam(value = "id",required = false) Integer id){
		if ( null != id ) {
			return activityService.getByNotice(id);
		}
		return new Activity();
	}

	@RequestMapping(value="selListActivity",method=RequestMethod.GET)
	public String selListActivity(Model model){
		List<Activity> activities=activityService.selListActivity();
		model.addAttribute("activities", activities).addAttribute("nodeActivity",Const.NODE_ACTIVITY)
		.addAttribute("activityRootUri", Const.ACTIVITY_IMG_ROOT_URL);
		return URI_PREFIX+"mobile";
	}
	
	/**
	 * 获取以报名人数  
	 */
	@RequestMapping(value="hasJoinAmount",method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> hasJoinAmount(Integer id){
		Map<String, Object> map =new HashMap<String, Object>();
		try {
			Activity activity=activityService.getByNotice(id);
			if(activity.getState()==Const.ActivityState.ENABLE){
				if(activity.getMaxJoinAmount()>activity.getHasJoinAmount()){
					map.put("state", 1);
					map.put("result", activity.getHasJoinAmount());
				}else{
					map.put("state", 3);
					map.put("result","人数已报满");
				}
			}else{
				map.put("state", 2);
				map.put("result", "活动已结束");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("state", 0);
			map.put("result", "无法参加活动");
		}
		return map;
	}
	
	/**
	 * 活动报名
	 */
	@RequestMapping(value="updateAmount/{id}",method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateAmount(@PathVariable("id")Integer id,ActivityUser activityUser){
		Map<String, Object> map =new HashMap<String, Object>();
		try {
			Activity activity=activityService.getByNotice(id);
			if(activity.getState()==Const.ActivityState.ENABLE){
				if(activity.getMaxJoinAmount()>activity.getHasJoinAmount()){
					 ActivityUser act=activityUserService.findMobileNoActivityUser(activityUser.getMobileNo());
					 if(act!=null){
						 map.put("state", 4);
						 map.put("result", "这场活动您已过报名了,不用在报");
					 }else{
						 User user = UserUtils.getCurrentUser();
						 boolean isModify = user.hasId();
						 if(isModify){
							 activityUserService.saveEntity(id,activityUser,user);
							 activityService.updateHasJoinAmount(id, activity.getHasJoinAmount()+1);
							 map.put("state", 1);
							 map.put("result", "报名成功");
						 }else{
								map.put("state", 5);
								map.put("result","亲！您还没有登录");
						 }
					 }
				}else{
					map.put("state", 3);
					map.put("result","人数已报满了");
				}
			}else{
				map.put("state", 2);
				map.put("result", "活动已结束");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			map.put("state", 0);
			map.put("result", "无法参加活动");
		}
		return map;
	} 
	
	@RequestMapping(value="enroll/{id}",method=RequestMethod.GET)
	public String enroll(@PathVariable("id") Integer id,Model model){
		model.addAttribute("id", id);
		return URI_PREFIX + "enroll";
	}
	
	@RequestMapping(value="delThumbImgUrl",method=RequestMethod.POST)
	public @ResponseBody Integer delThumbImgUrl(Integer id){
		Integer result=null;
		try {
			activityService.delThumbImgUrl(id);
			result = Const.COMMON_RESULT_SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = 0;
		}
		return result;
	}
	
	@RequestMapping(value="delImgUrl",method=RequestMethod.POST)
	public @ResponseBody Integer delImgUrl(@RequestParam("id")Integer id){
		Integer result=null;
		try {
			activityService.delImgUrl(id);
			result = Const.COMMON_RESULT_SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = 0;
		}
		return result;
	}
}

