package com.ligg.controller.user;

import com.ligg.entity.User;
import com.ligg.entity.UserSubscription;
import com.ligg.service.UserService;
import com.ligg.service.UserSubscriptionService;
import com.ligg.util.JwtTokenUtil;
import com.ligg.util.ResponseResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户控制器，处理用户信息相关操作
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserSubscriptionService userSubscriptionService;

    /**
     * 获取当前登录用户信息
     * @param request 请求对象
     * @return 用户信息
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser(jakarta.servlet.http.HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // 从请求属性中获取用户信息（由JWT拦截器设置）
        User user = (User) request.getAttribute("user");
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
            result.put("success", true);
            result.put("user", user);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "未获取到用户信息");
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable Long id) {
        log.info("获取用户信息: {}", id);
        
        Map<String, Object> result = new HashMap<>();
        User user = userService.getUserById(id);
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
            result.put("success", true);
            result.put("user", user);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 用户信息
     * @param request HTTP请求对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id, 
            @RequestBody User user, 
            HttpServletRequest request) {
        log.info("更新用户信息: {}", id);
        
        // 从请求属性中获取当前登录用户
        User currentUser = (User) request.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        // 验证当前用户是否有权限修改此用户信息（只能修改自己的信息）
        if (currentUser == null || !currentUser.getId().equals(id)) {
            result.put("success", false);
            result.put("message", "无权限修改他人信息");
            return ResponseEntity.status(403).body(result);
        }
        
        // 只允许修改部分字段，防止恶意修改
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setNickname(user.getNickname());
        updateUser.setAvatarUrl(user.getAvatarUrl());
        // 可以根据需要添加其他允许修改的字段
        
        boolean success = userService.updateUser(updateUser);
        
        if (success) {
            result.put("success", true);
            result.put("message", "更新成功");
        } else {
            result.put("success", false);
            result.put("message", "更新失败");
        }
        
        return ResponseEntity.ok(result);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        log.info("删除用户: {}", id);
        
        boolean success = userService.deleteUser(id);
        
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("success", true);
            result.put("message", "删除成功");
        } else {
            result.put("success", false);
            result.put("message", "删除失败");
        }
        
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有用户
     * @return 用户列表
     */
    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("获取所有用户");
        
        List<User> users = userService.getAllUsers();
        // 不返回密码
        users.forEach(user -> user.setPassword(null));
        
        return ResponseEntity.ok(users);
    }

    /**
     * 关注用户
     * @param targetUserId 被关注用户ID
     * @param request 请求对象
     * @return 关注结果
     */
    @PostMapping("/subscribe/{targetUserId}")
    public ResponseResult<String> subscribeUser(@PathVariable Long targetUserId, jakarta.servlet.http.HttpServletRequest request) {
        // 从请求属性中获取当前用户信息（由JWT拦截器设置）
        User currentUser = (User) request.getAttribute("user");
        if (currentUser == null) {
            return ResponseResult.error("未登录");
        }
        
        // 检查目标用户是否存在
        User targetUser = userService.getUserById(targetUserId);
        if (targetUser == null) {
            return ResponseResult.error("目标用户不存在");
        }
        
        // 执行关注操作
        boolean success = userSubscriptionService.subscribe(currentUser.getId(), targetUserId);
        if (success) {
            return ResponseResult.success("关注成功");
        } else {
            return ResponseResult.error("关注失败，可能已经关注过该用户");
        }
    }
    
    /**
     * 取消关注用户
     * @param targetUserId 被关注用户ID
     * @param request 请求对象
     * @return 取消关注结果
     */
    @DeleteMapping("/subscribe/{targetUserId}")
    public ResponseResult<String> unsubscribeUser(@PathVariable Long targetUserId, jakarta.servlet.http.HttpServletRequest request) {
        // 从请求属性中获取当前用户信息（由JWT拦截器设置）
        User currentUser = (User) request.getAttribute("user");
        if (currentUser == null) {
            return ResponseResult.error("未登录");
        }
        
        // 执行取消关注操作
        boolean success = userSubscriptionService.unsubscribe(currentUser.getId(), targetUserId);
        if (success) {
            return ResponseResult.success("取消关注成功");
        } else {
            return ResponseResult.error("取消关注失败，可能未关注该用户");
        }
    }
    
    /**
     * 检查是否已关注用户
     * @param targetUserId 目标用户ID
     * @param request HTTP请求
     * @return 是否已关注
     */
    @GetMapping("/is-subscribed/{targetUserId}")
    public ResponseResult<Boolean> isSubscribed(@PathVariable Long targetUserId, jakarta.servlet.http.HttpServletRequest request) {
        log.info("检查是否已关注用户，目标用户ID: {}", targetUserId);
        
        // 从请求属性中获取当前用户信息（由JWT拦截器设置）
        User currentUser = (User) request.getAttribute("user");
        
        if (currentUser == null) {
            log.warn("用户未登录，返回401状态码");
            return ResponseResult.error(401, "未登录");
        }
        
        log.info("当前用户: {}, 目标用户: {}", currentUser.getUsername(), targetUserId);
        boolean isSubscribed = userSubscriptionService.isSubscribed(currentUser.getId(), targetUserId);
        log.info("关注状态: {}", isSubscribed);
        
        return ResponseResult.success(isSubscribed);
    }
    
    /**
     * 获取用户关注列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 关注列表
     */
    @GetMapping("/{userId}/subscriptions")
    public ResponseResult<Map<String, Object>> getUserSubscriptions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // 获取用户关注列表
        List<UserSubscription> subscriptions = userSubscriptionService.getSubscriptions(userId, page, size);
        
        // 获取关注用户的详细信息
        List<Long> targetUserIds = subscriptions.stream()
                .map(UserSubscription::getTargetUserId)
                .collect(Collectors.toList());
        
        List<User> targetUsers = new ArrayList<>();
        for (Long targetUserId : targetUserIds) {
            User user = userService.getUserById(targetUserId);
            if (user != null) {
                user.setPassword(null); // 不返回密码
                targetUsers.add(user);
            }
        }
        
        // 统计总关注数
        int total = userSubscriptionService.countSubscriptions(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("subscriptions", targetUsers);
        result.put("total", total);
        result.put("currentPage", page);
        result.put("pageSize", size);
        
        return ResponseResult.success(result);
    }
    
    /**
     * 获取用户粉丝列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 粉丝列表
     */
    @GetMapping("/{userId}/subscribers")
    public ResponseResult<Map<String, Object>> getUserSubscribers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // 获取用户粉丝列表
        List<UserSubscription> subscribers = userSubscriptionService.getSubscribers(userId, page, size);
        
        // 获取粉丝用户的详细信息
        List<Long> subscriberIds = subscribers.stream()
                .map(UserSubscription::getUserId)
                .collect(Collectors.toList());
        
        List<User> subscriberUsers = new ArrayList<>();
        for (Long subscriberId : subscriberIds) {
            User user = userService.getUserById(subscriberId);
            if (user != null) {
                user.setPassword(null); // 不返回密码
                subscriberUsers.add(user);
            }
        }
        
        // 统计总粉丝数
        int total = userSubscriptionService.countSubscribers(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("subscribers", subscriberUsers);
        result.put("total", total);
        result.put("currentPage", page);
        result.put("pageSize", size);
        
        return ResponseResult.success(result);
    }
    
    /**
     * 获取用户的粉丝数
     * @param userId 用户ID
     * @return 粉丝数
     */
    @GetMapping("/{userId}/subscriber-count")
    public ResponseResult<Integer> getSubscriberCount(@PathVariable Long userId) {
        int count = userSubscriptionService.countSubscribers(userId);
        return ResponseResult.success(count);
    }

    /**
     * 更新用户信息
     */
//    @PutMapping("/updateUserInfo")
//    public ResponseResult<?> updateUserInfo(HttpServletRequest request){
//        String authorization = request.getHeader("authorization");
//        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//        Claims allClaimsFromToken = jwtTokenUtil.getAllClaimsFromToken(authorization);
//        Long userId = (Long) allClaimsFromToken.get("id");
//
//        userService.updateUser()
//    }
}