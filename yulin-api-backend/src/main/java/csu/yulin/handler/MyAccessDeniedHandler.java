//package csu.yulin.handler;
//
//import cn.hutool.json.JSONUtil;
//import csu.yulin.common.response.ResultCode;
//import csu.yulin.common.response.ResultResponse;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * 访问被拒绝的处理器
// */
//@Component
//public class MyAccessDeniedHandler implements AccessDeniedHandler {
//
//    /**
//     * 处理访问被拒绝的情况
//     */
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        // 构建拒绝访问响应信息
//        String resp = JSONUtil.toJsonStr(ResultResponse.failure(ResultCode.USER_NO_PERMISSION));
//        // 设置响应编码和内容类型
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        // 输出响应信息
//        response.getWriter().println(resp);
//        response.getWriter().flush();
//    }
//}