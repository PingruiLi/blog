package com.lpr.blog.service;

import com.lpr.blog.dao.CommentRepository;
import com.lpr.blog.entity.Comment;
import com.lpr.blog.exception.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository repository;

    @Override
    public List<Comment> listCommentByBlog(Integer blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "publishDate");
        return processParentComments(repository.findByBlogIdAndParentCommentNull(blogId, sort));
    }

    @Override
    @Transactional
    public Comment postComment(Comment comment) {
        int parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1) {
            Optional<Comment> container = repository.findById(parentCommentId);
            if(!container.isPresent()) {
                throw new NotFoundException("抱歉，这条评论已经被删除了");
            }
            Comment parent = container.get();
            comment.setParentComment(parent);
        } else {
            comment.setParentComment(null);
        }
        comment.setPublishDate(new Date());
        return repository.save(comment);
    }

    private List<Comment> processParentComments(List<Comment> comments) { //不能直接在对象修改，需要new一个新的对象以免影响数据库内容
        List<Comment> parentComments = new ArrayList<>();
        for(Comment comment : comments) {
            Comment tempComment = new Comment();
            BeanUtils.copyProperties(comment, tempComment);
            if(tempComment.getReplyComments() != null) {
                for(Comment c : tempComment.getReplyComments()) {
                    getChildren(c);
                    tempComment.setReplyComments(childrenTemp);
                    childrenTemp = new ArrayList<>();
                }
            }
            parentComments.add(tempComment);
        }
        return parentComments;
    }

    List<Comment> childrenTemp = new ArrayList<>();

    private void getChildren(Comment comment) {
        childrenTemp.add(comment);
        if(comment.getReplyComments().size() > 0){
            for(Comment c : comment.getReplyComments()) {
                getChildren(c);
            }
        }
    }
}
