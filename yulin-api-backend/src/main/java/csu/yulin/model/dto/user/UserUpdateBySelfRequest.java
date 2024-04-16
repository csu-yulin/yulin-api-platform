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
import java.util.Objects;

/**
 * 用户更新请求
 *
 * @author 刘飘
 */
@Data
@Schema(name = "UserUpdateBySelfRequest", description = "用户自行更新请求体")
public class UserUpdateBySelfRequest implements Serializable {
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


    @Serial
    private static final long serialVersionUID = 1L;

    public void validateUserUpdateBySelfRequest() {
        if (Objects.isNull(id)) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);

        }

        if (id <= 0 ||
                (!StringUtils.isBlank(email) && !RegexUtils.checkEmail(email)) ||
                (!StringUtils.isBlank(phone) && !RegexUtils.checkPhone(phone)) ||
                (!StringUtils.isBlank(password) && !RegexUtils.checkPassword(password)) ||
                (!StringUtils.isBlank(gender) && !UserGenderEnum.containsGender(gender))) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}