package com.lpr.blog.service;

import com.lpr.blog.entity.Blog;
import com.lpr.blog.entity.Tag;
import com.lpr.blog.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(Integer id);

    Tag getTagByName(String name);

    Tag updateTag(Integer id, Tag tag);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> getTags(String ids);

    void deleteTag(Integer id);

    List<Tag> getTopTags(int n);
}
