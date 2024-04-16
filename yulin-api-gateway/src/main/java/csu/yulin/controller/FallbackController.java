package csu.yulin.controller;

import csu.yulin.common.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘飘
 */
@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public ResultResponse<Object> fallback() {
        return ResultResponse.failure("服务暂时不可用");
    }
}