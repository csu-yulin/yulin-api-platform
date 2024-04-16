package csu.yulin.utils;

import csu.yulin.model.entity.ApiInfo;
import csu.yulin.model.entity.ApiMate;
import csu.yulin.properties.SDKProperties;
import csu.yulin.service.ApiInfoService;
import csu.yulin.service.impl.ApiMateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JavaClassGeneratorUtils {
    private SDKProperties sdkProperties;

    @Resource
    private ApiInfoService apiInfoService;

    @Resource
    private ApiMateService apiMateService;

    private final Configuration cfg;

    @Autowired
    public JavaClassGeneratorUtils(SDKProperties sdkProperties) {
        this.sdkProperties = sdkProperties;
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(JavaClassGeneratorUtils.class, sdkProperties.getTemplatePath());
        cfg.setDefaultEncoding("UTF-8");

    }

    /**
     * @param className      类名
     * @param subPackageName 子包名
     * @param httpMethod     请求方法
     * @param path           请求路径
     * @param fields         请求参数
     */
    public void generateJavaRequestClass(String className, String subPackageName, String httpMethod, String path, Map<String, String>[] fields) throws IOException, TemplateException {
        Template template = cfg.getTemplate(sdkProperties.getRequestTemplate());

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", sdkProperties.getPackageName());
        dataModel.put("subPackageName", subPackageName);
        dataModel.put("className", className);
        dataModel.put("httpMethod", httpMethod);
        dataModel.put("path", path);
        dataModel.put("fields", fields);

        File outputDirectory = new File(sdkProperties.getOutputPath() + subPackageName);
        outputDirectory.mkdirs();

        File outputFile = new File(outputDirectory, className + ".java");
        FileWriter writer = new FileWriter(outputFile);

        template.process(dataModel, writer);

        writer.close();
    }


    /**
     * @param className      类名
     * @param subPackageName 子包名
     * @param imports        导入包
     * @param fields         响应参数
     */
    public void generateJavaResponseClass(String className, String subPackageName, List<String> imports, Map<String, String>[] fields) throws IOException, TemplateException {
        Template template = cfg.getTemplate(sdkProperties.getResponseTemplate());

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", sdkProperties.getPackageName());
        dataModel.put("subPackageName", subPackageName);
        dataModel.put("className", className);
        dataModel.put("imports", imports);
        dataModel.put("fields", fields);

        File outputDirectory = new File(sdkProperties.getOutputPath() + subPackageName);
        outputDirectory.mkdirs();

        File outputFile = new File(outputDirectory, className + ".java");
        FileWriter writer = new FileWriter(outputFile);

        template.process(dataModel, writer);

        writer.close();
    }


    /**
     * @param className      类名
     * @param subPackageName 子包名
     * @param imports        导入包
     * @param fields         请求参数
     * @param responseFields 响应参数
     */
    public void generateJavaResponseExampleClass(String className, String subPackageName, String outputDir, List<String> imports, Map<String, String>[] fields, Map<String, String>[] responseFields) throws IOException, TemplateException {
        Template template = cfg.getTemplate(sdkProperties.getRequestExampleTemplate());

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", sdkProperties.getPackageName());
        dataModel.put("subPackageName", subPackageName);
        dataModel.put("className", className);
        dataModel.put("imports", imports);
        dataModel.put("fields", fields);
        dataModel.put("responseFields", responseFields);

        File outputDirectory = new File(sdkProperties.getOutputPath() + subPackageName);
        outputDirectory.mkdirs();

        File outputFile = new File(outputDirectory, className + "Example.java");
        FileWriter writer = new FileWriter(outputFile);

        template.process(dataModel, writer);

        writer.close();
    }


    /**
     * @param id API Id
     */
    public void generate(Long id) throws TemplateException, IOException {
        log.info("==================================开始生成SDK==================================");
        // 获取API信息
        ApiInfo api = apiInfoService.getById(id);
        ApiMate apiMate = apiMateService.getApiMateByApiId(id);
        String subPackageName = apiMate.getSubPackageName();
        String className = apiMate.getClassName();
        log.info("API 信息: {}", api);
        log.info("API Mate 信息: {}", apiMate);


        log.info("==================================请求类生成==================================");
        String path = api.getPath();
        String httpMethod = api.getHttpMethod();
        Map[] requestFields = Arrays.stream(api.getResponseParameters().split(",")).map(kv -> {
            String[] split = kv.split(":");
            return Map.of("type", split[0], "name", split[1], "comment", "暂时使用默认的");
        }).toArray(Map[]::new);

        generateJavaRequestClass(className, subPackageName, httpMethod, path, requestFields);
        log.info("==================================请求类生成完成==================================");


        log.info("==================================响应类生成==================================");
        // 创建参数和字段列表
        // TODO: 没有什么好的解决方法，只能先写死
        List<String> imports = List.of(
                "com.fasterxml.jackson.annotation.JsonProperty;",
                "com.fasterxml.jackson.databind.annotation.JsonDeserialize;",
                "csu.yulin.serialization.LocalDateTimeDeserializer;",
                "com.fasterxml.jackson.annotation.JsonProperty;",
                "java.time.LocalDateTime;"
        );
        Map<String, String>[] responseFields = new Map[]{
                Map.of("type", "LocalDateTime", "name", "localTime", "comment", "本地时间",
                        "annotations", List.of("@JsonDeserialize(using = LocalDateTimeDeserializer.class)",
                                "@JsonProperty(\"data\")"))
        };

        generateJavaResponseClass(className, subPackageName, imports, responseFields);
        log.info("==================================响应类生成完成==================================");


        log.info("==================================请求模板类生成==================================");
        String outputPath = sdkProperties.getOutputPath();
        List<String> imports1 = List.of(
                "java.time.LocalDateTime;"
        );

        Map<String, String>[] fields1 = new Map[]{
                Map.of("name", "timezone", "example", "\"Asia/Shanghai\"")
        };


        Map<String, String>[] responseFields1 = new Map[]{
                Map.of("type", "LocalDateTime", "name", "localTime")
        };
        generateJavaResponseExampleClass(className, subPackageName, outputPath, imports, fields1, responseFields1);
        log.info("==================================请求模板类生成完成==================================");
        log.info("==================================SDK生成完成==================================");
    }
}
