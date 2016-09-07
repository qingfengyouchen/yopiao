package com.zx.stlife.service.goods;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zx.stlife.constant.Const.CommonState;
import com.zx.stlife.entity.goods.ShareImage;
import com.zx.stlife.repository.jpa.goods.ShareImageDao;

/**
 * @author micheal fcw
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class ShareImageService {

	@Autowired
	private ShareImageDao shareImageDao;
	private static final String HQL_FIND_TOP_IMG = 
			"select t from ShareImage t where t.shareGoods.id=?1 order by t.createTime desc";
	@Transactional
	public ShareImage save(ShareImage entity){
		entity.setState(CommonState.ENABLE);
		return shareImageDao.save(entity);
	}
	
	public List<String> findShareImage(Integer shareGoodsId){
		List<ShareImage> shareImageList =  shareImageDao.findTop(3,HQL_FIND_TOP_IMG,shareGoodsId);
		List<String> urlList = new ArrayList<String>();
		for (ShareImage shareImage : shareImageList) {
			urlList.add(shareImage.getUrl());
		}
		return urlList;
	}
	
	public List<String> findAllShareImage(Integer shareGoodsId){
		List<ShareImage> shareImageList =  shareImageDao.findShareImage(shareGoodsId);
		List<String> urlList = new ArrayList<String>();
		for (ShareImage shareImage : shareImageList) {
			urlList.add(shareImage.getUrl());
		}
		return urlList;
	}
	@Transactional
	public void deleteImg(List<Integer> ids){
		shareImageDao.deleteByIds(CommonState.DELETED,ids);
	}
 }
