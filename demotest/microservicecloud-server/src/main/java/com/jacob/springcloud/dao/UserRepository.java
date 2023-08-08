package com.jacob.springcloud.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jacob.springcloud.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * 註冊
	 */

	User save(User user);

	/**
	 * 查詢帳號密碼
	 */

	User findByAccountAndPassword(String account, String password);

	/**
	 * 預防帳號重複
	 */
	User findByAccount(String account);

}
