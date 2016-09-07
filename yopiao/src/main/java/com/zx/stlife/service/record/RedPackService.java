package com.zx.stlife.service.record;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.base.modules.util.SimpleUtils;
import com.zx.stlife.entity.sys.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.util.ConvertUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.app.base.JsonResultUtils;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.record.RechargeRecord;
import com.zx.stlife.entity.record.RedPack;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.record.RedPackDao;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.DateUtils;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class RedPackService {

	private static Logger logger = LoggerFactory.getLogger(RedPackService.class);

	@Autowired
	private RedPackDao redPackDao;

	@Autowired
	private ConfigService configService;

	@Lazy
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private AccountService accountService;

	public List<RedPack> findCanUseByUser(Integer userId, Byte category) {
		return redPackDao.findCanUseByUser(userId, Const.RedPackState.CAN_USE, category);
	}

	public List<RedPack> findCanUseRedPackByUserAndIds(Integer userId, List<Integer> ids) {
		return redPackDao.findCanUseRedPackByUserAndIds(userId, ids, Const.RedPackState.CAN_USE);
	}

	@Transactional
	public void save(RedPack entity) {
		entity = redPackDao.save(entity);
	}

	/**
	 * 获取红包列表
	 */
	public void findList(Integer userId, Date date, Page<RedPack> page,
			Byte type) {
		Query query = new Query();
		query.table("select s from RedPack s");
		query.eq("s.user.id", userId);
		query.le("s.createTime", date);
		if (type == Const.RedPackState.CAN_USE) {
			query.eq("s.state", type);
		} else {
			query.ne("s.state", Const.RedPackState.CAN_USE);
		}
		query.orderBy("s.createTime desc");
		redPackDao.queryPage(page, query.getQLString(), query.getValues());
	}

	/**
	 * 获取积分抽红包平均值
	 */
	public Double getRedPackAvgValue() {
		return redPackDao.findRedPackAvgValue(Const.RedPackSourceType.FROM_JIFEN);
	}

	/**
	 * 分享抽夺宝红包
	 */
	@Transactional
	public JsonResult drawRedPackByShare(Integer userId) {
		Date currentDate = DateUtils.getNow();
		Config openTimeConfig = configService.get(ConfigService.REDPACK_ACTIVITY_OPENTIME);
		Config endTimeConfig = configService.get(ConfigService.REDPACK_ACTIVITY_ENDTIME);
		if ((openTimeConfig != null) && (endTimeConfig != null)) {
			Date openTime = SimpleUtils.stringToDate(openTimeConfig.getValue());
			if (currentDate.before(openTime)) {
				return JsonResultUtils.buildFailureResult(301); // 活动未开始
			}
		}

		Date startDate = DateUtils.resetDateFields(currentDate, 
				new int[] { Calendar.HOUR_OF_DAY, 0 },
				new int[] { Calendar.MINUTE, 0 },
				new int[] { Calendar.SECOND, 0 },
				new int[] { Calendar.MILLISECOND, 0 });
		
		Date endDate = DateUtils.resetDateFields(currentDate, 
				new int[] { Calendar.HOUR_OF_DAY, 23 },
				new int[] { Calendar.MINUTE, 59 },
				new int[] { Calendar.SECOND, 59 },
				new int[] { Calendar.MILLISECOND, 999 });
		
		RedPack redPack = redPackDao.findRedPackByShare(userId,
				Const.RedPackCategory.SNATCH, startDate, endDate);
		if (redPack != null) {
			return JsonResultUtils.buildFailureResult(302); // 已抽奖,不能重复抽奖
		}

		Integer redPackMoney = 5;
		Integer expireDay = configService.getRedpackExpireDay();
		Date expireTime = DateUtils.addDays(new Date(), expireDay);
		redPack = new RedPack(new User(userId), redPackMoney, redPackMoney,
				Const.RedPackCategory.SNATCH, Const.RedPackSourceType.FROM_SHARE,
				expireTime, Const.RedPackState.CAN_USE);
		save(redPack);

		Map<String, Integer> data = new HashMap<String, Integer>(1);
		data.put("redPackMoney", redPackMoney);
		return JsonResultUtils.buildSuccessResult(data);
	}

	/**
	 * 推荐码抽夺宝红包
	 */
	@Transactional
	public JsonResult drawRedPackByReferral(Integer userId, Integer redPackId) {
		RedPack redPack = redPackDao.findOne(redPackId);
		if (redPack == null) {
			return JsonResultUtils.buildFailureResult(301); // 红包不存在
		}
		if (!userId.equals(redPack.getUser().getId())) {
			return JsonResultUtils.buildFailureResult(302); // 非本人红包,不能抽奖
		}
		if (redPack.getHasDrawed()) {
			return JsonResultUtils.buildFailureResult(303); // 已抽奖,不能抽奖
		}

		Integer redPackMoney = 10;
		redPack.setTotal(redPackMoney);
		redPack.setBalance(redPackMoney);
		redPack.setState(Const.RedPackState.CAN_USE);
		redPack.setHasDrawed(true);
		
		int expireDay = 7;
		Date expireTime = DateUtils.addDays(new Date(), expireDay);
		redPack.setExpireTime(expireTime);
		
		save(redPack);
		
		Member member = memberService.findMember(userId);
		AtomicInteger counter = new AtomicInteger(member.getPrizeAmount());
		member.setPrizeAmount(counter.decrementAndGet());
		memberService.saveMember(member);

		Map<String, Integer> data = new HashMap<String, Integer>(1);
		data.put("redPackMoney", redPackMoney);
		return JsonResultUtils.buildSuccessResult(data);
	}

	/**
	 * 获取生活类红包
	 */
	@Transactional
	public JsonResult drawRedPackByLife(Integer userId) {
		RedPack redPack = redPackDao.findRedPackByLife(userId, Const.RedPackCategory.LIFE,
				Const.RedPackSourceType.FORM_LIEF_SERVICE);
		if (redPack != null) {
			return JsonResultUtils.buildFailureResult(301); // 已领取过生活类红包
		}

		Integer redPackMoney = 10;
		Integer expireDay = configService.getRedpackExpireDay();
		Date expireTime = DateUtils.addDays(new Date(), expireDay);
		redPack = new RedPack(new User(userId), redPackMoney, redPackMoney,
				Const.RedPackCategory.LIFE,
				Const.RedPackSourceType.FORM_LIEF_SERVICE, expireTime,
				Const.RedPackState.CAN_USE);
		save(redPack);
		
		Map<String, Integer> data = new HashMap<String, Integer>(1);
		data.put("redPackMoney", redPackMoney);
		return JsonResultUtils.buildSuccessResult(data);
	}
	
	/**
	 * 后台发红包
	 */
	@Transactional
	public JsonResult drawRedPackByAdmin(Integer userId, Integer redPackValue) {

		Integer redPackMoney = redPackValue;
		Integer expireDay = configService.getRedpackExpireDay();
		Date expireTime = DateUtils.addDays(new Date(), expireDay);
		RedPack redPack = new RedPack(new User(userId), redPackMoney, redPackMoney,
				Const.RedPackCategory.ADMIN,
				Const.RedPackSourceType.FROM_ADMIN, expireTime,
				Const.RedPackState.CAN_USE);
		save(redPack);
		
		Map<String, Integer> data = new HashMap<String, Integer>(1);
		data.put("redPackMoney", redPackMoney);
		return JsonResultUtils.buildSuccessResult(data);
	}

	/**
	 * 老用户推荐获得红包奖励
	 */
	@Transactional
	public void redPackRewardToUser(Integer userId) {
		int referralUseCount = 0;
		List<RedPack> redPackList = redPackDao.findRedPackListByReferral(
				userId, Const.RedPackCategory.SNATCH,
				Const.RedPackSourceType.FORM_RECOMMEND);
		if (redPackList != null) {
			referralUseCount = redPackList.size();
		}
		if (referralUseCount < 10) { // 推荐码只能使用10次
			RedPack redPack = new RedPack(new User(userId), 0, 0,
					Const.RedPackCategory.SNATCH,
					Const.RedPackSourceType.FORM_RECOMMEND, null, null, false);
			save(redPack);

			Member member = memberService.findMember(userId);
			AtomicInteger counter = new AtomicInteger(member.getPrizeAmount());
			member.setPrizeAmount(counter.incrementAndGet());
			memberService.saveMember(member);
		}
	}
	
	/**
	 * 推荐码页面列表
	 */
	public JsonResult referralList(Integer userId) {
		List<RedPack> redPackList = redPackDao.findRedPackListByReferral(
				userId, Const.RedPackCategory.SNATCH,
				Const.RedPackSourceType.FORM_RECOMMEND);
		String referralCode = accountService.getUser(userId).getUserName();

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("referralCode", referralCode);
		data.put("redPackList", ConvertUtils.convertCollectionToListMap(
				redPackList, new String[] { "id", "redPackId" }, new String[] { "total",
						"redPackMoney" }, new String[] { "hasDrawed" }));
		return JsonResultUtils.buildSuccessResult(data);
	}
	
	/**
	 * 模糊查询充值记录
	 * 
	 * @param page
	 * @param params
	 */
	public void search(Page<RedPack> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select t from RedPack t")
		.like("t.user.userName", params.get("userName"))
		.eq("t.category", Const.RedPackCategory.ADMIN)
		.orderBy("t.createTime desc");
		redPackDao.queryPage(page, query.getQLString(), query.getValues());
	}

}
