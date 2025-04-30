package com.ligg.admin.controller;

import com.ligg.admin.service.AdminVideoService;
import com.ligg.entity.Episodes;
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
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    
    // 存储分片上传的临时信息
    private static final Map<String, Map<String, Object>> CHUNK_INFO = new ConcurrentHashMap<>();

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
     * 初始化分片上传
     * @param request 初始化请求
     * @return 初始化结果
     */
    @PostMapping("/upload/init")
    public ResponseResult<Map<String, Object>> initChunkUpload(@RequestBody Map<String, Object> request) {
        try {
            String fileName = (String) request.get("fileName");
            long fileSize = Long.parseLong(request.get("fileSize").toString());
            int chunkSize = Integer.parseInt(request.get("chunkSize").toString());
            int totalChunks = Integer.parseInt(request.get("totalChunks").toString());
            
            // 生成上传ID
            String uploadId = UUID.randomUUID().toString();
            
            // 按日期创建临时目录
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String tempDirPath = videoUploadPath + "/temp/" + datePath + "/" + uploadId;
            
            // 确保临时目录存在
            File tempDir = new File(tempDirPath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            
            // 存储分片信息
            Map<String, Object> uploadInfo = new HashMap<>();
            uploadInfo.put("fileName", fileName);
            uploadInfo.put("fileSize", fileSize);
            uploadInfo.put("chunkSize", chunkSize);
            uploadInfo.put("totalChunks", totalChunks);
            uploadInfo.put("tempDir", tempDirPath);
            uploadInfo.put("uploadedChunks", new ConcurrentHashMap<Integer, Boolean>());
            uploadInfo.put("datePath", datePath);
            
            CHUNK_INFO.put(uploadId, uploadInfo);
            
            Map<String, Object> result = new HashMap<>();
            result.put("uploadId", uploadId);
            
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("初始化分片上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传视频分片
     * @param chunk 分片文件
     * @param index 分片索引
     * @param uploadId 上传ID
     * @param fileName 文件名
     * @return 上传结果
     */
    @PostMapping("/upload/chunk")
    public ResponseResult<Map<String, Object>> uploadChunk(
            @RequestParam("chunk") MultipartFile chunk,
            @RequestParam("index") int index,
            @RequestParam("uploadId") String uploadId,
            @RequestParam("fileName") String fileName) {
        
        try {
            // 获取上传信息
            Map<String, Object> uploadInfo = CHUNK_INFO.get(uploadId);
            if (uploadInfo == null) {
                return ResponseResult.error("无效的上传ID");
            }
            
            String tempDir = (String) uploadInfo.get("tempDir");
            int totalChunks = (int) uploadInfo.get("totalChunks");
            
            if (index >= totalChunks) {
                return ResponseResult.error("无效的分片索引");
            }
            
            // 保存分片文件
            String chunkPath = tempDir + "/" + index;
            Path path = Paths.get(chunkPath);
            Files.write(path, chunk.getBytes());
            
            // 更新已上传分片信息
            Map<Integer, Boolean> uploadedChunks = (Map<Integer, Boolean>) uploadInfo.get("uploadedChunks");
            uploadedChunks.put(index, true);
            
            Map<String, Object> result = new HashMap<>();
            result.put("uploaded", uploadedChunks.size());
            result.put("total", totalChunks);
            
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("上传分片失败: " + e.getMessage());
        }
    }
    
    /**
     * 合并视频分片
     * @param request 合并请求
     * @return 合并结果
     */
    @PostMapping("/upload/merge")
    public ResponseResult<Map<String, String>> mergeChunks(@RequestBody Map<String, Object> request) {
        try {
            String uploadId = (String) request.get("uploadId");
            String fileName = (String) request.get("fileName");
            int totalChunks = Integer.parseInt(request.get("totalChunks").toString());
            
            // 获取上传信息
            Map<String, Object> uploadInfo = CHUNK_INFO.get(uploadId);
            if (uploadInfo == null) {
                return ResponseResult.error("无效的上传ID");
            }
            
            String tempDir = (String) uploadInfo.get("tempDir");
            Map<Integer, Boolean> uploadedChunks = (Map<Integer, Boolean>) uploadInfo.get("uploadedChunks");
            
            // 检查是否所有分片都已上传
            if (uploadedChunks.size() != totalChunks) {
                return ResponseResult.error("分片不完整，无法合并");
            }
            
            // 生成最终文件名和路径
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            String datePath = (String) uploadInfo.get("datePath");
            String finalDirPath = videoUploadPath + "/" + datePath;
            String relativePath = datePath + "/" + newFileName;
            
            // 确保目录存在
            File finalDir = new File(finalDirPath);
            if (!finalDir.exists()) {
                finalDir.mkdirs();
            }
            
            // 合并文件
            Path targetPath = Paths.get(finalDirPath + "/" + newFileName);
            Files.createFile(targetPath);
            
            for (int i = 0; i < totalChunks; i++) {
                Path chunkPath = Paths.get(tempDir + "/" + i);
                Files.write(targetPath, Files.readAllBytes(chunkPath), StandardOpenOption.APPEND);
                
                // 删除分片文件
                Files.delete(chunkPath);
            }
            
            // 删除临时目录
            Files.delete(Paths.get(tempDir));
            
            // 清理上传信息
            CHUNK_INFO.remove(uploadId);
            
            // 返回视频访问地址
            Map<String, String> result = new HashMap<>();
            result.put("videoUrl", videoAccessUrl + "/" + relativePath);
            result.put("relativePath", relativePath);
            
            return ResponseResult.success(result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("合并分片失败: " + e.getMessage());
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

    /**
     * 添加剧集
     * @param episode 剧集信息
     * @return 保存结果
     */
    @PostMapping("/episode/add")
    public ResponseResult<?> addEpisode(@RequestBody Episodes episode) {
        // 保存剧集
        adminVideoService.insertEpisode(episode);
        
        return ResponseResult.success("剧集添加成功");
    }
}
