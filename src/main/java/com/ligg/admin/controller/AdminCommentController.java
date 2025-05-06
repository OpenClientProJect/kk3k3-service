package com.ligg.admin.controller;

import com.ligg.admin.service.AdminCommentService;
import com.ligg.common.Result;
import com.ligg.entity.Comment;
import com.ligg.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论管理
 */
@RestController
@RequestMapping("/api/admin/comment")
public class AdminCommentController {



    @Autowired
    private AdminCommentService adminCommentService;

    /**
     * 获取所有评论
     */
    @GetMapping
    public ResponseResult<List<Comment>> getAllComments() {
      List<Comment> comments = adminCommentService.getAllComments();
      return ResponseResult.success(comments);
    }

    /**
     * 更新评论
     */
    @DeleteMapping("/{id}")
    public ResponseResult<Void> statusComment(@PathVariable Long id, @RequestParam Integer status) {
      adminCommentService.statusComment(id, status);
      return ResponseResult.success();
    }
}
