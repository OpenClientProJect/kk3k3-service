package com.ligg.controller;

import com.ligg.entity.User;
import com.ligg.service.UserService;
import com.ligg.util.JwtTokenUtil;
import com.ligg.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户控制器，处理登录、注册、登出等操作
 */
@RestController
@RequestMapping("/api/account")
@Slf4j
public class AccountController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    /**
     * 用户登录
     * @param loginInfo 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginInfo) {
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");
        
        log.info("用户登录: {}", username);
        
        User user = userService.login(username, password);
        Map<String, Object> result = new HashMap<>();
        
        if (user != null) {
            // 生成JWT令牌
            String token = jwtTokenUtil.generateToken(user);
            
            // 将用户信息存入Redis，键为username，过期时间与JWT一致
            String redisKey = "user:token:" + username;
            redisUtil.set(redisKey, token, jwtExpiration);
            
            // 登录成功，返回用户信息（密码置空）
            user.setPassword(null);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("user", user);
            result.put("token", token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", tokenPrefix + token);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(result);
        } else {
            // 登录失败
            result.put("success", false);
            result.put("message", "用户名或密码错误");
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        log.info("用户注册: {}", user.getUsername());
        
        Map<String, Object> result = new HashMap<>();
        
        // 检查用户名是否已存在
        if (userService.getUserByUsername(user.getUsername()) != null) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return ResponseEntity.ok(result);
        }
        
        boolean success = userService.register(user);
        
        if (success) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "注册失败");
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 用户登出
     * @param token 令牌
     * @return 登出结果
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        
        if (token != null && token.startsWith(tokenPrefix)) {
            String jwtToken = token.substring(tokenPrefix.length());
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            
            // 从Redis中删除令牌
            String redisKey = "user:token:" + username;
            redisUtil.delete(redisKey);
            
            result.put("success", true);
            result.put("message", "登出成功");
        } else {
            result.put("success", false);
            result.put("message", "无效的令牌");
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 刷新令牌
     * @param token 原令牌
     * @return 新令牌
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        
        if (token != null && token.startsWith(tokenPrefix)) {
            String jwtToken = token.substring(tokenPrefix.length());
            
            try {
                // 从令牌中获取用户名
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                
                // 检查Redis中是否存在对应的令牌
                String redisKey = "user:token:" + username;
                String storedToken = (String) redisUtil.get(redisKey);
                
                // 如果Redis中存在令牌且匹配，则刷新令牌
                if (storedToken != null && storedToken.equals(jwtToken)) {
                    // 获取用户信息
                    User user = userService.getUserByUsername(username);
                    
                    if (user != null) {
                        // 生成新令牌
                        String newToken = jwtTokenUtil.generateToken(user);
                        
                        // 更新Redis中的令牌
                        redisUtil.set(redisKey, newToken, jwtExpiration);
                        
                        result.put("success", true);
                        result.put("message", "令牌刷新成功");
                        result.put("token", newToken);
                        
                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Authorization", tokenPrefix + newToken);
                        
                        return ResponseEntity.ok()
                                .headers(headers)
                                .body(result);
                    }
                }
            } catch (Exception e) {
                log.error("刷新令牌失败", e);
            }
        }
        
        result.put("success", false);
        result.put("message", "令牌刷新失败");
        return ResponseEntity.ok(result);
    }
}
