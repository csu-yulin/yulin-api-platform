package csu.yulin.model.dto.ApiRequestLog;

import csu.yulin.common.response.ResultCode;
import csu.yulin.exception.ParamsException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 接口请求日志添加请求类
 *
 * @author 刘飘
 */
@Schema(name = "ApiRequestLogAddRequest", description = "接口请求日志添加请求类")
@Data
public class ApiRequestLogAddRequest implements Serializable {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 接口ID
     */
    @Schema(description = "接口ID")
    private Long apiId;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    private String requestParams;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private String responseData;

    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private Integer statusCode;

    /**
     * 响应时间
     */
    @Schema(description = "响应时间")
    private Long responseTime;

    /**
     * 请求结果
     */
    @Schema(description = "请求结果")
    private String requestResult;

    /**
     * IP地址
     */
    @Schema(description = "IP地址")
    private String ipAddress;

    /**
     * 设备信息
     */
    @Schema(description = "设备信息")
    private String deviceInfo;

    /**
     * 请求来源
     */
    @Schema(description = "请求来源")
    private String requestSource;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    @Serial
    private static final long serialVersionUID = 1L;

    public void validateApiRequestLogAddRequest() {
        if (Objects.isNull(userId) || Objects.isNull(apiId) || Objects.isNull(responseTime)
                || Objects.isNull(statusCode) || StringUtils.isAnyBlank(requestParams, responseData, requestResult,
                ipAddress, deviceInfo, requestSource)) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);
        }

        if (userId <= 0 || apiId <= 0 || responseTime <= 0 || statusCode <= 0) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}
