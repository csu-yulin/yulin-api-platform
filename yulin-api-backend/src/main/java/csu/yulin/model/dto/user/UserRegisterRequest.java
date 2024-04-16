package csu.yulin.model.dto.user;

import csu.yulin.common.response.ResultCode;
import csu.yulin.enums.UserGenderEnum;
import csu.yulin.exception.ParamsException;
import csu.yulin.utils.RegexUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
@Schema(name = "UserRegisterRequest", description = "用户注册请求体")
public class UserRegisterRequest implements Serializable {
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
     * 性别
     */
    @Schema(description = "性别")
    private String gender;

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    public void validateUserRegisterRequest() {
        if (StringUtils.isAnyBlank(username, email, password, phone, gender)) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);

        }

        if (!UserGenderEnum.containsGender(gender) ||
                !RegexUtils.checkEmail(email) ||
                !RegexUtils.checkPhone(phone) ||
                !RegexUtils.checkPassword(password)) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}
