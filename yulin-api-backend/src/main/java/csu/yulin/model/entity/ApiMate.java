package csu.yulin.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 刘飘
 * @TableName api_mate
 */
@TableName(value = "api_mate")
@Data
public class ApiMate implements Serializable {
    /**
     * 元数据Id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 接口ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long apiId;

    /**
     * 包名
     */
    private String subPackageName;

    /**
     * 类名
     */
    private String className;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}