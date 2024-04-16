package csu.yulin;

import csu.yulin.http.impl.HutoolHttpClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 刘飘
 */
@AutoConfiguration
@ConditionalOnClass(YulinApiSdkClient.class)
@EnableConfigurationProperties(value = YulinApiSdkClient.class)
public class YulinApiSdkAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public YulinApiSdkClient yulinApiSdkClient() {
        return new YulinApiSdkClient(new HutoolHttpClient());
    }
}
