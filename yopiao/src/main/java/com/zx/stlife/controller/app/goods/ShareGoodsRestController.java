package com.zx.stlife.controller.app.goods;

import com.base.jpa.query.Page;
import com.base.modules.util.ConvertUtils;
import com.base.modules.util.FileUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.base.modules.util.Threads;
import com.zx.stlife.constant.Const;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.controller.common.BizCommon;
import com.zx.stlife.entity.goods.GoodsTimes;
import com.zx.stlife.entity.goods.ShareGoods;
import com.zx.stlife.entity.goods.ShareImage;
import com.zx.stlife.service.goods.GoodsService;
import com.zx.stlife.service.goods.GoodsTimesService;
import com.zx.stlife.service.goods.ShareGoodsService;
import com.zx.stlife.service.goods.ShareImageService;
import com.zx.stlife.tools.DateUtils;
import com.zx.stlife.tools.ImageUtils;
import com.zx.stlife.tools.RandomUitls;
import com.zx.stlife.tools.validator.ShareGoodsValidator;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.LockTimeoutException;

import java.io.File;
import java.net.URLDecoder;
import java.util.*;

/**
 * 晒单服务API
 * 
 * @author fcw
 *
 */
@RestController
@RequestMapping("/app/goods/shareGoods")
public class ShareGoodsRestController extends BaseRestController {

	@Autowired
	private ShareGoodsService shareGoodsService;

	@Autowired
	private ShareGoodsValidator shareGoodsValidator;

	@Autowired
	private ShareImageService shareImageService;

	@Autowired
	private GoodsTimesService goodsTimesService;
	@Autowired
	private GoodsService goodsService;

