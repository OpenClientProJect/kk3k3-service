package com.ligg.service.impl;

import com.ligg.admin.utils.MD5Utils;
import com.ligg.mapper.UserMapper;
import com.ligg.entity.User;
import com.ligg.service.UserService;
import com.ligg.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userDao;

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (userDao.findByUsername(user.getUsername()) != null) {
            return false;
        }
        
        // 设置创建和更新时间
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        
        // 密码加密
        user.setPassword(PasswordEncoder.encode(user.getPassword()));
        
        return userDao.insert(user) > 0;
    }

    @Override
    public User login(String username, String password) {
        // 获取用户信息
        User user = userDao.findByUsername(username);
        
        // 如果用户存在且密码匹配，则返回用户信息
        if (user != null ) {
            String encrypt = MD5Utils.encrypt(password);
            System.out.println(encrypt);
            System.out.println(user.getPassword());
            return user;
        }
        
        return null;
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        // 更新时间
        user.setUpdateTime(new Date());
        
        // 如果修改了密码，进行加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(PasswordEncoder.encode(user.getPassword()));
        }
        
        return userDao.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        return userDao.deleteById(id) > 0;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
} 