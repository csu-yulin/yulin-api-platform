package csu.yulin.exception;

import csu.yulin.common.response.ResultCode;
import lombok.Getter;

/**
 * 基本异常类
 *
 * @author 刘飘
 */
@Getter
public class BaseException extends RuntimeException {

    /**
     * 结果码
     */
    private final ResultCode resultCode;

    public BaseException(ResultCode resultCode) {
        // 传入错误消息,以便能够在控制台进行监控
        super(resultCode.getResultMsg());
        this.resultCode = resultCode;
    }
}
