package csu.yulin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 刘飘
 */
@Component
@ConfigurationProperties(prefix = "sdk")
@Data
public class SDKProperties {

    /**
     * 主包名
     */
    private String packageName;

    /**
     * 模板路径
     */
    private String templatePath;

    /**
     * 请求类模板文件
     */
    private String requestTemplate;

    /**
     * 响应类模板文件
     */
    private String responseTemplate;

    /**
     * 请求示例类模板文件
     */
    private String requestExampleTemplate;

    /**
     * 响应示例类模板文件
     */
    private String outputPath;
}