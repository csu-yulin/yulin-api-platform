package csu.yulin.handler;

import cn.hutool.json.JSONUtil;
import csu.yulin.common.response.ResultCode;
import csu.yulin.common.response.ResultResponse;
import csu.yulin.exception.BaseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

/**
 * 全局异常处理器
 *
 * @author 刘飘
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    /**
     * 统一处理异常
     *
     * @param e 业务异常对象
     * @return 返回封装的结果响应
     */
    @ExceptionHandler(BaseException.class)
    public ResultResponse<?> businessExceptionHandler(BaseException e) {
        log.error("异常发生", e);
        return ResultResponse.failure(ResultCode.DATABASE_OPERATION_FAILED);
    }

    /**
     * 处理违反唯一约束的数据库异常
     *
     * @param e SQLIntegrityConstraintViolationException 异常对象
     * @return 返回封装的结果响应，表示唯一约束违反
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResultResponse<?> businessExceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error("SQLIntegrityConstraintViolationException  occurred", e);
        return ResultResponse.failure(ResultCode.CONSTRAINT_VIOLATION);
    }

    /**
     * 处理SQL语法错误的数据库异常
     *
     * @param e SQLSyntaxErrorException 异常对象
     * @return 返回封装的结果响应，表示SQL语法错误
     */
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResultResponse<?> businessExceptionHandler(SQLSyntaxErrorException e) {
        log.error("SQLIntegrityConstraintViolationException  occurred", e);
        return ResultResponse.failure(ResultCode.DATABASE_EXECUTION_FAILED);
    }

    /**
     * 处理访问被拒绝的情况
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 构建拒绝访问响应信息
        String resp = JSONUtil.toJsonStr(ResultResponse.failure(ResultCode.USER_NO_PERMISSION));
        // 设置响应编码和内容类型
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        // 输出响应信息
        response.getWriter().println(resp);
        response.getWriter().flush();
    }

    /**
     * 处理认证入口点，当认证失败时调用
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 设置响应内容类型和编码
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        // 构建未授权响应信息

        String resp = JSONUtil.toJsonStr(ResultResponse.failure(ResultCode.UNAUTHORIZED));
        // 输出响应信息
        out.write(resp);
        out.flush();
        out.close();
    }
}
