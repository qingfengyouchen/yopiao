package com.zx.stlife.service.goods;

import com.base.jpa.query.Page;
import com.base.jpa.query.Query;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.FileUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.base.UserUtils;
import com.zx.stlife.constant.Const;

import static com.zx.stlife.constant.Const.*;

import com.zx.stlife.entity.TmpFile;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.goods.ShareGoods;
import com.zx.stlife.entity.goods.ShareImage;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.goods.ShareGoodsDao;
import com.zx.stlife.repository.jpa.goods.ShareImageDao;
import com.zx.stlife.repository.jpa.member.MemberDao;
import com.zx.stlife.repository.jpa.sys.UserDao;
import com.zx.stlife.service.TmpFileService;
import com.zx.stlife.service.order.WinngGoodsStateService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.ImageUtils;
import com.zx.stlife.tools.StringUtilsEx;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class ShareGoodsService {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ShareGoodsService.class);

	@Autowired
	private GoodsTimesService goodsTimesService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ShareGoodsDao shareGoodsDao;
	@Autowired
	private ShareImageDao shareImageDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private WinngGoodsStateService winngGoodsStateService;
	@Autowired
	private ShareImageService shareImageService;
	@Autowired
	private TmpFileService tmpFileService;

	private static final String HQL_FIND_TOP_SHAREGOODS =
			"select s from ShareGoods s where s.state =?1 order by s.id desc";

	private static final String HQL_FIND_TOP_SHAREGOODS_BY_GOODSID =
			"select s from ShareGoods s where s.goodsTimes.goodsInfo.id =?1 and s.state =?2 order by s.id desc";

	private static final String SQL_UPDATE_GOODS_NAME =
			"update goods_share t, goods_times g set t.goods_name=?2 where t.goods_times_id=g.id and g.id=?1";
	/**
	 * 添加/修改晒单信息
	 * 
	 * @param userId
	 * @param goodsTimesId
	 * @param shareGoods
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public ShareGoods saveShareGoods(Integer userId, Integer goodsTimesId,
			ShareGoods shareGoods) throws Exception {
		GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
		User user = userDao.findOne(userId);
		shareGoods.setGoodsTimes(goodsTimes);
		shareGoods.setUser(user);
		shareGoods.setGoodsTimesName(goodsTimes.getTimes());
		shareGoods.setGoodsName(goodsTimes.getGoodsName());
		shareGoods.setUserNickName(user.getNickName());
		shareGoods.setUserHeadImg(memberDao.getHeadImgByUser(userId));
		shareGoods.setState(Const.ShareGoodsState.UNAUDIT);

		shareGoods = shareGoodsDao.save(shareGoods);
		goodsTimesService.updateHasShareGoods(goodsTimesId, true);
		winngGoodsStateService.finish(goodsTimesId);

		return shareGoods;
	}

	/**
	 * 查看晒单列表
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ShareGoods> list(Integer userId) {
		return shareGoodsDao.findShareGoods(userId);
	}

	public ShareGoods get(Integer id) {
		return shareGoodsDao.findOne(id);
	}

	public boolean checkGoodsTimes(Integer goodsTimesId) {
		Integer count = shareGoodsDao.countByGoodsTimes(goodsTimesId, ShareGoodsState.DELETEED);
		return count != null && count > 0;
	}

	/**
	 * 查询全部晒单列表信息API
	 * 
	 * @param date
	 * @param page
	 * @param userId
	 */
	public void searchAll(Date date, Page<ShareGoods> page, Integer userId) {
		Query query = new Query();
		query.table("select t from ShareGoods t ");
		if (userId != null && !"".equals(userId)) {
			query.eq("t.user.id", userId);
		}
		query.eq("t.state", ShareGoodsState.AUDIT_PASS)
				.le("t.createTime", date).orderBy("t.createTime desc");

		shareGoodsDao.queryPage(page, query.getQLString(), query.getValues());
		// bindData(page);
	}

	/**
	 * 分页查看晒单列表信息API
	 * 
	 * @param goodsId
	 * @param date
	 * @param page
	 */
	public void pageSearch(Integer goodsId, Date date, Page<ShareGoods> page) {
		Query query = new Query();
		query.table("select t from ShareGoods t ")
				.eq("t.goodsTimes.goodsInfo.id", goodsId)
				.eq("t.state", ShareGoodsState.AUDIT_PASS)
				.le("t.createTime", date).orderBy("t.createTime desc");

		shareGoodsDao.queryPage(page, query.getQLString(), query.getValues());
	}

	/**
	 * 我的晒单信息API
	 * 
	 * @param goodsTimesId
	 * @return
	 */
	@Transactional(readOnly = true)
	public ShareGoods getShareGoodsByGoodsTimes(Integer goodsTimesId) {
		return shareGoodsDao.getShareGoodsByGoodsTimes(goodsTimesId, ShareGoodsState.DELETEED);
	}

	/**
	 * 查看晒单信息
	 * 
	 * @param shareGoodsId
	 * @return
	 */
	@Transactional(readOnly = true)
	public ShareGoods detail(Integer shareGoodsId) {
		return shareGoodsDao.findOne(shareGoodsId);
	}

	/**
	 * 删除晒单
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional
	public void deleteShareGoods(List<Integer> ids) {
		shareGoodsDao.deleteShareGoods(ShareGoodsState.DELETEED, ids);
		shareImageDao.deleteByShareGoodsId(ShareGoodsState.DELETEED, ids);
	}

	/**
	 * 根据用户ID查询晒单列表
	 * 
	 * @return
	 */
	public List<ShareGoods> findAllWithIdAndName() {
		Integer userId = UserUtils.getCurrentUserId();
		return shareGoodsDao.findShareGoods(userId);
	}

	/**
	 * 分页查询晒单WEB
	 * 
	 * @param page
	 * @param params
	 */
	public void search(Page<ShareGoods> page, Map<String, String> params) {
		Query query = new Query();
		query.table("select t from ShareGoods t ").like("t.title",
				params.get("title"));
		Byte state = SimpleUtils.stringToByte(params.get("state"));
		if (state == null) {
			query.ne("t.state", CommonState.DELETED);
		} else {
			query.eq("t.state", state);
		}
		query.orderBy("t.createTime asc");

		shareGoodsDao.queryPage(page, query.getQLString(), query.getValues());
	}

	/**
	 * 保存晒单信息
	 * 
	 * @param entity
	 */
	@Transactional
	public void save(ShareGoods entity) {
		entity = shareGoodsDao.save(entity);
	}

	/**
	 * 查询晒单图片列表
	 * 
	 * @param shareGoodsId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ShareImage> findByShareGoods(Integer shareGoodsId) {
		return shareImageDao.findByShareGoods(shareGoodsId, CommonState.ENABLE);
	}

	/**
	 * 修改用户头像
	 * 
	 * @param userHeadImg
	 * @param userId
	 */
	@Transactional
	public void updateUserHeadImg(String userHeadImg, Integer userId) {
		shareGoodsDao.updateUserHeadImg(userHeadImg, userId);
	}

	public List<Map<String, Object>> bindData(Page<ShareGoods> page) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (ShareGoods shareGoods : page.getResult()) {
			Map<String, Object> shareMap = new HashMap<String, Object>();
			shareMap.put("shareGoodsId", shareGoods.getId());
			shareMap.put("goodsTimesName", shareGoods.getGoodsTimes()
					.getTimes());
			shareMap.put("userId", shareGoods.getUser().getId());
			shareMap.put("userHeadImg", shareGoods.getUserHeadImg());
			shareMap.put("userNickName", shareGoods.getUserNickName());
			shareMap.put("goodsName", shareGoods.getGoodsName());
			shareMap.put("createTimeStr", DateUtils
					.dateToYYYYMMDDHHMMSSSSSString(shareGoods.getCreateTime()));
			shareMap.put("title", shareGoods.getTitle());
			shareMap.put("content", shareGoods.getContent());

			List<String> urlList = shareImageService.findShareImage(shareGoods
					.getId());
			shareMap.put("shareImages", urlList);
			result.add(shareMap);
		}
		return result;
	}

	/**
	 * 微信版晒单首页
	 */
	public List<Map<String, Object>> searchAll(Page<ShareGoods> page) {
		shareGoodsDao.queryPage(page, HQL_FIND_TOP_SHAREGOODS,ShareGoodsState.AUDIT_PASS);
		return bindData(page);

	}

	public List<ShareGoods> findOnSales() {
		return shareGoodsDao.find(HQL_FIND_TOP_SHAREGOODS,ShareGoodsState.AUDIT_PASS);
	}

	public List<Map<String, Object>> pageSearch(Page<ShareGoods> page,
			Integer goodsId) {
		shareGoodsDao.queryPage(page, HQL_FIND_TOP_SHAREGOODS_BY_GOODSID,goodsId,ShareGoodsState.AUDIT_PASS);
		return bindData(page);
	}
	
	@Transactional
	public void deleteShareImg(Integer id){
		/*ShareImage goodsImage = shareImageDao.getOne(id);
		String imgPath = FileUtilsEx.joinPaths(SHARE_GOODS_IMG_ROOT_PATH, goodsImage.getUrl());
		FileUtilsEx.deleteFile(imgPath);*/
		shareImageDao.deleteByIds(CommonState.DELETED, Arrays.asList(id));  // 逻辑删除
	}
	public  List<Map<String, Object>> bindDateByMy(Page<GoodsTimes>page){
		List<Map<String, Object>> shareGoodsList = new ArrayList<Map<String, Object>>();
		List<GoodsTimes> goodsTimesList = page.getResult();
		for (GoodsTimes goodsTimes : goodsTimesList) {
			Map<String, Object> shareMap = ConvertUtils.convertEntityToMap(goodsTimes,
					new String[]{"id", "goodsTimesId"},
					new String[]{"times"},
					new String[]{"goodsName"},
					new String[]{"thumbnail"},
					new String[]{"luckNum"},
					new String[]{"openTimeStr"},
					new String[]{"winngUserBuyTimes"},
					new String[]{"winngUserName", "userNickName"},
					new String[]{"hasShareGoods"});

			if (goodsTimes.getHasShareGoods()) {
				ShareGoods shareGoods = getShareGoodsByGoodsTimes(goodsTimes.getId());
				shareMap.put("shareGoodsId", shareGoods.getId());
				shareMap.put("title", shareGoods.getTitle());
				shareMap.put("content", shareGoods.getContent());
				shareMap.put("shareGoodsThumbnail",
						shareGoods.getThumbnail());
				shareMap.put("createTime", shareGoods.getCreateTime());
				shareMap.put("state", shareGoods.getState());
			} else {
				shareMap.put("shareGoodsId", null);
				shareMap.put("title", null);
				shareMap.put("content", null);
				shareMap.put("shareGoodsThumbnail", null);
				shareMap.put("createTime", null);
				shareMap.put("state", null);
			}
			shareMap.put("stateMap", Const.ShareGoodsState.MAP);
			shareGoodsList.add(shareMap);
		}
		return shareGoodsList;
	}

	/**
	 * 新增或修改晒单
	 * @param user
	 * @param goodsTimesId
	 * @param shareGoods
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveShareGoods(User user, Integer goodsTimesId,
									 ShareGoods shareGoods) throws Exception {
		GoodsTimes goodsTimes = goodsTimesService.get(goodsTimesId);
		shareGoods.setGoodsTimes(goodsTimes);
		shareGoods.setUser(user);
		shareGoods.setGoodsTimesName(goodsTimes.getTimes());
		shareGoods.setGoodsName(goodsTimes.getGoodsName());
		shareGoods.setUserNickName(user.getNickName());
		shareGoods.setUserHeadImg(memberDao.getHeadImgByUser(user.getId()));

		shareGoods = shareGoodsDao.save(shareGoods);
		goodsTimesService.updateHasShareGoods(goodsTimesId, true);

		winngGoodsStateService.finish(goodsTimesId);
	}

	@Transactional
	public void buildImgs(ShareGoods entity, boolean isEdit, List<Integer> tmpFileIds) {
		if(SimpleUtils.isNullList(tmpFileIds))
			return;

		String foldName = goodsTimesService.getFoldName(entity.getGoodsTimes().getId());
		String imgRootPath = FileUtilsEx.joinPaths(SHARE_GOODS_IMG_ROOT_PATH, foldName);
		FileUtilsEx.createFold(imgRootPath);
		String imagePath = null;
		int i = 0;
		for(Integer tmpFileId : tmpFileIds) {
			if (tmpFileId == null) {
				continue;
			}
			TmpFile tmpFile = tmpFileService.get(tmpFileId);
			if(tmpFile == null){
				logger.error("找不到TmpFile ID为" + tmpFileId + "的记录");
				continue;
			}

			String imgPath = tmpFileService.dowithImg(
					SHARE_GOODS_IMG_ROOT_PATH, foldName, tmpFile.getUrl());
			ShareImage shareImage = new ShareImage(imgPath, entity);
			shareImageDao.save(shareImage);
			tmpFileService.deleteById(tmpFile.getId(), false);

			if(i == 0){
				imagePath = imgPath;
				i++;
			}
		}

		if(isEdit){
			String url = getFirstImagePath(entity.getId());
			if(StringUtils.isNotBlank(url)){
				imagePath = url;
			}
		}

		String thumbnailName = SimpleUtils.getHibernateUUID() +
				"." + StringUtilsEx.substringAfterLast(imagePath, ".");
		String thumbnailPath = FileUtilsEx.joinPaths(
				imgRootPath, thumbnailName);
		imagePath = FileUtilsEx.joinPaths(SHARE_GOODS_IMG_ROOT_PATH, imagePath);
		try {
			InputStream in = new FileInputStream(imagePath);
			ImageUtils.createThumbnail(in, thumbnailPath, SHARE_THUMB_IMAGE_WIDTH, SHARE_THUMB_IMAGE_HEIGHT);
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
		}

		entity.setThumbnail(foldName + "/" + thumbnailName);
		save(entity);
	}

	@Transactional
	public void saveShareImage(ShareImage entity){
		entity = shareImageDao.save(entity);
	}

	public String getFirstImagePath(Integer shareGoodsId){
		return shareImageDao.getFirstImageUrl(shareGoodsId, CommonState.ENABLE);
	}

	@Transactional
	public void updateGoodsName(Integer goodsId, String goodsName){
		shareImageDao.executeSQLUpdate(SQL_UPDATE_GOODS_NAME, goodsId, goodsName);
	}
}
