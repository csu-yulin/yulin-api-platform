package csu.yulin.model.dto.user;

import csu.yulin.common.PageRequest;
import csu.yulin.common.response.ResultCode;
import csu.yulin.enums.UserGenderEnum;
import csu.yulin.enums.UserRoleEnum;
import csu.yulin.exception.ParamsException;
import csu.yulin.utils.RegexUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "UserQueryRequest", description = "用户查询请求")
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @Schema(description = "用户ID")
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
    @Schema(description = "用户状态", allowableValues = {"0", "1"})
    private Integer status;

    @Serial
    private static final long serialVersionUID = 1L;

    public void validateUserQueryRequest() {
        if ((!StringUtils.isBlank(email) && !RegexUtils.checkEmail(email)) ||
                (!StringUtils.isBlank(phone) && !RegexUtils.checkPhone(phone)) ||
                (!StringUtils.isBlank(gender) && !UserGenderEnum.containsGender(gender)) ||
                (!StringUtils.isBlank(role) && !UserRoleEnum.containsRole(role)) ||
                (!Objects.isNull(status) && status != 0 && status != 1)) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}