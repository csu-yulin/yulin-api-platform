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
 * @TableName user
 */
@TableName(value = "user")
@Data
@Schema(name = "User", description = "用户实体类")
public class User implements Serializable {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 电子邮箱
     */
    @Schema(description = "电子邮箱")
    private String email;

    /**
     * 电话
     */
    @Schema(description = "电话")
    private String phone;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 性别
     */
    @Schema(description = "性别（male | female | secret）")
    private String gender;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 用户角色
     */
    @Schema(description = "用户角色（ROLE_USER | ROLE_ADMIN）")
    private String role;

    /**
     * Access Key
     */
    @Schema(description = "Access Key")
    private String accessKey;

    /**
     * Secret Key
     */
    @Schema(description = "Secret Key")
    private String secretKey;

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
     * 用户状态:0: 禁用, 1: 启用
     */
    @Schema(description = "用户状态:0: 禁用, 1: 启用")
    private Integer status;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    @TableLogic
    @JsonIgnore
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}