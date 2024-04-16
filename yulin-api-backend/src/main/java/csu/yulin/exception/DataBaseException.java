package csu.yulin.exception;

import csu.yulin.common.response.ResultCode;

public class DataBaseException extends BaseException {

    public DataBaseException(ResultCode resultCode) {
        super(resultCode);
    }
}
