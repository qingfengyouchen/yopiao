package com.zx.stlife.service.record;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.member.Member;
import com.zx.stlife.entity.record.RechargeRecord;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.record.RechargeRecordDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class RechargeRecordService {

    private static Logger logger = LoggerFactory.getLogger(RechargeRecordService.class);

    private String QL_QUERY_PAGE_BY_USER = "select t from RechargeRecord t where " +
            "t.user.id=?1 and t.payTime<=?2 and t.state=?3 order by t.payTime desc";
    
    private String QL_QUERY_PAGE = "select t from RechargeRecord t where " +
            "t.payTime<=?1 and t.state=?2 order by t.payTime desc";

    @Autowired
    private RechargeRecordDao rechargeRecordDao;


    public RechargeRecord get(Integer id){
        return rechargeRecordDao.findOne(id);
    }

    @Transactional
    public void save(RechargeRecord record) {
        record = rechargeRecordDao.save(record);
    }

    public void findRechargeRecords(Integer userId, Date date, Page<RechargeRecord> page) {
        rechargeRecordDao.queryPage(page, QL_QUERY_PAGE_BY_USER,
                userId, date, Const.RechargeState.RECHARGE_SUCCESS);
    }
    
    public void findRechargeRecordsAll(Date date, Page<RechargeRecord> page) {
        rechargeRecordDao.queryPage(page, QL_QUERY_PAGE_BY_USER,
                date, Const.RechargeState.RECHARGE_SUCCESS);
    }

    @Transactional
    public void save(int thirdpartPayMoney, boolean isWeixinPay, User user, Byte state, Date payTime) {
        RechargeRecord rechargeRecord = new RechargeRecord(
                user, thirdpartPayMoney, isWeixinPay
                    ? Const.PayWayType.WEIXIN : Const.PayWayType.ALIPAY, state, payTime);
        save(rechargeRecord);
    }

    @Transactional
    public RechargeRecord save(int money, boolean isWeixinPay, User user,
                     String outTradeNo, Boolean needQueryPayResult, Date queryDeadlineTime, Byte state) {
        RechargeRecord rechargeRecord = new RechargeRecord(
                user, money, isWeixinPay ? Const.PayWayType.WEIXIN : Const.PayWayType.ALIPAY,
                outTradeNo, needQueryPayResult, queryDeadlineTime, state);
        save(rechargeRecord);

        return rechargeRecord;
    }

    public RechargeRecord getByOutTradeNo(String outTradeNo){
        return rechargeRecordDao.getByOutTradeNo(outTradeNo);
    }

	/**
	 * 模糊查询充值记录
	 * 
	 * @param page
	 * @param params
	 */
	public void search(Page<RechargeRecord> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select t from RechargeRecord t")
		.like("t.user.userName", params.get("userName"))
		.like("t.user.mobileNo", params.get("mobileNo"))
		.eq("t.state", Const.PayResult.PAY_SUCCESS)
		.orderBy("t.payTime desc");
		rechargeRecordDao.queryPage(page, query.getQLString(), query.getValues());
	}
}
