package com.jacob.springcloud.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import static com.jacob.springcloud.SecurityConstants.SecurityConstants.*;
import com.jacob.springcloud.config.RedisTokenRepository;
import com.jacob.springcloud.utils.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    private final RedisTokenRepository redisTokenRepository;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, RedisTokenRepository redisTokenRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTokenRepository = redisTokenRepository;
    }
    /**
     * 根據Authorization判斷token
     * 並且放行SecurityConstants指定的路徑
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();


       //放行WebSocket連接的請求
        if (requestURI.contains("/websocket-example")) {
//            System.out.println("WebSocket connection request to " + requestURI + " is allowed.");
            chain.doFilter(request, response);
            return;
        }


        //放行路徑
        if (requestURI.equals(REGISTER_URL.getUrl()) || requestURI.equals(LOGIN_URL.getUrl()) ||
                 requestURI.equals(LOGINPAGE_URL.getUrl())) {
            chain.doFilter(request, response);
            return;
        }
        
        // 放行靜態資源 (JS, CSS, images, etc.)
        if (requestURI.endsWith(".js") || requestURI.endsWith(".css") || requestURI.endsWith(".jpg") || requestURI.endsWith(".png")) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
//        System.out.println("Authorization Header: " + authorizationHeader);
//        System.out.println("requestURI    " + requestURI);
//        System.out.println("token    " + request.getParameter("token"));

        // 判斷token，優先使用Authorization header中的token
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 取得標頭為	"Bearer " 前輟
            token = authorizationHeader.substring(7);
        } else {
            // 檢查參數token
            token = request.getParameter("token");
        }

     // 如果token存在，進行驗證
        if (token != null) {
        	// 從Redis檢查
            if (!jwtTokenUtil.isTokenExpired(token) && redisTokenRepository.isTokenValid(token)) {
                // If the token is valid, get the username and user role from it
                String username = jwtTokenUtil.getUsername(token);
                String role = jwtTokenUtil.getUserRole(token);
                // Set authentication in SecurityContextHolder
                if (username != null && role != null) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                            AuthorityUtils.createAuthorityList(role));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}
