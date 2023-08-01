package com.jacob.springcloud.service;



import com.jacob.springcloud.model.User;

public interface UserService {
	User save(User user);
	User findByAccountAndPassword(String account, String password);
	 User findByAccount(String account);
	
}
