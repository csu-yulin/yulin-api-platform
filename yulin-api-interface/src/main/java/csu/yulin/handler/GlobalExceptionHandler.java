package csu.yulin.handler;

import csu.yulin.common.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;

/**
 * 全局异常处理器
 *
 * @author 刘飘
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常对象
     * @return 返回封装的结果响应
     */
    @ExceptionHandler(DateTimeException.class)
    public ResultResponse<?> businessExceptionHandler(DateTimeException e) {
        log.error("DateTimeException occurred", e);
        return ResultResponse.failure("400", "无效的时区");
    }
}
