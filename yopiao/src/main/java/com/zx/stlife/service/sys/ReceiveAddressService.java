package com.zx.stlife.service.sys;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zx.stlife.entity.sys.ReceiveAddress;
import com.zx.stlife.entity.sys.User;
import com.zx.stlife.repository.jpa.sys.ReceiveAddressDao;
import com.zx.stlife.repository.jpa.sys.UserDao;

/**
 * 收获地址业务类
 * 
 * @author micheal cao
 */
// Spring Service Bean的标识.
@Component
@Transactional(readOnly = true)
public class ReceiveAddressService {

	private static Logger logger = LoggerFactory
			.getLogger(ReceiveAddressService.class);
	@Autowired
	private ReceiveAddressDao receiveAddressDao;
	@Autowired
	private UserDao userDao;

	public ReceiveAddress get(Integer id){
		return receiveAddressDao.findOne(id);
	}

	@Transactional
	public ReceiveAddress saveReceiveAddress(ReceiveAddress receiveAddress,
			Integer userId) {
		receiveAddress.setUser(new User(userId));
		return receiveAddressDao.save(receiveAddress);
	}

	/**
	 * 根据用户 ID获取收获地址信息
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ReceiveAddress> findByUserId(Integer userId) {
		return receiveAddressDao.findByUserId(userId);
	}

	/**
	 * 查询用户设置默认收货地址
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public ReceiveAddress findByState(Integer userId) {
		return receiveAddressDao.findByState(userId);
	}

	/**
	 * 查询具体收货地址信息
	 * 
	 * @param addressId
	 * @return
	 */
	@Transactional(readOnly = true)
	public ReceiveAddress findReceiveAddressInfo(Integer addressId) {
		return receiveAddressDao.findOne(addressId);
	}

	/**
	 * 删除收获地址信息
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	@Transactional
	public boolean delete(Integer userId, Integer id) {
		try {
			int result = receiveAddressDao.deleteReceiveAddress(userId, id);
			if (result > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional(readOnly=true)
	public User findUser(Integer userId){
		return userDao.findOne(userId);
	}

	/**
	 * 设置默认地址
	 * @return
	 */
	@Transactional
	public int updateSetDefault(List<Integer> ids,Integer addressId) {
		int result = receiveAddressDao.updateSetAllFalse(ids);//全设置为false
		if(result<0){
			return -1;
		}
		result = receiveAddressDao.updateSetDefault(addressId);//根据id设置默认地址
		if(result<0){
			return -1;
		}
		return 1;
	}
}
