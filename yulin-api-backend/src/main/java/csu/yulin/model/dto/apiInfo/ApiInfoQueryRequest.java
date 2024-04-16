package csu.yulin.model.dto.apiInfo;

import csu.yulin.common.PageRequest;
import csu.yulin.common.response.ResultCode;
import csu.yulin.enums.HttpMethodEnum;
import csu.yulin.exception.ParamsException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 接口查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "ApiInfoQueryRequest", description = "接口查询请求")
public class ApiInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
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
     * 接口状态:0: 禁用, 1: 启用
     */
    @Schema(description = "接口状态:0: 禁用, 1: 启用")
    private Integer status;

    @Serial
    private static final long serialVersionUID = 1L;

    public void validateApiInfoQueryRequest() {
        if ((!StringUtils.isBlank(httpMethod) && !HttpMethodEnum.containsMethod(httpMethod)) ||
                (!Objects.isNull(status) && status != 0 && status != 1)) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}