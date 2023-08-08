package com.jacob.springcloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jacob.springcloud.config.RedisTokenRepository;
import com.jacob.springcloud.dao.UserRepository;
import com.jacob.springcloud.dto.LoginResponse;
import com.jacob.springcloud.model.User;
import com.jacob.springcloud.service.UserService;
import com.jacob.springcloud.utils.AccountLockManager;
import com.jacob.springcloud.utils.JwtTokenUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private RedisTokenRepository redisTokenRepository;

	public UserController(UserService userService, RedisTokenRepository redisTokenRepository) {
		this.userService = userService;
		this.redisTokenRepository = redisTokenRepository;
	}

	@PostMapping("/register")
	public ResponseEntity<LoginResponse> registerUser(@RequestBody User user) {
		LoginResponse response = new LoginResponse();

		try {
			User userContainer = new User();
			userContainer.setAccount(user.getAccount());
			userContainer.setPassword(user.getPassword());
			userContainer.setRole("ROLE_USER");
			userService.save(userContainer);

			// Registration success
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("registration success");
			response.setLoginSuccess(true);
			response.setToken(null);

			return ResponseEntity.ok(response);
		} catch (DataIntegrityViolationException e) {
			// Handle the duplicate account scenario here
			e.printStackTrace();
			System.out.println("Duplicate account: " + user.getAccount());

			// Registration failure - account already exists
			response.setStatusCode(HttpStatus.BAD_REQUEST.value());
			response.setMessage("Account already exists.");
			response.setLoginSuccess(false);
			response.setToken(null);

			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			// Handle other exceptions here
			e.printStackTrace();
			System.out.println("Other exception occurred.");

			// Registration failure due to other errors
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage("Internal Server Error");
			response.setLoginSuccess(false);
			response.setToken(null);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody User user) {
		String account = user.getAccount();
		String password = user.getPassword();

		User userContainer = userService.findByAccountAndPassword(account, password);
		if (userContainer == null) {
			// 登入失敗的處理邏輯
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setStatusCode(401);
			loginResponse.setMessage("Login failed: Invalid account or password.");
			loginResponse.setLoginSuccess(false);
			loginResponse.setToken(null); // 登录失败时，将User设为null
			return ResponseEntity.ok(loginResponse);
		} else {
			long expiration = 24 * 60 * 60 * 1000; // Set the expiration time in seconds as desired

			String token = jwtTokenUtil.createToken(user.getAccount(), "ROLE_USER", true);

			String tokenBody = jwtTokenUtil.getUsername(token);
			System.out.println(tokenBody);

			// 登入成功的處理邏輯
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setStatusCode(200);
			loginResponse.setMessage("login success");
			loginResponse.setLoginSuccess(true);
			loginResponse.setToken(token); // 登录成功时，设置User属性为登录用户

			// Redis
			redisTokenRepository.saveToken(token);

			return ResponseEntity.ok(loginResponse);
		}
	}

	/**
	 * 用來檢查帳號是否重複
	 *
	 */
	@GetMapping("/accountcheck")
	public ResponseEntity<LoginResponse> accountcheck(@RequestParam String account) {
		if (AccountLockManager.isAccountLocked(account)) {
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setStatusCode(403); // 403: Forbidden
			loginResponse.setMessage("account already exists and is locked");
			loginResponse.setLoginSuccess(false);
			loginResponse.setToken(null);
			return ResponseEntity.ok(loginResponse);
		} else {
			// 首先進行帳號檢查，如果存在則鎖定帳號
			User userContainer = userService.findByAccount(account);
			if (userContainer != null) {
				LoginResponse loginResponse = new LoginResponse();
				loginResponse.setStatusCode(409);
				loginResponse.setMessage("account already exists");
				loginResponse.setLoginSuccess(false);
				loginResponse.setToken(null);
				return ResponseEntity.ok(loginResponse);
			} else {
				AccountLockManager.lockAccount(account); // Lock the account
				LoginResponse loginResponse = new LoginResponse();
				loginResponse.setStatusCode(200);
				loginResponse.setMessage("account not in use");
				loginResponse.setLoginSuccess(false);
				loginResponse.setToken(null);
				return ResponseEntity.ok(loginResponse);
			}
		}
	}

}
