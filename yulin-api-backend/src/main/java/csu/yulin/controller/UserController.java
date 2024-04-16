package csu.yulin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import csu.yulin.common.DeleteRequest;
import csu.yulin.common.IdRequest;
import csu.yulin.common.response.ResultCode;
import csu.yulin.common.response.ResultResponse;
import csu.yulin.exception.BusinessException;
import csu.yulin.exception.DataBaseException;
import csu.yulin.exception.UserException;
import csu.yulin.model.dto.user.*;
import csu.yulin.model.entity.User;
import csu.yulin.model.vo.user.UserVO;
import csu.yulin.service.UserService;
import csu.yulin.utils.ThrowUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 用户接口
 *
 * @author 刘飘
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口", description = "用户登录、注册以及管理等接口")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest 包含注册所需信息的用户对象
     * @return 注册成功后生成的 JWT Token
     */
    @Operation(summary = "用户注册", description = "用户注册接口")
    @PostMapping("/register")
    public ResultResponse<String> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        userRegisterRequest.validateUserRegisterRequest();

        User user = new User();
        BeanUtils.copyProperties(userRegisterRequest, user);

        // 注册用户并获取生成的 JWT Token
        String token = userService.register(user);
        // 返回成功响应，包含生成的 Token
        return ResultResponse.success(token);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 包含用户身份信息的User对象
     * @return 登录成功后生成的访问令牌
     */
    @Operation(summary = "用户登录", description = "用户登录接口")
    @PostMapping("/login")
    public ResultResponse<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        userLoginRequest.validateUserLoginRequest();
        User user = new User();
        BeanUtils.copyProperties(userLoginRequest, user);
        String token = userService.login(user);
        return ResultResponse.success(token);
    }

    /**
     * 添加用户
     *
     * @param userAddRequest 用户对象
     * @return 返回用户ID
     */
    @Operation(summary = "添加用户", description = "管理员权限下添加用户接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResultResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        userAddRequest.validateUserAddRequest();

        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        userService.addUser(user);
        return ResultResponse.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest 用户ID
     * @return 返回操作结果，true表示删除成功，false表示删除失败
     */
    @Operation(summary = "删除用户", description = "管理员权限下删除用户接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public ResultResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        deleteRequest.validateDeleteRequest();

        Long id = deleteRequest.getId();
        boolean result = userService.removeById(id);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.DATABASE_EXECUTION_FAILED));

        return ResultResponse.success(true);
    }

    /**
     * 更新用户信息(管理员权限)
     *
     * @param userUpdateRequest 包含更新后用户信息的对象，必须包含用户ID
     * @return 返回操作结果，true表示更新成功，false表示更新失败
     */
    @Operation(summary = "更新用户", description = "管理员权限下更新用户信息接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ResultResponse<Boolean> updateUserByAdmin(@RequestBody UserUpdateRequest userUpdateRequest) {
        userUpdateRequest.validateUserUpdateRequest();

        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);

        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.DATABASE_EXECUTION_FAILED));

        return ResultResponse.success(true);
    }

    /**
     * 更新用户信息(用户自己)
     *
     * @param userUpdateBySelfRequest 包含更新后用户信息的对象，必须包含用户ID
     * @return 返回操作结果，true表示更新成功，false表示更新失败
     */
    @Operation(summary = "更新用户", description = "用户自己更新用户信息接口")
    @PostMapping("/update/my")
    public ResultResponse<Boolean> updateUserBySelf(@RequestBody UserUpdateBySelfRequest userUpdateBySelfRequest) {
        userUpdateBySelfRequest.validateUserUpdateBySelfRequest();

        //只能由用户自己更新自己的信息
        User loginUser = userService.getLoginUser();
        if (!loginUser.getId().equals(userUpdateBySelfRequest.getId())) {
            throw new BusinessException(ResultCode.USER_NO_PERMISSION);
        }

        User user = new User();
        BeanUtils.copyProperties(userUpdateBySelfRequest, user);

        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.DATABASE_EXECUTION_FAILED));

        return ResultResponse.success(true);
    }

    /**
     * 根据用户ID获取用户全部信息
     *
     * @param idRequest 用户ID
     * @return 用户信息(User)
     */
    @Operation(summary = "根据用户ID获取用户全部信息", description = "管理员权限下根据用户ID获取用户信息接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get")
    public ResultResponse<User> getUserById(IdRequest idRequest) {
        idRequest.validateIdRequest();
        User user = userService.getById(idRequest.getId());
        ThrowUtils.throwIf(user == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));
        return ResultResponse.success(user);
    }

    /**
     * 根据用户ID获取用户部分信息
     *
     * @param id 用户ID
     * @return 用户信息(UserVO)
     */
    @Operation(summary = "根据用户ID获取用户部分信息", description = "管理员权限下根据用户ID获取用户信息接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get/vo")
    public ResultResponse<UserVO> getUserVoById(IdRequest idRequest) {
        idRequest.validateIdRequest();
        User user = userService.getById(idRequest.getId());
        ThrowUtils.throwIf(user == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return ResultResponse.success(UserVO.fromUser(user));
    }

    /**
     * 根据用户ID获取用户全部信息(用户自己)
     *
     * @param idRequest 用户ID
     * @return 用户信息(User)
     */
    @Operation(summary = "根据用户ID获取用户信息", description = "根据用户ID获取用户信息接口")
    @GetMapping("/get/vo/my")
    public ResultResponse<UserVO> getMyselfById(IdRequest idRequest) {
        idRequest.validateIdRequest();

        //只能由用户自己更新自己的信息
        User loginUser = userService.getLoginUser();
        ThrowUtils.throwIf(!loginUser.getId().equals(idRequest.getId()),
                new UserException(ResultCode.USER_NO_PERMISSION));

        return ResultResponse.success(UserVO.fromUser(loginUser));
    }

    /**
     * 分页查询用户全部信息
     *
     * @param userQueryRequest 用户查询请求对象
     * @return 用户信息分页结果
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "分页查询用户信息", description = "管理员权限下的分页查询用户信息接口")
    @PostMapping("/page")
    public ResultResponse<Page<User>> page(@RequestBody UserQueryRequest userQueryRequest) {
        userQueryRequest.validateUserQueryRequest();

        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();

        Page<User> pageInfo = new Page<>(current, size);
        User user = new User();
        BeanUtils.copyProperties(userQueryRequest, user);
        userService.page(pageInfo, userService.getQueryWrapper(user, userQueryRequest.getSortField(),
                userQueryRequest.getSortOrder()));

        return ResultResponse.success(pageInfo);
    }

    /**
     * 分页查询用户部分信息
     *
     * @param userQueryRequest 用户查询请求对象
     * @return 用户信息分页结果
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "分页查询用户信息", description = "管理员权限下的分页查询用户信息接口")
    @PostMapping("/page/vo")
    public ResultResponse<Page<UserVO>> pageVO(@RequestBody UserQueryRequest userQueryRequest) {
        userQueryRequest.validateUserQueryRequest();

        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();

        Page<User> pageInfo = new Page<>(current, size);
        User user = new User();
        BeanUtils.copyProperties(userQueryRequest, user);
        userService.page(pageInfo, userService.getQueryWrapper(user, userQueryRequest.getSortField(),
                userQueryRequest.getSortOrder()));

        // 将User对象转换为UserVO对象
        Page<UserVO> userVoPage = (Page<UserVO>) pageInfo.convert(UserVO::fromUser);

        return ResultResponse.success(userVoPage);
    }
}