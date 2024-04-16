package csu.yulin.common;

import csu.yulin.common.response.ResultCode;
import csu.yulin.exception.ParamsException;
import csu.yulin.utils.ThrowUtils;
import lombok.Data;

/**
 * @author 刘飘
 */
@Data
public class IdRequest {
    private Long id;

    public void validateIdRequest() {
        ThrowUtils.throwIf(id == null || id <= 0, new ParamsException(ResultCode.ID_IS_INVALID));
    }
}