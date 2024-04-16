package csu.yulin.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类，用于配置 MyBatis Plus 的功能。
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 配置 MyBatis Plus 的拦截器，包括分页功能。
     *
     * @return 配置好的 MyBatis Plus 拦截器对象。
     */
    @Bean
    public MybatisPlusInterceptor interceptor() {
        // 创建 MyBatis Plus 的拦截器实例
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 添加分页插件到拦截器中
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 返回配置好的拦截器实例
        return mybatisPlusInterceptor;
    }
}