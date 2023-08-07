package com.jacob.springcloud.service.impl.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jacob.springcloud.dao.UserRepository;
import com.jacob.springcloud.model.User;

@Service
public class UserTx {
	
	@Autowired
	UserRepository userRepository;
	
	public User save(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}
	public User findByAccountAndPassword(String account, String password) {
		// TODO Auto-generated method stub
		return userRepository.findByAccountAndPassword(account, password);
	}
	public User findByAccount(String account) {
		// TODO Auto-generated method stub
		return userRepository.findByAccount(account);
	}

}
