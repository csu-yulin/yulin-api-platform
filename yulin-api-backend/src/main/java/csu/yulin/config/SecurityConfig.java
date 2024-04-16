package csu.yulin.config;

import csu.yulin.filter.JwtAuthenticationTokenFilter;
import csu.yulin.handler.GlobalExceptionHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Spring Security配置类，用于配置安全策略和过滤器链。
 *
 * @author 刘飘
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

//    @Resource
//    private MyEntryPoint myEntryPoint;
//
//    @Resource
//    private MyAccessDeniedHandler accessDeniedHandler;

    @Resource
    private GlobalExceptionHandler exceptionHandler;

    /**
     * 配置AuthenticationManager，用于支持密码模式的身份验证。
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 配置密码编码器，使用BCryptPasswordEncoder。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置SecurityFilterChain，包括禁用CSRF、禁用session、配置授权规则和添加JWT认证过滤器。
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 禁用 session
                .authorizeHttpRequests(conf -> {
                    // 放行登录接口和注册接口
                    conf.requestMatchers("/user/login", "/user/register",
                            "/swagger-ui/**", "/v3/**", "/swagger-resources/**", "/webjars/**",
                            "/druid/**").permitAll();
                    // 其他请求需要身份验证
                    conf.anyRequest().authenticated();
                })
                // 在UsernamePasswordAuthenticationFilter之前添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置异常处理器
                .exceptionHandling(conf -> {
                    conf.authenticationEntryPoint(exceptionHandler);
                    conf.accessDeniedHandler(exceptionHandler);
                })
                .build();
    }
}