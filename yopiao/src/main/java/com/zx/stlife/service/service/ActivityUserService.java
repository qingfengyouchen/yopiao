package com.zx.stlife.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zx.stlife.base.UserUtils;
import com.zx.stlife.entity.service.Activity;
import com.zx.stlife.entity.service.ActivityUser;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.service.ActivityUserDao;

/**
 * 天台活动参与用户
 * @author LXW
 *
 */
@Service
@Transactional(readOnly = true)
public class ActivityUserService {

	@Autowired
	private ActivityUserDao activityUserDao;
	
	public ActivityUser getOne(Integer id){
		ActivityUser  activityUser=activityUserDao.getOne(id);
		return activityUser;
	}
	
	public ActivityUser findMobileNoActivityUser(String mobileNo){
		
		return activityUserDao.findMobileNoActivityUser(mobileNo);
	}
	
	@Transactional
	public void saveEntity(Integer id,ActivityUser entity,User user){
		entity.setUser(user);
		Activity activity=new Activity();
		activity.setId(id);
		entity.setActivity(activity);
		activityUserDao.save(entity);
	}
}
