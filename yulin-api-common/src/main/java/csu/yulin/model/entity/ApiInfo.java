package csu.yulin.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 刘飘
 * @TableName api_info
 */
@TableName(value = "api_info")
@Data
@Schema(name = "ApiInfo", description = "接口信息实体类")
public class ApiInfo implements Serializable {
    /**
     * 接口ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "接口ID")
    private Long id;

    /**
     * 接口名称
     */
    @Schema(description = "接口名称")
    private String name;

    /**
     * 接口描述
     */
    @Schema(description = "接口描述")
    private String description;

    /**
     * 接口路径
     */
    @Schema(description = "接口路径")
    private String path;

    /**
     * HTTP方法
     */
    @Schema(description = "HTTP方法")
    private String httpMethod;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    private String requestParameters;

    /**
     * 响应参数
     */
    @Schema(description = "响应参数")
    private String responseParameters;

    /**
     * 请求示例
     */
    @Schema(description = "请求示例")
    private String requestExample;

    /**
     * 响应示例
     */
    @Schema(description = "响应示例")
    private String responseExample;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    /**
     * 创建人
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    /**
     * 状态: 0-禁用, 1-启用
     */
    @Schema(description = "状态: 0-禁用, 1-启用")
    private Integer status;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    @JsonIgnore
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}