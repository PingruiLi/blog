package com.lpr.blog.service;

import com.lpr.blog.entity.User;

public interface UserService {
    User validate(String username, String password);
}
