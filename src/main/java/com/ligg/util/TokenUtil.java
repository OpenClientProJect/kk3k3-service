package com.ligg.util;

import com.ligg.entity.User;
import com.ligg.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Token工具类，用于从请求中获取用户信息
 */
@Component
public class TokenUtil {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.header}")
    private String tokenHeader;
    
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
    
    /**
     * 从请求中获取用户信息
     * @param request HTTP请求
     * @return 用户对象，如果获取失败则返回null
     */
    public User getUserFromRequest(HttpServletRequest request) {
        // 从请求头中获取token
        String authHeader = request.getHeader(tokenHeader);
        
        // 检查token是否存在且格式正确
        if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
            return null;
        }
        
        // 去除token前缀
        String token = authHeader.substring(tokenPrefix.length());
        
        try {
            // 解析token获取用户名
            String username = jwtTokenUtil.getUsernameFromToken(token);
            
            // 根据用户名查询用户信息
            return userMapper.findByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 从请求中获取用户ID
     * @param request HTTP请求
     * @return 用户ID，如果获取失败则返回null
     */
    public Long getUserIdFromRequest(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        return user != null ? user.getId() : null;
    }
} 