package com.lpr.blog.service;

import com.lpr.blog.dao.BlogRepository;
import com.lpr.blog.entity.Blog;
import com.lpr.blog.entity.Type;
import com.lpr.blog.exception.NotFoundException;
import com.lpr.blog.utils.MarkdownUtils;
import com.lpr.blog.utils.MyBeanUtils;
import com.lpr.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;

import java.util.*;
import java.util.function.Function;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository repository;

    @Override
    public Blog getBlog(Integer id) {
        Blog blog = repository.getById(id);
        //System.out.println("get original blog: content\n---------------------------\n" + blog.getContent());
        if(blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog htmlBlog = markdownToHtml(blog);
        return htmlBlog;
    }

    private Blog markdownToHtml(Blog blog) {
        String html = MarkdownUtils.markdownToHtmlExtension(blog.getContent());
        Blog htmlBlog = new Blog();
        BeanUtils.copyProperties(blog,htmlBlog);
        htmlBlog.setContent(html);
        //System.out.println("get original blog: content\n---------------------------\n" + blog.getContent());
        return htmlBlog;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return repository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(blog.getTitle() != null && !blog.getTitle().equals("")){
                    predicates.add(criteriaBuilder.like(root.<String>get("title"),"%" + blog.getTitle() + "%"));
                }
                if(blog.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.getRecommended()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommended"), blog.getRecommended()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        blog.setPublishDate(new Date());
        blog.setViews(0);
        blog.setUpdateDate(new Date());
        return repository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Integer id, Blog blog) {
        Optional<Blog> container = repository.findById(id);
        if(!container.isPresent()){
            return null;
        }
        Blog target = container.get();
        BeanUtils.copyProperties(blog, target, MyBeanUtils.getNullPropertyNames(blog));
        //把值为null的过滤掉
        target.setUpdateDate(new Date());
        return repository.save(target);
    }

    @Transactional
    @Override
    public void deleteBlog(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Blog> getRecommendedBlogs(int n) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateDate");
        Pageable pageable = PageRequest.of(0,n,sort);
        return repository.findRecommendedBlogs(pageable);
    }

    /*
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return repository.findAll(pageable);
    }
    */
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        List<Blog> list =  repository.findAll();
        List<Blog> htmlList = new ArrayList<>();
        int startOfPage = pageable.getPageNumber() * pageable.getPageSize();
        if(startOfPage > list.size()) {
            return new PageImpl<Blog>(new ArrayList<>(), pageable, 0);
        }
        int endOfPage = Math.min(startOfPage + pageable.getPageSize(), list.size());
        List<Blog> blogsInPage = list.subList(startOfPage, endOfPage);
        for(Blog blog : blogsInPage) {
            String html = MarkdownUtils.markdownToHtmlExtension(blog.getContent());
            Blog htmlBlog = new Blog();
            BeanUtils.copyProperties(blog, htmlBlog);
            htmlBlog.setContent(html);
            htmlList.add(htmlBlog);
        }
        return new PageImpl<Blog>(htmlList, pageable, list.size());
    }



    @Override
    public Page<Blog> searchBlog(Pageable pageable, String key) {
        return repository.findByKey(key,pageable);
    }

    @Transactional
    @Override
    public Blog getAndUpdateView(Integer id) {
        repository.updateView(id);
        Optional<Blog> container = repository.findById(id);
        if(!container.isPresent()){
            throw new NotFoundException("博客信息不存在");
        }
        Blog htmlBlog = markdownToHtml(container.get());
        return htmlBlog;
    }

    @Override
    public Page<Blog> listBlog(Integer tagId, Pageable pageable) {
        return repository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        }, pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlogs() {
        List<String> years = repository.findYears();
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year,repository.findByYear(year));
        }
        return map;
    }

    @Override
    public int countBlogs() {
        return (int) repository.count();
    }
}
