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
 * 用户-接口信息关系实体类
 *
 * @author 刘飘
 */
@TableName(value = "user_api_relation")
@Data
@Schema(name = "UserApiRelation", description = "用户-接口信息关系实体类")
public class UserApiRelation implements Serializable {
    /**
     * 用户-接口信息关系ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户-接口信息关系ID")
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
     * 总调用次数
     */
    @Schema(description = "总调用次数")
    private Integer total;

    /**
     * 已调用次数
     */
    @Schema(description = "已调用次数")
    private Integer called;

    /**
     * 状态: 0-禁用, 1-启用
     */
    @Schema(description = "状态: 0-禁用, 1-启用")
    private Integer status;

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
     * 是否删除
     */
    @TableLogic
    @JsonIgnore
    @Schema(description = "是否删除")
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}