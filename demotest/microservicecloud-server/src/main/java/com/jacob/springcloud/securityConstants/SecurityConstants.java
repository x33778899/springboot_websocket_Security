package com.jacob.springcloud.securityConstants;

public enum SecurityConstants {

	REGISTER_URL("/register"), LOGIN_URL("/login"), LOGINPAGE_URL("/loginPage"),
	WEBSOCKET_CONNECT_URL("/websocket-example/info");

	private final String url;

	SecurityConstants(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
