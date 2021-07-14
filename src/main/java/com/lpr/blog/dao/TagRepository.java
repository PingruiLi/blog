package com.lpr.blog.dao;

import com.lpr.blog.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;


@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String name);

    @Query("select t from Tag t")
    List<Tag> getTopTags(Pageable pageable);
}
