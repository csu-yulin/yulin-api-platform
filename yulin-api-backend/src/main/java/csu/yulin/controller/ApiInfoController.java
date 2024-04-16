package csu.yulin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import csu.yulin.common.DeleteRequest;
import csu.yulin.common.IdRequest;
import csu.yulin.common.response.ResultCode;
import csu.yulin.common.response.ResultResponse;
import csu.yulin.enums.HttpMethodEnum;
import csu.yulin.exception.ApiException;
import csu.yulin.exception.DataBaseException;
import csu.yulin.model.dto.apiInfo.ApiInfoAddRequest;
import csu.yulin.model.dto.apiInfo.ApiInfoQueryRequest;
import csu.yulin.model.dto.apiInfo.ApiInfoUpdateRequest;
import csu.yulin.model.entity.ApiInfo;
import csu.yulin.service.ApiInfoService;
import csu.yulin.utils.HttpUtils;
import csu.yulin.utils.JavaClassGeneratorUtils;
import csu.yulin.utils.ThrowUtils;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * 用户接口
 *
 * @author 刘飘
 */
@Slf4j
@RestController
@RequestMapping("/apiInfo")
@Tag(name = "接口信息接口", description = "接口管理等接口")
public class ApiInfoController {
    @Resource
    private ApiInfoService apiInfoService;

    @Resource
    private HttpUtils httpUtils;

    @Resource
    private JavaClassGeneratorUtils javaClassGeneratorUtils;

    /**
     * 添加接口
     *
     * @param apiInfoAddRequest 添加接口请求对象
     * @return 返回操作结果，true表示添加成功，false表示添加失败
     */
    @Operation(summary = "添加接口", description = "管理员添加接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResultResponse<Long> addApi(@RequestBody ApiInfoAddRequest apiInfoAddRequest) throws TemplateException, IOException {
        apiInfoAddRequest.validateApiInfoAddRequest();

        String path = apiInfoAddRequest.getPath();
        String httpMethod = apiInfoAddRequest.getHttpMethod();
        String requestExample = apiInfoAddRequest.getRequestExample();

        HttpMethodEnum method = HttpMethodEnum.fromValue(httpMethod);
        // 判断接口是否可以调用
        boolean request = httpUtils.request(path, method, requestExample);

        ThrowUtils.throwIf(!request, new ApiException(ResultCode.API_CAN_NOT_USE));

        // 添加接口信息
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(apiInfoAddRequest, apiInfo);
        // 默认启用
        apiInfo.setStatus(1);
        apiInfoService.save(apiInfo);

        // TODO: 同时也要同步创建A对应的API MATE数据
        // 自动生成SDK
        javaClassGeneratorUtils.generate(apiInfo.getId());

        return ResultResponse.success(apiInfo.getId());
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
        boolean result = apiInfoService.removeById(id);
        ThrowUtils.throwIf(result, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return ResultResponse.success(true);
    }

    /**
     * 更新接口
     *
     * @param apiInfoUpdateRequest 更新接口请求对象
     * @return 返回操作结果，true表示更新成功，false表示更新失败
     */
    @Operation(summary = "更新接口", description = "管理员权限更新接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ResultResponse<Boolean> updateApiInfo(@RequestBody ApiInfoUpdateRequest apiInfoUpdateRequest) {
        apiInfoUpdateRequest.validateApiInfoUpdateRequest();

        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(apiInfoUpdateRequest, apiInfo);

        boolean result = apiInfoService.updateById(apiInfo);
        ThrowUtils.throwIf(result, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return ResultResponse.success(true);
    }


    /**
     * 根据接口ID获取接口全部信息
     *
     * @param idRequest 接口ID请求对象
     * @return 返回接口信息
     */
    @Operation(summary = "根据接口ID获取接口全部信息", description = "根据接口ID获取接口全部信息")
    @GetMapping("/get")
    public ResultResponse<ApiInfo> getApiInfoById(IdRequest idRequest) {
        idRequest.validateIdRequest();

        ApiInfo apiInfo = apiInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(apiInfo == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return ResultResponse.success(apiInfo);
    }

    /**
     * 分页查询接口信息
     *
     * @param apiInfoQueryRequest 查询接口请求对象
     * @return 分页查询接口信息
     */
    @Operation(summary = "分页查询接口信息", description = "分页查询接口信息接口")
    @PostMapping("/page")
    public ResultResponse<Page<ApiInfo>> page(@RequestBody ApiInfoQueryRequest apiInfoQueryRequest) {
        apiInfoQueryRequest.validateApiInfoQueryRequest();

        long current = apiInfoQueryRequest.getCurrent();
        long size = apiInfoQueryRequest.getPageSize();

        Page<ApiInfo> pageInfo = new Page<>(current, size);
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(apiInfoQueryRequest, apiInfo);
        apiInfoService.page(pageInfo, apiInfoService.getQueryWrapper(apiInfo, apiInfoQueryRequest.getSortField(),
                apiInfoQueryRequest.getSortOrder()));

        return ResultResponse.success(pageInfo);
    }

    /**
     * 上线接口
     *
     * @param idRequest 上线接口请求对象
     * @return 返回操作结果，true表示上线成功，false表示上线失败
     */
    @Operation(summary = "上线接口", description = "上线接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/online")
    public ResultResponse<Boolean> online(@RequestBody IdRequest idRequest) {
        idRequest.validateIdRequest();

        Long id = idRequest.getId();
        // 查询接口是否存在
        ApiInfo apiInfo = apiInfoService.getById(id);
        ThrowUtils.throwIf(apiInfo == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        // 判断接口是否可以调用
        boolean request = httpUtils.request(apiInfo.getPath(), HttpMethodEnum.fromValue(apiInfo.getHttpMethod()),
                apiInfo.getRequestExample());
        ThrowUtils.throwIf(!request, new ApiException(ResultCode.API_CAN_NOT_USE));

        // 上线接口
        ApiInfo apiOnline = new ApiInfo();
        apiOnline.setId(id);
        apiOnline.setStatus(1);
        boolean result = apiInfoService.updateById(apiOnline);
        ThrowUtils.throwIf(result, new DataBaseException(ResultCode.DATABASE_OPERATION_FAILED));

        return ResultResponse.success(true);
    }

    /**
     * 下线接口
     *
     * @param idRequest 下线接口请求对象
     * @return 返回操作结果，true表示下线成功，false表示下线失败
     */
    @Operation(summary = "下线接口", description = "下线接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/offline")
    public ResultResponse<Boolean> offline(@RequestBody IdRequest idRequest) {
        idRequest.validateIdRequest();

        Long id = idRequest.getId();
        // 查询接口是否存在
        ApiInfo apiInfo = apiInfoService.getById(id);
        ThrowUtils.throwIf(apiInfo == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        // 下线接口
        ApiInfo apiOnline = new ApiInfo();
        apiOnline.setId(id);
        apiOnline.setStatus(0);
        boolean result = apiInfoService.updateById(apiOnline);
        ThrowUtils.throwIf(result, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return ResultResponse.success(true);
    }
}