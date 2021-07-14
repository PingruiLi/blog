package com.lpr.blog.dao;

import com.lpr.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsernameAndPassword(String username, String password);
}
