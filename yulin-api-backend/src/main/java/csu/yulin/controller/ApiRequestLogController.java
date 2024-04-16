package csu.yulin.controller;

import csu.yulin.common.DeleteRequest;
import csu.yulin.common.response.ResultCode;
import csu.yulin.common.response.ResultResponse;
import csu.yulin.exception.DataBaseException;
import csu.yulin.model.dto.ApiRequestLog.ApiRequestLogAddRequest;
import csu.yulin.model.entity.ApiInfo;
import csu.yulin.model.entity.ApiRequestLog;
import csu.yulin.model.entity.User;
import csu.yulin.model.entity.vo.apiRequestLog.StatisticalReportVO;
import csu.yulin.service.ApiInfoService;
import csu.yulin.service.ApiRequestLogService;
import csu.yulin.service.UserService;
import csu.yulin.utils.ThrowUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


/**
 * 接口请求日志接口
 *
 * @author 刘飘
 */
@Slf4j
@RestController
@RequestMapping("/log")
@Tag(name = "接口请求日志", description = "接口请求日志管理")
public class ApiRequestLogController {
    @Resource
    private UserService userService;

    @Resource
    private ApiInfoService apiInfoService;

    @Resource
    private ApiRequestLogService apiRequestLogService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加接口请求日志
     *
     * @param apiRequestLogAddRequest 接口请求日志添加请求对象
     * @return 返回操作结果，true表示添加成功，false表示添加失败
     */
    @Operation(summary = "添加接口请求日志", description = "添加接口请求日志")
    @PostMapping("/add")
    public ResultResponse<Boolean> addApiRequestLog(@RequestBody ApiRequestLogAddRequest apiRequestLogAddRequest) {
        apiRequestLogAddRequest.validateApiRequestLogAddRequest();

        // 判断用户和接口是否存在
        User user = userService.getById(apiRequestLogAddRequest.getUserId());
        ThrowUtils.throwIf(user == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));
        ApiInfo apiInfo = apiInfoService.getById(apiRequestLogAddRequest.getApiId());
        ThrowUtils.throwIf(apiInfo == null, new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        // 添加接口请求日志
        ApiRequestLog apiRequestLog = new ApiRequestLog();
        BeanUtils.copyProperties(apiRequestLogAddRequest, apiRequestLog);
        boolean result = apiRequestLogService.save(apiRequestLog);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.DATABASE_OPERATION_FAILED));
        return ResultResponse.success(true);
    }

    /**
     * 删除接口请求日志
     *
     * @param deleteRequest 日志ID
     * @return 返回操作结果，true表示删除成功，false表示删除失败
     */
    @Operation(summary = "删除接口请求日志", description = "管理员权限下删除接口请求日志接口")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public ResultResponse<Boolean> deleteApiRequestLog(@RequestBody DeleteRequest deleteRequest) {
        deleteRequest.validateDeleteRequest();

        Long id = deleteRequest.getId();
        boolean result = apiRequestLogService.removeById(id);
        ThrowUtils.throwIf(!result, new DataBaseException(ResultCode.DATABASE_OPERATION_FAILED));

        return ResultResponse.success(true);
    }


    /**
     * 获取统计报告
     *
     * @return 返回统计报告
     */
    @Operation(summary = "获取统计报告", description = "获取统计报告")
    @GetMapping("/repost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultResponse<StatisticalReportVO> getStatisticalReport() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        // 从缓存中获取数据
        if (Boolean.TRUE.equals(redisTemplate.hasKey(yesterday.toString()))) {
            StatisticalReportVO statisticalReportVOFromCache =
                    (StatisticalReportVO) redisTemplate.opsForValue().get(yesterday.toString());
            return ResultResponse.success(statisticalReportVOFromCache);
        }

        // 为了防止缓存失效，这里获取的是昨天的数据
        StatisticalReportVO statisticalReportVO = apiRequestLogService
                .getStatisticalReportOnSpecifiedDate(yesterday);
        // 设置缓存数据
        redisTemplate.opsForValue().set(yesterday.toString(), statisticalReportVO);

        return ResultResponse.success(statisticalReportVO);
    }
}