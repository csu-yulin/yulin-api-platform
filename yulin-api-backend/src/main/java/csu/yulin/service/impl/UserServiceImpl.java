package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.common.response.ResultCode;
import csu.yulin.constant.CommonConstant;
import csu.yulin.enums.UserRoleEnum;
import csu.yulin.enums.UserStatusEnum;
import csu.yulin.exception.BusinessException;
import csu.yulin.exception.DataBaseException;
import csu.yulin.exception.ParamsException;
import csu.yulin.exception.UserException;
import csu.yulin.mapper.UserMapper;
import csu.yulin.model.entity.User;
import csu.yulin.model.entity.UserDetail;
import csu.yulin.service.UserService;
import csu.yulin.utils.JwtUtil;
import csu.yulin.utils.KeyGeneratorUtils;
import csu.yulin.utils.SqlUtils;
import csu.yulin.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

/**
 * @author 刘飘
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-03-27 19:39:36
 */
@DubboService
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户登录方法
     *
     * @param user 包含登录所需的用户名和密码信息的用户对象
     * @return 登录成功后生成的 JWT Token
     */
    @Override
    public String login(User user) throws BusinessException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        // 进行身份验证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetail loginUser = (UserDetail) authenticate.getPrincipal();

        // 获取认证成功后的用户信息
        user = loginUser.getUser();
        // 生成 JWT Token
        return jwtUtil.generateToken(user.getUsername(), Objects.requireNonNull(UserRoleEnum.fromValue(user.getRole())));
    }

    /**
     * 用户注册方法
     *
     * @param user 包含注册所需的用户名和密码信息的用户对象
     * @return 注册成功后生成的 JWT Token
     */
    @Override
    public String register(User user) throws BusinessException {
        // 对用户密码进行加密
        user.setPassword(getEncryptedPassword(user.getPassword()));

        // 分配默认头像
        user.setAvatar(CommonConstant.DEFAULT_AVATAR);

        // 默认分配用户角色: USER
        user.setRole(UserRoleEnum.USER.getValue());

        // 分配默认随机访问密钥
        user.setAccessKey(KeyGeneratorUtils.generateAccessKey());
        user.setSecretKey(KeyGeneratorUtils.generateSecretKey());

        // 设置用户状态为正常
        user.setStatus(UserStatusEnum.ENABLED.getValue());

        boolean result = save(user);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.DATABASE_OPERATION_FAILED));

        // 生成 JWT Token
        return jwtUtil.generateToken(user.getUsername(), Objects.requireNonNull(UserRoleEnum.fromValue(user.getRole())));
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    @Override
    public User getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getUsername, principal));

        ThrowUtils.throwIf(user == null, new UserException(ResultCode.USER_NOT_LOGGED_IN));

        return user;
    }

    /**
     * 获取用于查询用户的查询包装器。
     *
     * @param user      用户对象，用于构建查询条件。
     * @param sortField 排序字段，如果为 null 或空字符串，则不排序。
     * @param sortOrder 排序顺序，可选值为 "asc"（升序）或 "desc"（降序）。
     *                  如果为 null 或空字符串，则不排序。
     * @return 查询包装器，用于构建用户查询条件。
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(User user, String sortField, String sortOrder) {
        Long id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();
        String role = user.getRole();
        String phone = user.getPhone();
        String gender = user.getGender();
        Integer status = user.getStatus();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(email), "email", email);
        queryWrapper.eq(StringUtils.isNotBlank(phone), "phone", phone);
        queryWrapper.eq(StringUtils.isNotBlank(gender), "gender", gender);
        queryWrapper.eq(Objects.nonNull(status), "status", status);
        queryWrapper.eq(StringUtils.isNotBlank(role), "role", role);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                SqlUtils.camelCaseToSnakeCase(sortField));

        return queryWrapper;
    }

    /**
     * 添加用户
     *
     * @param user 用户对象
     */
    @Override
    public void addUser(User user) {
        // 对用户密码进行加密
        user.setPassword(getEncryptedPassword(user.getPassword()));

        // 分配默认头像
        user.setAvatar(CommonConstant.DEFAULT_AVATAR);

        // 分配默认随机访问密钥
        user.setAccessKey(KeyGeneratorUtils.generateAccessKey());
        user.setSecretKey(KeyGeneratorUtils.generateSecretKey());

        boolean result = save(user);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.DATABASE_OPERATION_FAILED));
    }

    /**
     * 根据用户密钥获取用户私钥
     *
     * @param accessKey 用户密钥
     * @return 用户私钥
     */
    @Override
    public String getUserSecretKeyByAccessKey(String accessKey) {
        ThrowUtils.throwIf(StringUtils.isBlank(accessKey),
                new ParamsException(ResultCode.PARAMS_IS_BLANK));

        User user = getOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getAccessKey, accessKey)
                .select(User::getSecretKey));

        ThrowUtils.throwIf(user == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return user.getSecretKey();
    }

    /**
     * 根据用户密钥获取用户ID
     *
     * @param accessKey 用户密钥
     * @return 用户ID
     */
    @Override
    public Long getUserIdByAccessKey(String accessKey) {
        User user = getOne(Wrappers.lambdaQuery(User.class)
                .eq(User::getAccessKey, accessKey)
                .select(User::getId));

        ThrowUtils.throwIf(user == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return user.getId();
    }


    private String getEncryptedPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}




