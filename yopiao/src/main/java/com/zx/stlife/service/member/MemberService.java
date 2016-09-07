package com.zx.stlife.service.member;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.jpa.query.Query.MatchType;
import com.zx.stlife.base.ParamsEntity;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.member.MemberMessage;
import com.zx.stlife.entity.member.MemberWithdraw;
import com.zx.stlife.entity.record.Jifen;
import com.zx.stlife.entity.sys.Config;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.member.MemberDao;
import com.zx.stlife.repository.jpa.member.MemberWithdrawDao;
import com.zx.stlife.service.record.JifenService;
import com.zx.stlife.service.sys.ConfigService;
import com.zx.stlife.tools.DateUtils;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class MemberService {

	private static Logger logger = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberWithdrawDao memberWithdrawDao;
	@Autowired
	private JifenService jifenService;
	@Autowired
	private ConfigService configService;

	private static String QL_COUNT_BY_QQ_NO = "select count(id) from Member where qq=?1 ";
	
	private static String QL_COUNT_BY_ALIPAY_NO = "select count(id) from Member where accountName=?1 and type=?2";
	
	private static String QL_COUNT_BY_USERID_NO = "select count(id) from Member where accountName=?1 and type=?2 and userId=?3";

	@Transactional
	public void saveMember(Member member) {
		member = memberDao.save(member);
	}

	@Transactional
	public void saveMemberWithdraw(MemberWithdraw memberWithdraw) {
		memberWithdraw = memberWithdrawDao.save(memberWithdraw);
	}
	/**
	 * 获取会员
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public Member findMember(Integer userId) {
		return memberDao.findMemberByUserId(userId);
	}

	/**
	 * 获取会员资料
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> findMemberInfo(Integer userId) {
		Map<String, Object> dataMap = null;
		Member member = memberDao.findMemberByUserId(userId);
		if (member != null) {
			dataMap = new HashMap<String, Object>();
			dataMap.put("nickName", member.getUser().getNickName());
			dataMap.put("userName", member.getUser().getUserName());
			dataMap.put("gender", member.getUser().getGender());
			dataMap.put("mobileNo", member.getUser().getMobileNo());
			dataMap.put("isFromThirdpart", member.getUser().isFromThirdpart());
			dataMap.put("headImg", member.getHeadImg());
			dataMap.put("balance", member.getBalance());
			dataMap.put("prizeAmount", member.getPrizeAmount());
			dataMap.put("jifen", member.getJifen());
			dataMap.put("memberLevel", member.getMemberLevel().getName());
			dataMap.put("type", member.getType());
			dataMap.put("accountName", member.getAccountName());
			dataMap.put("accountCode", member.getAccountCode());
			dataMap.put("accountBank", member.getAccountBank());
			dataMap.put("qq", member.getQq());

			Date birthday = member.getBirthday();
			if (birthday != null) {
				dataMap.put("birthday",
						DateUtils.dateToYYYYMMDDHHMMSSSSSString(birthday));
			} else {
				dataMap.put("birthday", birthday);
			}
		}
		return dataMap;
	}

	/**
	 * 模糊查询会员列表
	 * 
	 * @param page
	 * @param params
	 */
	public void search(Page<Member> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select t from Member t")
				.like("t.user.userName", params.get("userName"))
				.like("t.user.mobileNo", params.get("mobileNo"))
				.like("t.memberLevel.name", params.get("level")).orderBy("t.createTime");
		memberDao.queryPage(page, query.getQLString(), query.getValues());
	}

	public Member get(Integer id) {
		return memberDao.findOne(id);
	}

	@Transactional
	public void delete(List<Integer> ids) {
		memberDao.deleteByIds(Const.CommonState.DELETED, ids);
	}

	/**
	 * 获取用户的头像
	 * @param userId
	 * @return
	 */
	public String getHeadImgByUser(Integer userId){
		return memberDao.getHeadImgByUser(userId);
	}

	/**
	 * 获取用户余额
	 * @param userId
	 * @return
	 */
	public Integer getBalanceByUser(Integer userId){
		return memberDao.getBalanceByUser(userId);
	}

	@Transactional
	public void recordJifen(Integer userId, int jifen, String descr){
		Member member = findMember(userId);
		member.addJifen(jifen);
		saveMember(member);

		Jifen entity = new Jifen(member, jifen, descr, new User(userId));
		jifenService.save(entity);
	}

	/**
	 * 新增并保存余额
	 * @param userId
	 * @param money
	 */
	@Transactional
	public void addAndSaveBalance(Integer userId, int money){
		Member member = findMember(userId);
		member.addBalance(money);
		saveMember(member);
	}

	/**
	 * 减少并保存余额
	 * @param userId
	 * @param money
	 */
	@Transactional
	public void subtractAndSaveBalance(Integer userId, int money){
		Member member = findMember(userId);
		member.subtractBalance(money);
		saveMember(member);
	}
	
	
	
	
	/**
	 * 判断qq是否存在
	 * @param QQ
	 * @return
	 */
	
	public boolean isExistsQQ(long qq){

		Long amount = (Long)memberDao.getObject(QL_COUNT_BY_QQ_NO, qq);
		return amount > 0;
	}
	
	/**
	 * 判断alipay或者bankaccount是否存在
	 * @param alipayaccount
	 * @return
	 */
	
	public boolean isExistsAlipay(String alipayAccount,Integer type){

		Long amount = (Long)memberDao.getObject(QL_COUNT_BY_ALIPAY_NO, alipayAccount,type);
		return amount > 0;
	}
	
	/**
	 * 根据判断alipay或者bankaccount是否存在
	 * @param alipayaccount
	 * @return
	 */
	
	public boolean isExistsAlipay(String alipayAccount,Integer type,Integer userId){

		Long amount = (Long)memberDao.getObject(QL_COUNT_BY_USERID_NO, alipayAccount,type,userId);
		return amount > 0;
	}
	
	/**
	 * 保存提现记录
	 * @param userId
	 * @param money
	 */
	@Transactional
	public void saveWithdraw(Integer userId, Integer dhjifen){
		Member member = findMember(userId);
		int fee = 5;
		Config config = configService.get(ConfigService.WITHDRAW_FEE);
		if (config != null && StringUtils.isNotEmpty(config.getValue())) {
			fee = Integer.parseInt(config.getValue());
		}
		double feeFloat = (100.00-fee)/100;
		float price=(float) (dhjifen/100*feeFloat);
		MemberWithdraw memberWithdraw=new MemberWithdraw(member,dhjifen,price,null);
		memberWithdrawDao.save(memberWithdraw);

	}
	
	/**
	 * 查询会员提现申请
	 * 
	 * @param page
	 * @param params
	 */
	public void searchWithdraw(Page<MemberWithdraw> page,ParamsEntity paramsEntity) {
		Query query = new Query();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd") ; 
		try {
			if(paramsEntity.get("id")!=null&&paramsEntity.get("id")!="")
			{
			query.table("select t from MemberWithdraw t")
					.eq("t.member.id", Integer.parseInt(paramsEntity.get("id")))
					.like("t.member.user.userName", paramsEntity.get("userName"));
			}else
			{
				query.table("select t from MemberWithdraw t")
				.like("t.member.user.userName", paramsEntity.get("userName"));
				
				if (StringUtils.isNotEmpty(paramsEntity.get("createTime"))) {
					query.eq("t.createTime", sdf1.parse(paramsEntity.get("createTime")));
				}
				if (StringUtils.isNotEmpty(paramsEntity.get("isdone"))) {
					if ("0".equals(paramsEntity.get("isdone"))) {
						// 未处理（包括isdone=null,0）
						query.eq("t.isdone", Integer.parseInt(paramsEntity.get("isdone")));
						query.or("t.isdone", MatchType.NULL, "null");
					} else {
						// 已处理（isdone=1）
						query.eq("t.isdone", Integer.parseInt(paramsEntity.get("isdone")));
					}
				}

			}
		} catch (NumberFormatException | ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		query.orderBy("t.createTime");
		memberWithdrawDao.queryPage(page, query.getQLString(), query.getValues());
		
	}
	
	/**
	 * 查询会员留言
	 * 
	 * @param page
	 * @param params
	 */
	public void searchMessage(Page<MemberMessage> page,ParamsEntity paramsEntity) {
		Query query = new Query();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd") ; 
		
		try {
			if(paramsEntity.get("id")!=null&&paramsEntity.get("id")!="")
			{
				query.table("select t from MemberMessage t")
						.eq("t.member.id", Integer.parseInt(paramsEntity.get("id")))
						.like("t.member.user.userName", paramsEntity.get("userName"));
			}else
			{
				query.table("select t from MemberMessage t")
				.like("t.member.user.userName", paramsEntity.get("userName"));
			}
			if (StringUtils.isNotEmpty(paramsEntity.get("createTime"))) {
				query.eq("t.createTime", sdf1.parse(paramsEntity.get("createTime")));
			}
			if (StringUtils.isNotEmpty(paramsEntity.get("isread"))) {
				if ("0".equals(paramsEntity.get("isread"))) {
					// 未读（包括isread=null,0）
					query.eq("t.isread", Integer.parseInt(paramsEntity.get("isread")));
					query.or("t.isread", MatchType.NULL, "null");
				} else {
					// 已读（isread=1）
					query.eq("t.isread", Integer.parseInt(paramsEntity.get("isread")));
				}
			}
		} catch (NumberFormatException | ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		query.orderBy("t.createTime");
		memberWithdrawDao.queryPage(page, query.getQLString(), query.getValues());
		
	}

	

}
