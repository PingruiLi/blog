package com.lpr.blog.dao;

import com.lpr.blog.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByBlogId(Integer blogId, Sort sort);

    List<Comment> findByBlogIdAndParentCommentNull(Integer blohId, Sort sort);
}
