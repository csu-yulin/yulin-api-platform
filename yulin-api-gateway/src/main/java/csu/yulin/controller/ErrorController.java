package csu.yulin.controller;

import csu.yulin.common.ResultResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘飘
 */
@RestController
public class ErrorController {
    @RequestMapping("/error")
    public ResultResponse<Object> handleError() {
        return ResultResponse.failure("请求失败，请检查请求路径是否正确或者携带了正确的请求头!");
    }
}
