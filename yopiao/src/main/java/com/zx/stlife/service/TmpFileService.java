package com.zx.stlife.service;

import com.base.modules.util.FileUtilsEx;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.constant.Const;
import com.zx.stlife.entity.TmpFile;
import com.zx.stlife.repository.jpa.TmpFileDao;
import com.zx.stlife.tools.Base64FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class TmpFileService {

	private static Logger logger = LoggerFactory.getLogger(TmpFileService.class);

	@Autowired
	private TmpFileDao tmpFileDao;

	private final String QL_DELETE_BY_ID =
			"delete from TmpFile where id=?1";

	@Transactional
	public TmpFile saveTmpFile(TmpFile entity){
		entity = tmpFileDao.save(entity);
		return entity;
	}

	@Transactional
	public int deleteById(Integer id, boolean isDeleteImg){
		if(isDeleteImg){
			TmpFile tmpFile = get(id);
			String path = FileUtilsEx.joinPaths(Const.TMP_IMG_ROOT_PATH, "/", tmpFile.getUrl());
			FileUtilsEx.deleteFile(path);
		}
		return tmpFileDao.executeUpdate(QL_DELETE_BY_ID, id);
	}

	public TmpFile get(Integer id){
		return tmpFileDao.findOne(id);
	}

	@Transactional
	public Integer upload(MultipartFile img, Integer userId) {
		String id = SimpleUtils.getHibernateUUID();
		String extensionName = FileUtilsEx.getFileExtensionWithDot(img.getOriginalFilename());
		String imgName = id + extensionName;
		String sourceImagePath = FileUtilsEx.joinPaths(Const.TMP_IMG_ROOT_PATH, imgName);
		Integer entityId = null;
		try {
			File sourceImg = new File(sourceImagePath);
			FileUtilsEx.writeByteArrayToFile(sourceImg, img.getBytes());

			TmpFile tmpFile = new TmpFile(userId, imgName);
			tmpFile=saveTmpFile(tmpFile);
			entityId = tmpFile.getId();
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
		}

		return entityId;
	}

	@Transactional
	public String dowithImg(String rootDir, String parentNode, String tmpImgName){
		String destDirPath = rootDir;
		String imgDirPath = null;
		if(StringUtils.isNotBlank(parentNode)){
			imgDirPath = parentNode;
			destDirPath += "/" + imgDirPath;
		}

		try {
			FileUtilsEx.moveFileToDirectory(
					new File(Const.TMP_IMG_ROOT_PATH + "/" + tmpImgName),
					new File(destDirPath), true);
		}catch (Exception ex){
			logger.error(ex.getMessage(), ex);
		}

		return (imgDirPath == null ? "" : (imgDirPath + "/") ) + tmpImgName;
	}

	@Transactional
	public Integer uploadBase64File(Integer userId, String base64FileStr){
		String fileName = SimpleUtils.getHibernateUUID() + ".jpg";
		String uploadPath = FileUtilsEx.joinPaths(Const.TMP_IMG_ROOT_PATH, fileName);
		Integer tmpFileId = null;
		if(Base64FileUtils.decoderBase64File(base64FileStr, uploadPath)){
			TmpFile tmpFile = new TmpFile(userId, fileName);
			saveTmpFile(tmpFile);
			tmpFileId = tmpFile.getId();
		}
		return tmpFileId;
	}
}
