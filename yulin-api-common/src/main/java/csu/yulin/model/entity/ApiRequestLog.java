package csu.yulin.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口请求日志实体类
 *
 * @author 刘飘
 */
@TableName("api_request_log")
@Data
@Schema(name = "ApiRequestLog", description = "接口请求日志实体类")
public class ApiRequestLog implements Serializable {
    /**
     * 日志ID
     */
    @Schema(description = "日志ID")
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

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
     * 请求时间
     */
    @Schema(description = "请求时间")
    private LocalDateTime requestTime;

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
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
