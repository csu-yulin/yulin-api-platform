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

/**
 * 接口信息添加请求类
 *
 * @author 刘飘
 */
@Schema(name = "ApiInfoAddRequest", description = "接口信息添加请求类")
@Data
public class ApiInfoAddRequest implements Serializable {

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

    @Serial
    private static final long serialVersionUID = 1L;

    public void validateApiInfoAddRequest() {
        if (StringUtils.isAnyBlank(name, description, path, httpMethod, requestParameters, responseParameters,
                requestExample, responseExample)) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);
        }

        if (!HttpMethodEnum.containsMethod(httpMethod) ||
                !RegexUtils.isJSON(requestParameters) ||
                !RegexUtils.isJSON(responseParameters) ||
                !RegexUtils.isJSON(responseExample)) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}