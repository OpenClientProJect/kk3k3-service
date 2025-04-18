package com.ligg.service;

import com.ligg.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 通过ID获取用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getUserById(Long id);

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUsername(String username);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果，true为成功，false为失败
     */
    boolean register(User user);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，登录失败返回null
     */
    User login(String username, String password);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果，true为成功，false为失败
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果，true为成功，false为失败
     */
    boolean deleteUser(Long id);

    /**
     * 获取所有用户
     * @return 用户列表
     */
    List<User> getAllUsers();
} 