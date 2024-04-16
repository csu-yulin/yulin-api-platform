package csu.yulin.handler;

import csu.yulin.common.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class LocalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常对象
     * @return 返回封装的结果响应
     */
    @ExceptionHandler(RuntimeException.class)
    public ResultResponse<?> businessExceptionHandler(RuntimeException e) {
        log.error("Exception occurred", e);
        String message = e.getMessage();
        message = message.substring(message.indexOf(":") + 2, message.indexOf("\r"));
        return ResultResponse.failure(message);
    }
}
