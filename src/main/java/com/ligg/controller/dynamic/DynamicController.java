package com.ligg.controller.dynamic;

import com.ligg.entity.Dynamic;
import com.ligg.entity.DynamicComment;
import com.ligg.entity.User;
import com.ligg.service.DynamicService;
import com.ligg.util.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/dynamic")
public class DynamicController {

    @Autowired
    private DynamicService dynamicService;

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 发布动态
     */
    @PostMapping("/publish")
    public ResponseResult publishDynamic(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        // 从 user 对象中获取 userId
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return ResponseResult.error("用户未登录");
        }
        Long userId = user.getId();
        String content = (String) params.get("content");
        String images = (String) params.get("images");
        Long videoId = params.get("videoId") != null ? Long.valueOf(params.get("videoId").toString()) : null;

        if (content == null || content.trim().isEmpty()) {
            return ResponseResult.error("动态内容不能为空");
        }

        Dynamic dynamic = dynamicService.publishDynamic(userId, content, images, videoId);

        return ResponseResult.success(dynamic);
    }

    /**
     * 上传动态图片
     */
    @PostMapping("/upload-image")
    public ResponseResult uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 从 user 对象中获取 userId
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return ResponseResult.error("用户未登录");
        }
        Long userId = user.getId();

        if (file.isEmpty()) {
            return ResponseResult.error("文件不能为空");
        }

        try {
            // 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 生成新文件名（按日期存储）
            LocalDate now = LocalDate.now();
            String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String newFileName = UUID.randomUUID().toString() + extension;

            // 创建目录
            String dirPath = uploadPath + "/dynamic/" + datePath;
            Path path = Paths.get(dirPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            // 保存文件
            String filePath = dirPath + "/" + newFileName;
            file.transferTo(new File(filePath));

            // 返回文件访问URL
            String imageUrl = "/dynamic/images/" + datePath + "/" + newFileName;

            return ResponseResult.success(imageUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error("图片上传失败");
        }
    }

    /**
     * 获取动态列表
     */
    @GetMapping("/list")
    public ResponseResult getDynamicList(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         HttpServletRequest request) {
        Long userId = null;
        try {
            User user = (User) request.getAttribute("user");
            if (user != null) {
                userId = user.getId();
            }
        } catch (Exception e) {
            // 未登录用户，userId为null
        }

        Map<String, Object> result = dynamicService.getLatestDynamics(page, size, userId);

        return ResponseResult.success(result);
    }

    /**
     * 获取用户动态列表
     */
    @GetMapping("/user/{userId}")
    public ResponseResult getUserDynamics(@PathVariable("userId") Long targetUserId,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size) {

        Map<String, Object> result = dynamicService.getUserDynamics(targetUserId, page, size);

        return ResponseResult.success(result);
    }

    /**
     * 获取动态详情
     */
    @GetMapping("/{dynamicId}")
    public ResponseResult getDynamicDetail(@PathVariable("dynamicId") Long dynamicId, HttpServletRequest request) {
        Long userId = null;
        try {
            User user = (User) request.getAttribute("user");
            if (user != null) {
                userId = user.getId();
            }
        } catch (Exception e) {
            // 未登录用户，userId为null
        }

        Map<String, Object> result = dynamicService.getDynamicDetail(dynamicId, userId);

        if (result == null) {
            return ResponseResult.error("动态不存在");
        }

        return ResponseResult.success(result);
    }

    /**
     * 点赞/取消点赞动态
     */
    @PostMapping("/like/{dynamicId}")
    public ResponseResult likeDynamic(@PathVariable("dynamicId") Long dynamicId, HttpServletRequest request) {
        // 从 user 对象中获取 userId
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return ResponseResult.error("用户未登录");
        }
        Long userId = user.getId();

        boolean isLiked = dynamicService.likeDynamic(dynamicId, userId);

        Map<String, Object> data = new HashMap<>();
        data.put("liked", isLiked);

        return ResponseResult.success(data);
    }

    /**
     * 删除动态
     */
    @DeleteMapping("/{dynamicId}")
    public ResponseResult deleteDynamic(@PathVariable("dynamicId") Long dynamicId, HttpServletRequest request) {
        // 从 user 对象中获取 userId
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return ResponseResult.error("用户未登录");
        }
        Long userId = user.getId();

        boolean success = dynamicService.deleteDynamic(dynamicId, userId);

        if (success) {
            return ResponseResult.success("删除成功");
        } else {
            return ResponseResult.error("删除失败");
        }
    }

    /**
     * 获取动态评论
     */
    @GetMapping("/{dynamicId}/comments")
    public ResponseResult getDynamicComments(@PathVariable("dynamicId") Long dynamicId,
                                             @RequestParam(value = "page", defaultValue = "1") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {

        Map<String, Object> result = dynamicService.getDynamicComments(dynamicId, page, size);


        return ResponseResult.success(result);
    }

    /**
     * 发表评论
     */
    @PostMapping("/{dynamicId}/comment")
    public ResponseResult commentDynamic(@PathVariable("dynamicId") Long dynamicId,
                                         @RequestBody Map<String, Object> params,
                                         HttpServletRequest request) {
        // 从 user 对象中获取 userId
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return ResponseResult.error("用户未登录");
        }
        Long userId = user.getId();
        String content = (String) params.get("content");
        Long parentId = params.get("parentId") != null ? Long.valueOf(params.get("parentId").toString()) : null;

        if (content == null || content.trim().isEmpty()) {
            return ResponseResult.error("评论内容不能为空");
        }

        DynamicComment comment = dynamicService.comment(dynamicId, userId, content, parentId);

        if (comment == null) {
            return ResponseResult.error("评论失败");
        }

        return ResponseResult.success(comment);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") Long commentId, HttpServletRequest request) {
        // 从 user 对象中获取 userId
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return ResponseResult.error("用户未登录");
        }
        Long userId = user.getId();

        boolean success = dynamicService.deleteComment(commentId, userId);

        if (success) {
            return ResponseResult.success("删除成功");
        } else {
            return ResponseResult.error("删除失败");
        }
    }
} 