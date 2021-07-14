package com.lpr.blog.service;

import com.lpr.blog.dao.UserRepository;
import com.lpr.blog.entity.User;
import com.lpr.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User validate(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.encode(password));
        return user;
    }


}
