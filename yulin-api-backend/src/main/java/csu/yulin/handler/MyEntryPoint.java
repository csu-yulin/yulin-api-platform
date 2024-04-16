//package csu.yulin.handler;
//
//import cn.hutool.json.JSONUtil;
//import csu.yulin.common.response.ResultCode;
//import csu.yulin.common.response.ResultResponse;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * 认证入口点，处理认证失败的情况
// */
//@Component
//public class MyEntryPoint implements AuthenticationEntryPoint {
//
//    /**
//     * 处理认证入口点，当认证失败时调用
//     */
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        // 设置响应内容类型和编码
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        // 构建未授权响应信息
//
//        String resp = JSONUtil.toJsonStr(ResultResponse.failure(ResultCode.UNAUTHORIZED));
//        // 输出响应信息
//        out.write(resp);
//        out.flush();
//        out.close();
//    }
//}