	/**
	 * 晒单分享
	 * 
	 * @param userId
	 * @param goodsTimesId
	 * @param shareGoods
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public JsonResult create(@RequestParam("userId") Integer userId,
			@RequestParam("goodsTimesId") Integer goodsTimesId,
			@RequestParam("imgs") List<MultipartFile> imgFiles,
			@RequestParam("title") String title,
			@RequestParam("content") String content,
			ShareGoods shareGoods, BindingResult result) {

		String titletmp = null;
		String contenttmp = null;
		try {
            //app客户端需要先做一次url的encode处理
        	titletmp = URLDecoder.decode(title, "UTF-8");
        	contenttmp = URLDecoder.decode(content, "UTF-8");
        } catch (Exception e) {
        }
		shareGoods.setTitle(titletmp);
		shareGoods.setContent(contenttmp);
		try {
			ShareGoods shareGoodsNew = shareGoodsService
					.getShareGoodsByGoodsTimes(goodsTimesId);
			if (shareGoodsNew != null
					&& shareGoodsNew.getState() != Const.ShareGoodsState.AUDIT_NOT_PASS
					&& shareGoodsService.checkGoodsTimes(goodsTimesId)) {
				return buildFailureResult(307);
			}
			shareGoodsValidator.validate(shareGoods, result);// 添加验证
			if (result.hasErrors()) {
				int errorCode = Integer.valueOf(result.getFieldError()
						.getCode());
				return buildFailureResult(errorCode);
			} else {
				if (imgFiles == null) {
					return buildFailureResult(305);
				} else {
					if (imgFiles.size() > 8) {
						return buildFailureResult(306);
					}
				}
			}
			Map<String, Object> dataMap = uploadFile(imgFiles, goodsTimesId);
			List<String> imgList = (List<String>) dataMap.get("imgList");
			shareGoods.setThumbnail(dataMap.get("thumbnailName").toString());
			shareGoodsNew = new ShareGoods();
			shareGoodsNew.setThumbnail(dataMap.get("thumbnailName").toString());
			while (true) {
				try {
					if(shareGoodsNew != null){
						shareGoodsNew.setTitle(shareGoods.getTitle());
						shareGoodsNew.setContent(shareGoods.getContent());
						shareGoods = shareGoodsService.saveShareGoods(userId,
								goodsTimesId, shareGoodsNew);
					}
					break;
				} catch (ObjectOptimisticLockingFailureException
						| StaleObjectStateException ex) {
					logger.info("晒单出现并发访问...");
					Threads.sleep(RandomUitls.randomInt(100));
				} catch (CannotAcquireLockException | LockTimeoutException ex) {
					logger.info("晒单出现死锁，等待继续执行");
					Threads.sleep(RandomUitls.randomInt(300));
				} catch (Exception ex) {
					logger.error(ex.getClass().getName());
					logger.error("晒单出现系统错误：" + ex.getMessage(), ex);
					return buildFailureResult();
				}
			}
			for (String image : imgList) {
				ShareImage shareImage = new ShareImage(image, shareGoods);
				shareImageService.save(shareImage);
			}
			return buildSuccessResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}

	/**
	 * 上传晒单图片 图片存放目录 按商品/商品期号分文件夹
	 * 
	 * @param fileList
	 * @return
	 */
	private Map<String, Object> uploadFile(List<MultipartFile> fileList,
			Integer goodsTimesId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> imgList = new ArrayList<String>();
		String foldName = goodsTimesService.getFoldName(goodsTimesId);
		try {
			MultipartFile thumbnailFile = fileList.get(0);
			String thumbnailName = SimpleUtils.getHibernateUUID();
			String extName = thumbnailFile.getOriginalFilename();
			String imgRootPath = FileUtilsEx.joinPaths(
					Const.SHARE_GOODS_IMG_ROOT_PATH, foldName);
			String imgType = "";
			if (extName.lastIndexOf(".") == -1) {
				imgType = ".jpg";
			} else {
				imgType = extName.substring(extName.lastIndexOf('.'));
			}
			thumbnailName = thumbnailName + imgType;
			String thumbnailPath = FileUtilsEx.joinPaths(imgRootPath,
					thumbnailName);
			FileUtilsEx.createFold(imgRootPath);
			ImageUtils.createThumbnail(thumbnailFile.getInputStream(),
					thumbnailPath, Const.SHARE_THUMB_IMAGE_WIDTH,
					Const.SHARE_THUMB_IMAGE_HEIGHT);
			dataMap.put("thumbnailName", foldName + "/" + thumbnailName);

			for (MultipartFile multipartFile : fileList) {
				String fileName = SimpleUtils.getHibernateUUID();
				String extensionName = multipartFile.getOriginalFilename();
				String extensionType = "";
				if (extensionName.lastIndexOf(".") == -1) {
					extensionType = ".jpg";
				} else {
					extensionType = extensionName.substring(extensionName
							.lastIndexOf('.'));
				}
				String shareGoodsImg = fileName + extensionType;

				String sourceImagePath = FileUtilsEx.joinPaths(imgRootPath,
						shareGoodsImg);
				File sourceImageFile = new File(sourceImagePath);
				FileUtilsEx.writeByteArrayToFile(sourceImageFile,
						multipartFile.getBytes());
				imgList.add(foldName + "/" + shareGoodsImg);
			}
			dataMap.put("imgList", imgList);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return dataMap;
	}

	/**
	 * 查看晒单列表
	 * 
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/{goodsId}/list", method = RequestMethod.GET)
	public JsonResult list(@PathVariable("goodsId") Integer goodsId,
			@RequestParam("timestamp") String timestamp, Page<ShareGoods> page) {
		return BizCommon.getShareGoodsAttrByGoods(page, timestamp, goodsId,
				shareGoodsService);
	}

	/**
	 * 查看晒单详情
	 * 
	 * @param userId
	 * @param shareGoodsId
	 * @return
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public JsonResult detail(@PathVariable("id") Integer shareGoodsId) {
		try {
			ShareGoods shareGoods = shareGoodsService.detail(shareGoodsId);
			if (shareGoods != null) {
				Map<String, Object> shareGoodsMap = new HashMap<String, Object>();
				shareGoodsMap.put("shareGoodsId", shareGoodsId);
				shareGoodsMap.put("title", shareGoods.getTitle());
				shareGoodsMap.put("content", shareGoods.getContent());
				shareGoodsMap.put("createTimeStr", DateUtils
						.dateToYYYYMMDDHHMMSSSSSString(shareGoods
								.getCreateTime()));
				shareGoodsMap.put("userId", shareGoods.getUser().getId());
				shareGoodsMap.put("userNickName", shareGoods.getUserNickName());
				shareGoodsMap.put("goodsName", shareGoods.getGoodsName());
				shareGoodsMap.put("goodsTimesName", shareGoods.getGoodsTimes()
						.getTimes());
				shareGoodsMap.put("winngUserBuyTimes", shareGoods
						.getGoodsTimes().getWinngUserBuyTimes());
				shareGoodsMap.put("luckNum", shareGoods.getGoodsTimes()
						.getLuckNum());
				shareGoodsMap.put("openTimeStr", shareGoods.getGoodsTimes()
						.getOpenTimeStr());
				shareGoodsMap.put("state", shareGoods.getState());
				List<String> urlList = shareImageService
						.findShareImage(shareGoodsId);
				shareGoodsMap.put("shareImages", urlList);

				return buildSuccessResult(shareGoodsMap);
			}
			return buildFailureResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}

	/**
	 * 我的晒单
	 * 
	 * @param userId
	 * @param timestamp
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public JsonResult my(@RequestParam("userId") Integer userId,
			@RequestParam("timestamp") String timestamp, Page<GoodsTimes> page) {
		try {
			Date date = DateUtils.YYYYMMDDHHMMSSSSSStringToDate(timestamp);
			goodsTimesService.searchGoodsTimeList(page, date, userId);

			Map<String, Object> data = ConvertUtils.convertEntityToMap(page,
					"pageNo", "totalPages", "totalCount");
			List<GoodsTimes> goodsTimesList = page.getResult();

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

			for (GoodsTimes goodsTimes : goodsTimesList) {

				Map<String, Object> shareMap = ConvertUtils.convertEntityToMap(
						goodsTimes, new String[] { "id", "goodsTimesId" },
						new String[] { "times" }, new String[] { "goodsName" },
						new String[] { "thumbnail" },
						new String[] { "openTimeStr" },
						new String[] { "luckNum" },
						new String[] { "winngUserBuyTimes" }, new String[] {
								"winngUserName", "userNickName" },
						new String[] { "hasShareGoods" });

				if (goodsTimes.getHasShareGoods()) {
					ShareGoods shareGoods = shareGoodsService
							.getShareGoodsByGoodsTimes(goodsTimes.getId());
					shareMap.put("shareGoodsId", shareGoods.getId());
					shareMap.put("title", shareGoods.getTitle());
					shareMap.put("content", shareGoods.getContent());
					shareMap.put("shareGoodsThumbnail",
							shareGoods.getThumbnail());
					shareMap.put("createTimeStr", shareGoods.getCreateTimeStr());
					shareMap.put("state", shareGoods.getState());
				} else {
					shareMap.put("shareGoodsId", null);
					shareMap.put("title", null);
					shareMap.put("content", null);
					shareMap.put("shareGoodsThumbnail", null);
					shareMap.put("shareTime", null);
					shareMap.put("state", null);
				}
				result.add(shareMap);
			}
			data.put("result", result);
			return buildSuccessResult(data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}

	/**
	 * 晒单首页（查询所有的已审核的晒单信息）
	 * 
	 * @param timestamp
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult listAll(@RequestParam("timestamp") String timestamp,
			@RequestParam(value = "userId", required = false) Integer userId,
			Page<ShareGoods> page) {
		return BizCommon.getShareGoodsAttr(page, timestamp, userId,
				shareGoodsService);
	}
}
