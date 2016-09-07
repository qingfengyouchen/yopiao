package com.zx.stlife.service.record;

import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.query.Page;
import com.zx.stlife.constant.Const;
import com.zx.stlife.constant.Const.RedPackSourceType;
import com.zx.stlife.constant.Const.RedPackState;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.record.Jifen;
import com.zx.stlife.entity.record.RedPack;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.record.JifenDao;
import com.zx.stlife.service.member.MemberService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.DateUtils;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class JifenService {

	private static Logger logger = LoggerFactory.getLogger(JifenService.class);

	@Autowired
	private JifenDao jifenDao;

	@Lazy
	@Autowired
	private MemberService memberService;

	@Autowired
	private RedPackService redPackService;
	
	@Autowired
	private ConfigService configService;

	public Jifen get(Integer id) {
		return jifenDao.findOne(id);
	}

	@Transactional
	public void save(Jifen entity) {
		entity = jifenDao.save(entity);
	}

	/**
	 * 查询用户积分记录
	 * 
	 * @param page
	 * @param date
	 * @param userId
	 */
	public void findPageByUser(Page<Jifen> page, Date date, Integer userId) {
		String hql = "select t from Jifen t where t.user.id=?1 and t.createTime<=?2 and t.state=?3 order by t.createTime desc";
		jifenDao.queryPage(page, hql, userId, date, Const.CommonState.ENABLE);
	}

	/**
	 * 查看用户积分
	 * 
	 * @param userId
	 */
	public Integer findUserJifen(Integer userId) {
		Member member = memberService.findMember(userId);
		if (member != null) {
			return member.getJifen();
		}
		return null;
	}

	/**
	 * 积分抽红包
	 * 
	 * @param userId
	 */
	@Transactional
	public Integer drawRedPack(Integer userId) {
		Member member = memberService.findMember(userId);
		Integer totalJifen = member.getJifen();
		Integer amount = 100; // 每次所需100积分
		if (totalJifen == null || totalJifen < amount) {
			return null;
		} else {
			Integer redPackMoney = drawRedPackRule();
			if (redPackMoney > 0) {
				Integer expireDay = configService.getRedpackExpireDay();
		    	Date expireTime = DateUtils.addDays(new Date(), expireDay);
				RedPack redPack = new RedPack(new User(userId),
						redPackMoney, redPackMoney, Const.RedPackCategory.SNATCH,
						RedPackSourceType.FROM_JIFEN, expireTime, RedPackState.CAN_USE);
				redPackService.save(redPack);
			}
			member.subtractJifen(amount);
			memberService.saveMember(member);

			String descr = "积分抽红包支付";
			Jifen jifen = new Jifen(member, amount, descr, new User(userId));
			jifenDao.save(jifen);

			return redPackMoney;
		}
	}

	/**
	 * 积分抽红包规则
	 * 
	 * @return
	 */
	private Integer drawRedPackRule() {
		int minRedPack = 1, maxRedPack = 10, avgRedPack = 5;
		Double avgTotal = redPackService.getRedPackAvgValue();
		if (avgTotal == null) {
			return RandomUtils.nextInt(minRedPack, maxRedPack);
		}
		int avgValue = avgTotal.intValue();
		if (avgValue >= avgRedPack) {
			maxRedPack = avgRedPack;
		}
		return RandomUtils.nextInt(minRedPack, maxRedPack);
	}
	
	/**
	 * 积分兑换
	 * 
	 * @return
	 */
	@Transactional
	public Integer jifenExchange(Integer dhjifen,Integer userId)
	{
		Member member = memberService.findMember(userId);
		Integer totalJifen = member.getJifen();
		if (totalJifen == null || totalJifen < dhjifen ) {
			return null;
		} else {
			int balance = dhjifen/100;			
			member.subtractJifen(dhjifen);
			member.addBalance(balance);
			memberService.saveMember(member);
			String descr = "积分兑换支付";
			Integer amount=dhjifen;
			Jifen jifen = new Jifen(member, amount, descr, new User(userId));
			jifenDao.save(jifen);
			Integer newbalance= memberService.findMember(userId).getBalance();
			return newbalance;
		}

	}
	
	
	/**
	 * 积分提现
	 * 
	 * @return
	 */
	@Transactional
	public Integer jifenWithdrawals(Integer dhjifen,Integer userId)
	{
		Member member = memberService.findMember(userId);
		Integer totalJifen = member.getJifen();
		if (totalJifen == null || totalJifen < dhjifen ) {
			return null;
		} else {		
			member.subtractJifen(dhjifen);
			memberService.saveMember(member);
			String descr = "积分提现支付";
			Integer amount=dhjifen;
			Jifen jifen = new Jifen(member, amount, descr, new User(userId));
			jifenDao.save(jifen);
			totalJifen= memberService.findMember(userId).getJifen();
			return totalJifen;
		}

	}
	
	
	
}
