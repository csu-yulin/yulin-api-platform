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
@Schema(name = "UserApiRelationAddCountRequest", description = "用户接口关系添加次数请求类")
@Data
public class UserApiRelationAddCountRequest implements Serializable {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 接口Id
     */
    @Schema(description = "接口Id")
    private List<Long> apiIds;

    /**
     * 调用次数
     */
    @Schema(description = "调用次数")
    private Integer count;

    @Serial
    private static final long serialVersionUID = 1L;

    public void validateUserApiRelationAddCountRequest() {
        if ((Objects.isNull(userId) || userId <= 0) || (Objects.isNull(apiIds) || apiIds.isEmpty())) {
            throw new ParamsException(ResultCode.PARAMS_IS_BLANK);
        }

        if (Objects.isNull(count) || count <= 0) {
            throw new ParamsException(ResultCode.PARAMS_IS_INVALID);
        }
    }
}