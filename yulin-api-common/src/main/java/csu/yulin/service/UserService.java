package csu.yulin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.model.entity.User;

/**
 * @author 刘飘
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-03-27 19:39:36
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param user 用户对象，包含登录所需的用户名和密码信息
     * @return 登录成功后生成的 JWT Token
     */
    String login(User user);

    /**
     * 用户注册
     *
     * @param user 用户对象，包含注册所需的用户名和密码信息
     * @return 注册成功后生成的 JWT Token
     */
    String register(User user);

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    User getLoginUser();

    /**
     * 获取用于查询用户的查询包装器。
     *
     * @param user      用户对象，用于构建查询条件。
     * @param sortField 排序字段，如果为 null 或空字符串，则不排序。
     * @param sortOrder 排序顺序，可选值为 "asc"（升序）或 "desc"（降序）。
     *                  如果为 null 或空字符串，则不排序。
     * @return 查询包装器，用于构建用户查询条件。
     */
    QueryWrapper<User> getQueryWrapper(User user, String sortField, String sortOrder);

    /**
     * 添加用户
     *
     * @param user 用户对象
     */
    void addUser(User user);

    /**
     * 根据用户密钥获取用户私钥
     *
     * @param accessKey 用户密钥
     * @return 用户私钥
     */
    String getUserSecretKeyByAccessKey(String accessKey);

    /**
     * 根据用户密钥获取用户ID
     *
     * @param accessKey 用户密钥
     * @return 用户ID
     */
    Long getUserIdByAccessKey(String accessKey);
}
