package com.jacob.springcloud.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacob.springcloud.dto.UserResponse;

public class JWTAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AccessDeniedException e) throws IOException, ServletException {
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json; charset=utf-8");
		httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		UserResponse responses = new UserResponse(); // Use UserResponse instead of Response
		responses.setError("沒訪問權限");
		responses.setStatus("401");
		httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(responses));
	}
}
