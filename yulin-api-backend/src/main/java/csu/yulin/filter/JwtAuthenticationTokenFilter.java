package csu.yulin.filter;

import cn.hutool.jwt.JWT;
import csu.yulin.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用于处理JWT认证，验证请求中的JWT令牌，并将用户信息存储到Spring Security的上下文中。
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 这个方法用于实现JWT认证的逻辑。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取JWT令牌
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            // 如果令牌为空，说明用户未进行认证，直接放行
            filterChain.doFilter(request, response);
            return;
        }

        // 解析JWT令牌
        JWT jwt = jwtUtil.parseJWT(token);
        String username = (String) jwt.getPayload("username");

        // 根据用户名从数据库中获取用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 将用户信息存入Spring Security的上下文中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 继续执行过滤器链中的下一个过滤器
        filterChain.doFilter(request, response);
    }
}