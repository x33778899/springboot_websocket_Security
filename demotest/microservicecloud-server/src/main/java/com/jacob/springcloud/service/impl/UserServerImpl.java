package com.jacob.springcloud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacob.springcloud.dao.UserRepository;
import com.jacob.springcloud.model.User;
import com.jacob.springcloud.service.UserService;

@Service // 添加這個標記
public class UserServerImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User save(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}
	@Override
	public User findByAccountAndPassword(String account, String password) {
		// TODO Auto-generated method stub
		return userRepository.findByAccountAndPassword(account, password);
	}
	@Override
	public User findByAccount(String account) {
		// TODO Auto-generated method stub
		return userRepository.findByAccount(account);
	}
}
