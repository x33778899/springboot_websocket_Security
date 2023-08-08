package com.jacob.springcloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jacob.springcloud.filter.JwtTokenFilter;
import com.jacob.springcloud.handler.JWTAccessDeniedHandler;
import com.jacob.springcloud.handler.JWTAuthenticationEntryPoint;
import static com.jacob.springcloud.securityConstants.SecurityConstants.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenFilter jwtTokenFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.POST, REGISTER_URL.getUrl(), LOGIN_URL.getUrl()).permitAll() // 登入及註冊頁面不處理
				.antMatchers(HttpMethod.GET, LOGINPAGE_URL.getUrl(), WEBSOCKET_CONNECT_URL.getUrl(),ACCOUNT_CHECK_URL.getUrl()).permitAll() // authorizationProcessingPage放行
				.antMatchers("/webjars/**", "/css/**", "/js/**", "/websocket-example/**", "/websocket-example")
				.permitAll() // Allow access to webjars, CSS, and JS resources
				.anyRequest().authenticated().and()
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
				.authenticationEntryPoint(new JWTAuthenticationEntryPoint())
				.accessDeniedHandler(new JWTAccessDeniedHandler());
	}

}
