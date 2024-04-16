package csu.yulin.exception;

import csu.yulin.common.response.ResultCode;

public class ApiException extends BaseException {

    public ApiException(ResultCode resultCode) {
        super(resultCode);
    }
}