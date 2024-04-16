package csu.yulin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author 刘飘
 */
public class HeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String source = request.getHeader("Source");
        if (!"GateWay".equals(source)) {
            // 如果请求头中没有 source: gateway，可以根据需求进行处理，比如返回错误信息或重定向到错误页面
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid source");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在请求处理完成后执行，可以不做任何处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求完成后的回调方法，可以不做任何处理
    }
}
