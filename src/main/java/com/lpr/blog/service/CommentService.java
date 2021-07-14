package com.lpr.blog.service;

import com.lpr.blog.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlog(Integer blogId);

    Comment postComment(Comment comment);
}
