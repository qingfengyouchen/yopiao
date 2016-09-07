package com.zx.stlife.service.notice;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.member.MemberMessage;
import com.zx.stlife.entity.notice.Notice;
import com.zx.stlife.entity.notice.NoticeSendRecord;
import com.zx.stlife.repository.jpa.member.MemberMessageDao;
import com.zx.stlife.repository.jpa.notice.NoticeDao;
import com.zx.stlife.repository.jpa.notice.NoticeSendRecordDao;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Service
@Transactional(readOnly = true)
public class NoticeService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(NoticeService.class);

	private static String HQL_UPDATE_NOTICE="update Notice n set n.state=?1 where n.id=?2";
	
	private static String HQL_UPDATE_MESSAGE_READ="update MemberMessage n set n.isread=?1 where n.id=?2";
	
	private static String HQL_UPDATE_WITHDRAW_DONE="update MemberWithdraw n set n.isdone=?1 where n.id=?2";
	
	private static String HQL_GET_MEMBER_ID="select n.member.id from MemberWithdraw n where n.id=?1";
	
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private NoticeSendRecordDao noticeSendRecordDao;
	
	@Autowired
	private MemberMessageDao memberMessageDao;

	/**
	 * @param date 时间
	 * @param state 状态
	 * @param pageNo 开始
	 * @param pageSize 数量
	 * @return
	 */
	public  void getAllNotice(Date date,Byte state,Page<Notice> page) {
		Query query = new Query();
		query.table("select n from Notice n").eq("n.state", state).le("createTime", date);
		query.orderBy("n.createTime desc");
		noticeDao.queryPage(page,query.getQLString(),query.getValues());
	}

	/**
	 * @param date 时间
	 * @param state 状态
	 * @param pageNo 开始
	 * @param pageSize 数量
	 * @return
	 */
	public  void getAllNoticeMessage(List<Integer> userid, Date date,Byte state,Page<Notice> page) {
		Query query = new Query();
		query.table("select n from Notice n").in("n.user.id", userid).eq("n.state", state).le("createTime", date);
		query.orderBy("n.createTime desc");
		noticeDao.queryPage(page,query.getQLString(),query.getValues());
	}
	
	@Transactional
	public void save(Notice notice) {
		notice = noticeDao.save(notice);
	}

	@Transactional
	public void saveNoticeSendRecord(NoticeSendRecord noticeSendRecord) {
		noticeSendRecord = noticeSendRecordDao.save(noticeSendRecord);
	}
	@Transactional
	public void saveMessage(MemberMessage memberMessage) {
		memberMessage = memberMessageDao.save(memberMessage);
	}
	@Transactional
	public void delete(List<Integer> ids){
		for (Integer id : ids) {
			noticeDao.executeUpdate(HQL_UPDATE_NOTICE, Const.CommonState.DELETED,id);
		}
	} 
	
	@Transactional
	public void updateIsRead(Integer id){
		memberMessageDao.executeUpdate(HQL_UPDATE_MESSAGE_READ, 1, id);
	} 
	
	@Transactional
	public void updateIsDone(Integer id){
		memberMessageDao.executeUpdate(HQL_UPDATE_WITHDRAW_DONE, 1, id);
	} 
	
	@Transactional
	public Integer getMemberId(Integer id){
		return (Integer) memberMessageDao.getObject(HQL_GET_MEMBER_ID, id);
	}
	
	public Notice getByNotice(Integer noticeId) {
		return noticeDao.getOne(noticeId);
	}

	public void search(Page<Notice> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select n.id as id,n.title as title,n.content as content,n.sender_name as senderName,n.create_time as createTime from notice n")
		.like("n.title", params.get("title")).eq("n.state",Const.CommonState.ENABLE).orderBy("n.id desc");
		noticeDao.querySQLPage(page, Notice.class, query.getQLString(), query.getValues());
	}
}
