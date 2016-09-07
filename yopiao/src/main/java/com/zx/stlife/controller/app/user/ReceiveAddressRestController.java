package com.zx.stlife.controller.app.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.modules.util.ConvertUtils;
import com.base.modules.util.SimpleUtils;
import com.zx.stlife.controller.app.base.BaseRestController;
import com.zx.stlife.controller.app.base.JsonResult;
import com.zx.stlife.entity.sys.ReceiveAddress;
import com.zx.stlife.service.sys.ReceiveAddressService;

/**
 * 收获地址服务API
 */

@RestController
@RequestMapping("/app/address")
public class ReceiveAddressRestController extends BaseRestController {

	@Autowired
	private ReceiveAddressService receiveAddressService;

	/**
	 * 新增/修改收获地址
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JsonResult save(@RequestParam("userId") Integer userId,
			ReceiveAddress receiveAddress) {
		try {
			if(receiveAddressService.findUser(userId)==null){
				return buildFailureResult();
			}
			if (receiveAddress.getIsDefault()) {
				ReceiveAddress defaultReceiveAddress = receiveAddressService
						.findByState(userId);
				if (defaultReceiveAddress != null) {
					defaultReceiveAddress.setIsDefault(false);
					receiveAddressService.saveReceiveAddress(
							defaultReceiveAddress, userId);
				}
			}
			receiveAddress = receiveAddressService.saveReceiveAddress(
					receiveAddress, userId);
			if (receiveAddress != null) {
				return buildSuccessResult();
			} else {
				return buildFailureResult();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}

	/**
	 * 查看收获地址列表
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JsonResult list(@RequestParam("userId") Integer userId) {
		try {
			List<ReceiveAddress> receiveAddressList = receiveAddressService
					.findByUserId(userId);
			List<Map<String, Object>> list = ConvertUtils.convertCollectionToListMap(
					receiveAddressList,
					new String[]{"id"},
					new String[]{"receiver"},
					new String[]{"tel"},
					new String[]{"fullAddress", "detailAddress"},
					new String[]{"isDefault"}
			);

			return buildSuccessResult(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}

	}

	/**
	 * 查询具体收货地址信息
	 * 
	 * @param addressId
	 * @return
	 */
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public JsonResult get(@PathVariable("id") Integer addressId) {
		try {
			ReceiveAddress receiveAddress = receiveAddressService
					.findReceiveAddressInfo(addressId);
			if (receiveAddress == null) {
				return buildFailureResult(300);
			}
			Map<String, Object> map = ConvertUtils.convertEntityToMap(
					receiveAddress, "id", "receiver", "tel", "province",
					"city", "district", "detailAddress", "isDefault");
			return buildSuccessResult(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}

	/**
	 * 删除收获地址
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public JsonResult delete(@RequestParam("userId") Integer userId,
			@RequestParam("id") Integer id) {
		try {
			boolean result = receiveAddressService.delete(userId, id);
			if (result) {
				return buildSuccessResult();
			} else {
				return buildFailureResult();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}

	/**
	 * 设置默认收货地址
	 * 
	 * @param id
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/setDefault")
	public JsonResult setDefault(@RequestParam("id") Integer addressId,
			@RequestParam("userId") Integer userId) {
		try {
			ReceiveAddress receiveAddress = receiveAddressService.findReceiveAddressInfo(addressId);
			ReceiveAddress defaultReceiveAddress = receiveAddressService
					.findByState(userId);
			if (defaultReceiveAddress != null) {
				defaultReceiveAddress.setIsDefault(false);
				receiveAddressService.saveReceiveAddress(
						defaultReceiveAddress, userId);
			}
			receiveAddress.setIsDefault(true);
			receiveAddressService.saveReceiveAddress(receiveAddress, userId);
			return buildSuccessResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return buildFailureResult();
		}
	}
	
}
