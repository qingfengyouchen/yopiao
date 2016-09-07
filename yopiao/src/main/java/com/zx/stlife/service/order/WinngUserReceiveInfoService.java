package com.zx.stlife.service.order;

import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.order.WinngGoodsState;
import com.zx.stlife.entity.order.WinngUserReceiveInfo;
import com.zx.stlife.repository.jpa.order.WinngGoodsStateDao;
import com.zx.stlife.repository.jpa.order.WinngUserReceiveInfoDao;
import com.zx.stlife.service.goods.GoodsTimesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class WinngUserReceiveInfoService {

	private static Logger logger = LoggerFactory.getLogger(WinngUserReceiveInfoService.class);

	@Autowired
	private WinngUserReceiveInfoDao winngUserReceiveInfoDao;

	@Transactional
	public void save(WinngUserReceiveInfo entity){
		entity = winngUserReceiveInfoDao.save(entity);
	}

	public WinngUserReceiveInfo getByGoodsTimes(Integer goodsTimesId){
		return winngUserReceiveInfoDao.getByGoodsTimes(goodsTimesId);
	}

	@Transactional
	public void saveLogisticsInfo(Integer goodsTimesId, String logisticsName, String logisticsNo){
		WinngUserReceiveInfo winngUserReceiveInfo = getByGoodsTimes(goodsTimesId);
		winngUserReceiveInfo.addLogistics(logisticsName, logisticsNo);
		save(winngUserReceiveInfo);
	}

	public String getMobileNoByGoodsTimes(Integer goodsTimesId){
		return winngUserReceiveInfoDao.getMobileNoByGoodsTimes(goodsTimesId);
	}
}
