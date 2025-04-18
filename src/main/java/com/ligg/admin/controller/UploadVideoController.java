package com.ligg.admin.controller;

import com.ligg.admin.service.AdminVideoService;
import com.ligg.entity.User;
import com.ligg.entity.Video;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/video")
public class UploadVideoController {

    @Autowired
    private AdminVideoService adminVideoService;

    @Value("${video.upload.path}")
    private String videoUploadPath;

    @Value("${video.cover.path}")
    private String coverUploadPath;

    @Value("${video.access.url}")
    private String videoAccessUrl;

    /**
     * 上传视频文件
     * @param file 视频文件
     * @param request HTTP请求
     * @return 上传结果
     */
    @PostMapping("/upload")
    public ResponseResult<Map<String, String>> uploadVideo(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResponseResult.error("请选择视频文件");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 检查文件类型
            if (!isValidVideoExtension(fileExtension)) {
                return ResponseResult.error("不支持的视频格式，请上传mp4、avi、mov等格式");
            }

            // 生成新的文件名
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // 按日期创建子目录
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = datePath + "/" + newFileName;

            // 确保目录存在
            File uploadDir = new File(videoUploadPath + "/" + datePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件
            Path path = Paths.get(videoUploadPath + "/" + relativePath);
            Files.write(path, file.getBytes());

            // 返回视频访问地址
            Map<String, String> result = new HashMap<>();
            result.put("videoUrl", videoAccessUrl + "/" + relativePath);
            result.put("relativePath", relativePath);

            return ResponseResult.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error("视频上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传视频封面
     * @param file 封面图片
     * @param request HTTP请求
     * @return 上传结果
     */
    @PostMapping("/cover")
    public ResponseResult<Map<String, String>> uploadCover(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResponseResult.error("请选择封面图片");
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
            String relativePath = datePath + "/" + newFileName;

            // 确保目录存在
            File uploadDir = new File(coverUploadPath + "/" + datePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件
            Path path = Paths.get(coverUploadPath + "/" + relativePath);
            Files.write(path, file.getBytes());

            // 返回封面访问地址
            Map<String, String> result = new HashMap<>();
            result.put("coverUrl", videoAccessUrl + "/cover/" + relativePath);
            result.put("relativePath", relativePath);

            return ResponseResult.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error("封面上传失败: " + e.getMessage());
        }
    }

    /**
     * 保存视频信息
     * @param video 视频信息
     * @return 保存结果
     */
    @PostMapping("/save")
    public ResponseResult<Video> saveVideo(@RequestBody Video video) {
        // 从请求属性中获取用户信息（由JWT拦截器设置）


        // 设置初始数据
        video.setViews(0);
        video.setLikes(0);
        // 状态设置为2表示审核中
        video.setStatus(2);

        // 保存视频
        Video savedVideo = adminVideoService.saveVideo(video);

        return ResponseResult.success(savedVideo);
    }

    /**
     * 检查是否为有效的视频扩展名
     * @param extension 扩展名
     * @return 是否有效
     */
    private boolean isValidVideoExtension(String extension) {
        String[] validExtensions = {".mp4", ".avi", ".mov", ".wmv", ".flv", ".mkv"};
        for (String valid : validExtensions) {
            if (valid.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
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
