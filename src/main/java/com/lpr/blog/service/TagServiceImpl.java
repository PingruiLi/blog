package com.lpr.blog.service;

import com.lpr.blog.dao.BlogRepository;
import com.lpr.blog.dao.TagRepository;
import com.lpr.blog.entity.Blog;
import com.lpr.blog.entity.Tag;
import com.lpr.blog.exception.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService{
    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag saveTag(Tag tag) {
        tagRepository.save(tag);
        return tag;
    }

    @Override
    public Tag getTag(Integer id) {
        Tag tag = tagRepository.getById(id);
        return tag;
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Tag updateTag(Integer id, Tag tag) {
        Optional<Tag> tagToUpdate = tagRepository.findById(id);
        if(!tagToUpdate.isPresent()) {
            return null;
        }
        Tag target = tagToUpdate.get();
        BeanUtils.copyProperties(tag, target);
        return tagRepository.save(target);
    }

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public void deleteTag(Integer id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> getTags(String ids) {
        List<Tag> tags = new ArrayList<>();
        if(ids == null || ids.equals("")){
            return tags;
        }
        String[] idArray = ids.split(",");
        for(String id:idArray){
            int idInt = Integer.parseInt(id);
            Optional<Tag> tagContainer = tagRepository.findById(idInt);
            if(tagContainer.isPresent()){
                tags.add(tagContainer.get());
            }
        }
        return tags;
    }

    @Override
    public List<Tag> getTopTags(int n) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,n,sort);
        List<Tag> topTags = tagRepository.getTopTags(pageable);
        return topTags;
    }

}
