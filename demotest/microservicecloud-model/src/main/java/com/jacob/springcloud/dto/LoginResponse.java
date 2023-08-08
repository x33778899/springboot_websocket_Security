package com.jacob.springcloud.dto;


public class LoginResponse {
    private int statusCode;
    private String message;
    private boolean loginSuccess;
    private String token; 
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isLoginSuccess() {
		return loginSuccess;
	}
	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}



}