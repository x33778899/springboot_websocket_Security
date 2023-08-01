package com.jacob.springcloud.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacob.springcloud.dto.UserResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        UserResponse responses = new UserResponse(); // Use UserResponse instead of Response
        responses.setError("必須擁有token或token錯誤");
        responses.setStatus("403");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responses));
    }
}
