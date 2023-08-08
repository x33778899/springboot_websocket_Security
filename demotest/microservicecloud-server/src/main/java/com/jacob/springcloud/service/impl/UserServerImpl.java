package com.jacob.springcloud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacob.springcloud.dao.UserRepository;
import com.jacob.springcloud.model.User;
import com.jacob.springcloud.service.UserService;
import com.jacob.springcloud.service.impl.tx.UserTx;

@Service // 添加這個標記
public class UserServerImpl implements UserService {

	@Autowired
	private UserTx userTx;

	public User save(User user) {
		User result = null;
		result = userTx.save(user);
		return result;
	}

	@Override
	public User findByAccountAndPassword(String account, String password) {
		User result = null;
		result = userTx.findByAccountAndPassword(account, password);
		return result;
	}

	@Override
	public User findByAccount(String account) {
		User result = null;
		result = userTx.findByAccount(account);
		return result;
	}

	@Override
	public User findByAccountWithLock(String account) {
		User result = null;
		result = userTx.findByAccountWithLock(account);
		return result;
	}
}
