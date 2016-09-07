package com.zx.stlife.service.record;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.zx.stlife.entity.order.SnatchRecordDetail;
import com.zx.stlife.repository.jpa.order.SnatchRecordDetailDao;


@Component
@Transactional
public class SnatchService {

    @Autowired
    private SnatchRecordDetailDao snatchRecordDetailDao;

    public void getAllSnatch(Integer userId, Date date, Page<SnatchRecordDetail> page) {
        Query query = new Query();
        query.table("select t from SnatchRecordDetail t")
                .eq("t.user.id", userId)
                .le("t.createTime", date)
                .orderBy("t.createTime desc");
        snatchRecordDetailDao.queryPage(page, query.getQLString(), query.getValues());
    }

}
