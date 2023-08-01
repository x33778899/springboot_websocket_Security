//package com.jacob.springcloud.utils;
//
//import java.util.Date;
//import java.util.HashMap;
//
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//@Component
//public class JwtTokenUtils {
//
//    public static final String TOKEN_HEADER = "Authorization";
//    public static final String TOKEN_PREFIX = "Bearer ";
//
//    private static final String SECRET = "jwtsecretdemo";
//    private static final String ISS = "echisan";
//
//    // 角色的key
//    private static final String ROLE_CLAIMS = "rol";
//
//    // 過期時間是3600秒，既是1個小時
//    private static final long EXPIRATION = 3600L;
//
//    // 選擇了記住我之後的過期時間為7天
//    private static final long EXPIRATION_REMEMBER = 604800L;
//
//    // 創建token
//    public static String createToken(String username,String role, boolean isRememberMe) {
//        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
//        HashMap<String, Object> map = new HashMap<>();
//        map.put(ROLE_CLAIMS, role);
//        return Jwts.builder()
//                .signWith(SignatureAlgorithm.HS512, SECRET)
//                .setClaims(map)
//                .setIssuer(ISS)
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
//                .compact();
//    }
//    
//
//    
//
//    // 從token中獲取用戶名
//    public static String getUsername(String token){
//        return getTokenBody(token).getSubject();
//    }
//
//    // 獲取用戶角色
//    public static String getUserRole(String token){
//        return (String) getTokenBody(token).get(ROLE_CLAIMS);
//    }
//
//    // 是否已過期
//    public static boolean isExpiration(String token) {
//        try {
//            return getTokenBody(token).getExpiration().before(new Date());
//        } catch (ExpiredJwtException e) {
//            return true;
//        }
//    }
//
//    private static Claims getTokenBody(String token){
//        return Jwts.parser()
//                .setSigningKey(SECRET)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}