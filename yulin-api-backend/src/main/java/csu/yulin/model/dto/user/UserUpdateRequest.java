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
import java.util.Objects;

/**
 * 用户更新请求
 */
@Data
@Schema(name = "UserUpdateRequest", description = "用户更新请求体")
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    @Schema(description = "用户ID")
    private Long id;

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
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

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

    public void validateUserUpdateRequest() {
        if (Objects.isNull(id)) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);
        }

        if (id <= 0 ||
                (!StringUtils.isBlank(email) && !RegexUtils.checkEmail(email)) ||
                (!StringUtils.isBlank(phone) && !RegexUtils.checkPhone(phone)) ||
                (!StringUtils.isBlank(password) && !RegexUtils.checkPassword(password)) ||
                (!StringUtils.isBlank(gender) && !UserGenderEnum.containsGender(gender)) ||
                (!StringUtils.isBlank(role) && !UserRoleEnum.containsRole(role)) ||
                (!Objects.isNull(status) && status != 0 && status != 1)) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}