package csu.yulin.model.dto.userApiRelation;

import csu.yulin.common.response.ResultCode;
import csu.yulin.exception.ParamsException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * @author 刘飘
 */
@Schema(name = "UserApiRelationAddRequest", description = "用户接口关系添加请求类")
@Data
public class UserApiRelationAddRequest implements Serializable {
    /**
     * id
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * id
     */
    @Schema(description = "用户ID")
    private List<Long> apiIds;

    @Serial
    private static final long serialVersionUID = 1L;

    public void validateUserApiRelationAddRequest() {
        if ((Objects.isNull(userId) || userId <= 0) || (Objects.isNull(apiIds) || apiIds.isEmpty())) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);
        }
    }
}
