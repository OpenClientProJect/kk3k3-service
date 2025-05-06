package com.ligg.admin.mapper;

import com.ligg.entity.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminCommentMapper {

    @Select("select * from comment")
    List<Comment> getAllComments();

    @Delete("delete from comment where id = #{id}")
    void deleteComment(Long id);

    @Update("update comment set status = #{status} where id = #{id}")
    void statusComment(Long id, int status);
}
