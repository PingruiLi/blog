package com.lpr.blog.service;

import com.lpr.blog.entity.Blog;
import com.lpr.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Integer id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    List<Blog> getRecommendedBlogs(int n);

    Page<Blog> searchBlog(Pageable pageable, String key);

    Page<Blog> listBlog(Integer tagId, Pageable pageable);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Integer id, Blog blog);

    void deleteBlog(Integer id);

    Blog getAndUpdateView(Integer id);

    Map<String, List<Blog>> archiveBlogs();

    int countBlogs();
}
