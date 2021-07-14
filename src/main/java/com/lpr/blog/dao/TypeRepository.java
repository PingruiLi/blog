package com.lpr.blog.dao;

import com.lpr.blog.entity.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.criteria.CriteriaBuilder;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
    Type findByName(String name);

    @Query("select t from Type t")
    List<Type> findTopTypes(Pageable pageable);
}
