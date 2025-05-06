package com.ligg.admin.service;

import com.ligg.entity.Comment;

import java.util.List;

public interface AdminCommentService {
    List<Comment> getAllComments();

    void deleteComment(Long id);

    void statusComment(Long id, Integer status);
}
