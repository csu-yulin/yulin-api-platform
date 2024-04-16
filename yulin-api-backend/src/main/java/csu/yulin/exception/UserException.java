package csu.yulin.exception;

import csu.yulin.common.response.ResultCode;

public class UserException extends BaseException {

    public UserException(ResultCode resultCode) {
        super(resultCode);
    }
}