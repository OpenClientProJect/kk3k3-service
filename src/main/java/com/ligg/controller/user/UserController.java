package com.ligg.controller.user;

import com.ligg.entity.User;
import com.ligg.service.UserService;
import com.ligg.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户控制器，处理用户信息相关操作
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    


    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${server.port}")
    private String serverPort;

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
     * 上传用户头像
     * @param file 头像图片文件
     * @param request HTTP请求
     * @return 上传结果
     */
    @PostMapping("/avatar/upload")
    public ResponseResult<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 从请求属性中获取当前登录用户（由JWT拦截器设置）
        User currentUser = (User) request.getAttribute("user");
        if (currentUser == null) {
            return ResponseResult.error("未登录或登录已过期");
        }
        
        log.info("当前用户上传头像: {}", currentUser.getUsername());
        
        if (file.isEmpty()) {
            return ResponseResult.error("请选择头像图片");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 检查文件类型
            if (!isValidImageExtension(fileExtension)) {
                return ResponseResult.error("不支持的图片格式，请上传jpg、png等格式");
            }

            // 生成新的文件名
            String newFileName = UUID.randomUUID() + fileExtension;

            // 按日期创建子目录
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = "avatars/" + datePath + "/" + newFileName;

            // 确保目录存在
            File uploadDir = new File(uploadPath + "/avatars/" + datePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件
            Path path = Paths.get(uploadPath + "/" + relativePath);
            Files.write(path, file.getBytes());

            // 构建访问URL
            String baseUrl = "http://localhost:" + serverPort;
            String avatarUrl = baseUrl + "/images/" + relativePath;
            
            log.info("头像已上传，URL: {}", avatarUrl);
            
            // 更新用户头像URL
            User updateUser = new User();
            updateUser.setId(currentUser.getId());
            updateUser.setAvatarUrl(avatarUrl);
            userService.updateUser(updateUser);
            
            log.info("用户头像已更新: {}", currentUser.getId());

            // 返回头像访问地址
            Map<String, String> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);

            return ResponseResult.success(result);
        } catch (IOException e) {
            log.error("头像上传失败", e);
            return ResponseResult.error("头像上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查是否为有效的图片扩展名
     * @param extension 扩展名
     * @return 是否有效
     */
    private boolean isValidImageExtension(String extension) {
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
        for (String valid : validExtensions) {
            if (valid.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}