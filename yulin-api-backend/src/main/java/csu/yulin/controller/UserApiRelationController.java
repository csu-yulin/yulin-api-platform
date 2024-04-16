package csu.yulin.controller;

import csu.yulin.common.DeleteRequest;
import csu.yulin.common.IdRequest;
import csu.yulin.common.response.ResultCode;
import csu.yulin.common.response.ResultResponse;
import csu.yulin.exception.DataBaseException;
import csu.yulin.model.dto.userApiRelation.UserApiRelationAddCountRequest;
import csu.yulin.model.dto.userApiRelation.UserApiRelationAddRequest;
import csu.yulin.model.entity.UserApiRelation;
import csu.yulin.service.ApiInfoService;
import csu.yulin.service.UserApiRelationService;
import csu.yulin.service.UserService;
import csu.yulin.utils.ThrowUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


/**
 * 用户接口关系接口
 *
 * @author 刘飘
 */
@Slf4j
@RestController
@RequestMapping("/userApi")
@Tag(name = "用户接口关系接口", description = "用户-接口关系管理")
public class UserApiRelationController {
    @Resource
    private UserApiRelationService userApiRelationService;

    @Resource
    private UserService userService;

    @Resource
    private ApiInfoService apiInfoService;

    /**
     * 添加用户接口关系记录
     *
     * @param userApiRelationAddRequest 用户接口关系添加请求对象
     * @return 返回操作结果，true表示添加成功，false表示添加失败
     */
    @Operation(summary = "添加用户接口关系记录", description = "管理员添加用户接口关系记录")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResultResponse<Boolean> addApi(@RequestBody UserApiRelationAddRequest userApiRelationAddRequest) {
        userApiRelationAddRequest.validateUserApiRelationAddRequest();

        Long userId = userApiRelationAddRequest.getUserId();
        List<Long> apiIds = userApiRelationAddRequest.getApiIds();
        // 判断用户是否存在
        ThrowUtils.throwIf(Objects.isNull(userService.getById(userId)),
                new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));


        // 判断接口是否存在
        apiIds.forEach(apiId -> {
            ThrowUtils.throwIf(Objects.isNull(apiInfoService.getById(apiId)),
                    new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));
            UserApiRelation userApiRelation = new UserApiRelation();
            userApiRelation.setUserId(userId);
            userApiRelation.setApiId(apiId);
            userApiRelationService.save(userApiRelation);
        });

        return ResultResponse.success(true);
    }

    /**
     * 添加用户接口调用次数记录
     *
     * @param userApiRelationAddCountRequestRequest 用户接口调用次数添加请求对象
     * @return 返回操作结果，true表示添加成功，false表示添加失败
     */
    @Operation(summary = "添加用户接口调用次数记录", description = "管理员添加用户接口调用次数记录")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add/count")
    public ResultResponse<Boolean> addCount(@RequestBody UserApiRelationAddCountRequest userApiRelationAddCountRequestRequest) {
        userApiRelationAddCountRequestRequest.validateUserApiRelationAddCountRequest();

        Long userId = userApiRelationAddCountRequestRequest.getUserId();
        List<Long> apiIds = userApiRelationAddCountRequestRequest.getApiIds();
        Integer count = userApiRelationAddCountRequestRequest.getCount();

        apiIds.forEach(apiId -> {
            // 判断用户-接口关系是否存在
            UserApiRelation userApiRelation = userApiRelationService.getByUserIdAndApiId(userId, apiId);

            // 更新用户-接口调用次数信息
            userApiRelation.setTotal(userApiRelation.getTotal() + count);
            userApiRelationService.updateById(userApiRelation);
        });

        return ResultResponse.success(true);
    }

    /**
     * 删除接口
     *
     * @param deleteRequest 删除接口请求对象
     * @return 返回操作结果，true表示删除成功，false表示删除失败
     */
    @Operation(summary = "删除接口", description = "管理员权限删除接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public ResultResponse<Boolean> deleteApi(@RequestBody DeleteRequest deleteRequest) {
        deleteRequest.validateDeleteRequest();

        Long id = deleteRequest.getId();
        boolean result = userApiRelationService.removeById(id);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return ResultResponse.success(true);
    }

    /**
     * 更新用户-接口关系记录的已调用次数加1
     *
     * @param idRequest 更新用户接口关系记录请求对象
     * @return 返回操作结果，true表示更新成功，false表示更新失败
     */
    @Operation(summary = "更新用户接口关系记录的已调用次数加1", description = "更新用户接口关系记录的已调用次数加1")
    @PostMapping("/update/callcount")
    public ResultResponse<Boolean> updateUserApiRelationCallCount(@RequestBody IdRequest idRequest) {
        idRequest.validateIdRequest();

        Long id = idRequest.getId();
        UserApiRelation userApiRelation = userApiRelationService.getById(id);
        ThrowUtils.throwIf(Objects.isNull(userApiRelation),
                new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        // 更新已调用次数加1
        userApiRelation.setCalled(userApiRelation.getCalled() + 1);
        boolean result = userApiRelationService.updateById(userApiRelation);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.DATABASE_OPERATION_FAILED));

        return ResultResponse.success(true);
    }

    /**
     * 查询单个用户-接口关系记录
     *
     * @param idRequest 查询请求对象
     * @return 返回查询结果，如果找到则返回对应的用户-接口关系记录，否则返回null
     */
    @Operation(summary = "查询单个用户-接口关系记录", description = "查询单个用户-接口关系记录")
    @PostMapping("/get")
    public ResultResponse<UserApiRelation> getUserApiRelation(@RequestBody IdRequest idRequest) {
        idRequest.validateIdRequest();

        Long id = idRequest.getId();
        UserApiRelation userApiRelation = userApiRelationService.getById(id);
        return ResultResponse.success(userApiRelation);
    }
}