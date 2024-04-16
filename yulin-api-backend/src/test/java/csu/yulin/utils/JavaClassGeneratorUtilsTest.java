package csu.yulin.utils;

import csu.yulin.model.entity.ApiInfo;
import csu.yulin.service.ApiInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class JavaClassGeneratorUtilsTest {
    @Resource
    private ApiInfoService apiInfoService;


    @Test
    public void f() {
        ApiInfo api = apiInfoService.getById(1776494521355350017L);
        log.info("API 信息: {}", api);
    }

}