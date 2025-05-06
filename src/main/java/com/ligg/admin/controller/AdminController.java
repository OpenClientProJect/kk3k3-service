package com.ligg.admin.controller;

import com.ligg.admin.dto.AdminInfoDTO;
import com.ligg.admin.dto.AdminLoginDTO;
import com.ligg.admin.service.AdminUserService;
import com.ligg.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 管理员登录
     * @param loginDTO 登录信息
     * @param request 请求对象
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result login(@RequestBody AdminLoginDTO loginDTO, HttpServletRequest request) {
        if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            return Result.error("用户名和密码不能为空");
        }


        // 登录
        AdminInfoDTO adminInfo = adminUserService.login(loginDTO);
        if (adminInfo == null) {
            return Result.error("用户名或密码错误");
        }
        
        return Result.success(adminInfo);
    }


}
