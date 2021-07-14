package com.lpr.blog.dao;

import com.lpr.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer>, JpaSpecificationExecutor<Blog> {
    @Query("select b from Blog b where b.recommended=true")
    List<Blog> findRecommendedBlogs(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByKey(String key, Pageable pageable);

    @Modifying
    @Query("update Blog set views = views+1 where id=?1")
    void updateView(Integer id);

    @Query("select function('date_format', b.updateDate, '%Y') as year from Blog b group by function('date_format', b.updateDate, '%Y') order by year desc")
    List<String> findYears();
    //group by拿不到select as里定义的local变量

    @Query("select b from Blog b where function('date_format',b.updateDate,'%Y') = ?1")
    List<Blog> findByYear(String year);
}
