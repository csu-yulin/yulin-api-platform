package csu.yulin.controller;

import csu.yulin.service.impl.ApiMateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 元数据接口
 *
 * @author 刘飘
 */
@Slf4j
@RestController
@RequestMapping("/apiMate")
public class ApiMateController {
    @Resource
    private ApiMateService apiMateService;


}