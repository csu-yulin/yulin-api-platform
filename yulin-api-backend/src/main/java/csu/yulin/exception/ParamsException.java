package csu.yulin.exception;

import csu.yulin.common.response.ResultCode;

public class ParamsException extends BaseException {

    public ParamsException(ResultCode resultCode) {
        super(resultCode);
    }
}