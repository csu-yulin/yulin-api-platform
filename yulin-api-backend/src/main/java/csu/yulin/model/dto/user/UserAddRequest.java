package csu.yulin.model.dto.user;

import csu.yulin.common.response.ResultCode;
import csu.yulin.enums.UserGenderEnum;
import csu.yulin.enums.UserRoleEnum;
import csu.yulin.exception.ParamsException;
import csu.yulin.utils.RegexUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户添加请求类
 */
@Schema(name = "UserAddRequest", description = "用户添加请求类")
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String username;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 电话
     */
    @Schema(description = "电话")
    private String phone;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    private String password;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private String gender;

    /**
     * 用户角色
     */
    @Schema(description = "用户角色")
    private String role;

    /**
     * 用户状态:0: 禁用, 1: 启用
     */
    @Schema(description = "用户状态:0: 禁用, 1: 启用")
    private Integer status;

    @Serial
    private static final long serialVersionUID = 1L;

    public void validateUserAddRequest() {
        if (StringUtils.isAnyBlank(username, email, password, phone, gender, role)) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);
        }

        if (!UserGenderEnum.containsGender(gender) ||
                !UserRoleEnum.containsRole(role) ||
                !RegexUtils.checkEmail(email) ||
                !RegexUtils.checkPhone(phone) ||
                !RegexUtils.checkPassword(password)
        ) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }

        if (status == null || (status != 0 && status != 1)) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}