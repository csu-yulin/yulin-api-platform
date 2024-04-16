package csu.yulin.model.dto.user;

import csu.yulin.common.response.ResultCode;
import csu.yulin.exception.ParamsException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求
 */
@Schema(name = "UserLoginRequest", description = "用户登录请求")
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    public void validateUserLoginRequest() {
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);
        }
    }
}
