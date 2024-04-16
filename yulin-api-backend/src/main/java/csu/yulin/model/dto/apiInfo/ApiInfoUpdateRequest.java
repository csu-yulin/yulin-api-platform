package csu.yulin.model.dto.apiInfo;

import csu.yulin.common.response.ResultCode;
import csu.yulin.enums.HttpMethodEnum;
import csu.yulin.exception.ParamsException;
import csu.yulin.utils.RegexUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 接口信息更新请求类
 *
 * @author 刘飘
 */
@Data
@Schema(name = "ApiInfoUpdateRequest", description = "接口信息更新请求体")
public class ApiInfoUpdateRequest implements Serializable {
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
     * 接口状态:0: 禁用, 1: 启用
     */
    @Schema(description = "接口状态:0: 禁用, 1: 启用")
    private Integer status;

    @Serial
    private static final long serialVersionUID = 1L;

    public void validateApiInfoUpdateRequest() {
        if (Objects.isNull(id)) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);
        }
        if (id <= 0 ||
                (!StringUtils.isBlank(httpMethod) && !HttpMethodEnum.containsMethod(httpMethod)) ||
                (!StringUtils.isBlank(requestParameters) && !RegexUtils.isJSON(requestParameters)) ||
                (!StringUtils.isBlank(responseParameters) && !RegexUtils.isJSON(responseParameters)) ||
                (!StringUtils.isBlank(responseExample) && !RegexUtils.isJSON(responseExample)) ||
                (!Objects.isNull(status) && status != 0 && status != 1)) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}