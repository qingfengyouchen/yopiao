package com.zx.stlife.controller.wx.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.zx.stlife.base.UserWxUtils;
import com.zx.stlife.controller.web.BaseController;
import com.zx.stlife.entity.sys.ReceiveAddress;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.service.sys.AccountService;
import com.zx.stlife.service.sys.ReceiveAddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/wx/receiveAddress")
public class ReceiveAddressWxController extends BaseController {

	@Autowired
	private ReceiveAddressService addressService;

	@Autowired
	private AccountService accountService;

	private static final String VIEW_URI_PREFIX = "wx/user/";

	@RequestMapping(value = "detail")
	public String getAccount(Model model, HttpSession session) {
		Integer userId = UserWxUtils.getCurrUserId();
		List<ReceiveAddress> addresses = addressService.findByUserId(userId);
		model.addAttribute("addresses", addresses);
		model.addAttribute("userId", userId);
		boolean flag = true;
		if(session.getAttribute("goodsTimesId") == null){
			flag = false;
		}
		model.addAttribute("flag", flag);
		return VIEW_URI_PREFIX + "receive-address";
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public String edit(Model model, @RequestParam("id") Integer id) {
		Integer userId = UserWxUtils.getCurrUserId();
		ReceiveAddress address = addressService.get(id);
		model.addAttribute("userId", userId);
		model.addAttribute("address", address);
		return VIEW_URI_PREFIX + "edit-address";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(Model model) {
		Integer userId = UserWxUtils.getCurrUserId();
		ReceiveAddress address = new ReceiveAddress();
		model.addAttribute("userId", userId);
		model.addAttribute("address", address);
		return VIEW_URI_PREFIX + "edit-address";
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(Model model,
			@Valid @ModelAttribute("preloadEntity") ReceiveAddress address) {
		Integer userId = UserWxUtils.getCurrUserId();
		Integer id = address.getId();
		address = addressService.saveReceiveAddress(address, userId);
		if (id == null) {//id空则为新增，设置为默认地址
			List<ReceiveAddress> addresses = addressService
					.findByUserId(userId);
			List<Integer> ids = new ArrayList<Integer>();
			for (ReceiveAddress a : addresses) {
				ids.add(a.getId());
			}
			addressService.updateSetDefault(ids, address.getId());
		}
		return getRedirectUrl("detail");
	}

	@RequestMapping(value = "setDefault", method = RequestMethod.POST)
	@ResponseBody
	public int setDefault(Model model, @RequestParam("addressId") Integer addressId) {
		Integer userId = UserWxUtils.getCurrUserId();
		List<ReceiveAddress> addresses = addressService.findByUserId(userId);
		List<Integer> ids = new ArrayList<Integer>();
		for (ReceiveAddress address : addresses) {
			ids.add(address.getId());
		}
		int result = addressService.updateSetDefault(ids, addressId);
		return result;
	}

	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public String delete(Model model, @RequestParam("addressId") Integer addressId) {
		Integer userId = UserWxUtils.getCurrUserId();
		addressService.delete(userId, addressId);
		List<ReceiveAddress> addresses = addressService.findByUserId(userId);
		//删除后如果没有默认地址，把第一个设置为默认地址
		boolean hasDefault= false;
		for (ReceiveAddress address : addresses) {
			if(address.getIsDefault()){
				hasDefault = true;
				break;
			}
		}
		if(!hasDefault&&addresses.size()>0){
			ReceiveAddress address = addresses.get(0);
			address.setIsDefault(true);
			addressService.saveReceiveAddress(address, userId);
		}
		return getRedirectUrl("detail");
	}

	@ModelAttribute("preloadEntity")
	public ReceiveAddress getEntity(
			@RequestParam(value = "id", required = false) Integer id) {
		if (null != id) {
			return addressService.get(id);
		}
		ReceiveAddress receiveAddress = new ReceiveAddress();
		receiveAddress.setIsDefault(false);
		return receiveAddress;
	}

}
