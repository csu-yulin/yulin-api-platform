package csu.yulin.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Swagger配置类，用于配置Swagger生成的API文档信息。
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置Swagger的OpenAPI对象。
     *
     * @return 配置好的OpenAPI对象
     */
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Spring Boot3 Restful API") // 设置API文档的标题
                        .description("API文档") // 设置API文档的描述
                        .version("v1") // 设置API版本号
                        .license(new License().name("Apache 2.0").url("https://springdoc.org"))) // 设置API的许可信息
                .externalDocs(new ExternalDocumentation()
                        .description("外部文档") // 设置外部文档的描述
                        .url("https://springshop.wiki.github.org/docs")); // 设置外部文档的URL
    }

}
