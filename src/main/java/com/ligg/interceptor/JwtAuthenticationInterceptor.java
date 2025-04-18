package com.ligg.interceptor;

import com.ligg.entity.User;
import com.ligg.service.UserService;
import com.ligg.util.JwtTokenUtil;
import com.ligg.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT认证拦截器
 */
@Component
@Slf4j
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;
    
    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 对预检请求放行
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        
        // 从请求头中获取令牌
        final String requestHeader = request.getHeader(tokenHeader);
        log.info("接收到的Authorization头: {}", requestHeader);
        
        // 如果请求头中没有令牌，返回401
        if (requestHeader == null || requestHeader.trim().isEmpty()) {
            log.error("令牌缺失");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = requestHeader.startsWith(tokenPrefix) ?
            requestHeader.substring(tokenPrefix.length()) : requestHeader;
        log.info("解析出的token: {}", token);
        
        try {
            // 从令牌中获取用户名
            String username = jwtTokenUtil.getUsernameFromToken(token);
            log.info("从token中解析出的用户名: {}", username);
            
            // 检查Redis中是否存在对应的令牌
            String redisKey = "user:token:" + username;
            String storedToken = (String) redisUtil.get(redisKey);
            log.info("Redis中存储的token: {}", storedToken);
            
            // 如果Redis中没有令牌或令牌不匹配，返回401
            if (storedToken == null || !storedToken.equals(token)) {
                log.error("令牌已失效或已被注销, storedToken={}", storedToken);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            // 从数据库中获取用户信息
            User user = userService.getUserByUsername(username);
            log.info("从数据库获取到的用户信息: {}", user != null ? user.getUsername() : "null");
            
            // 如果用户不存在，返回401
            if (user == null) {
                log.error("用户不存在: {}", username);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            // 验证令牌是否有效
            boolean isValid = jwtTokenUtil.validateToken(token, user);
            log.info("token验证结果: {}", isValid);
            if (!isValid) {
                log.error("令牌无效");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            // 将用户信息放入请求属性中，供后续使用
            request.setAttribute("user", user);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("令牌已过期: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } catch (SignatureException | MalformedJwtException e) {
            log.error("令牌签名无效或格式错误: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } catch (Exception e) {
            log.error("令牌验证异常: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return false;
        }
    }
} 