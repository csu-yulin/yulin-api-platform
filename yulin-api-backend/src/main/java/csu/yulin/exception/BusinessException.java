package csu.yulin.exception;


import csu.yulin.common.response.ResultCode;

/**
 * 业务异常类
 *
 * @author 刘飘
 */
public class BusinessException extends BaseException {

    public BusinessException(ResultCode resultCode) {
        super(resultCode);
    }
}

