package csu.yulin.controller;

import csu.yulin.common.ResultResponse;
import csu.yulin.model.dto.AdditionRequest;
import csu.yulin.model.dto.EmailRequest;
import csu.yulin.model.vo.EmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author 刘飘
 */
@Slf4j
@RestController
@RequestMapping
public class CommonController {


    /**
     * 获取当前时区的时间
     *
     * @param timezone 时区
     * @return 当前时区的时间
     */
    @GetMapping("/local-time")
    public ResultResponse<String> getLocalTime(@RequestParam String timezone) {
        if (timezone == null || timezone.isBlank()) {
            return ResultResponse.failure("400", "时区不能为空");
        }

        LocalDateTime localTime = LocalDateTime.now(ZoneId.of(timezone));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ResultResponse.success(localTime.format(formatter));
    }

    /**
     * 两数相加
     *
     * @param additionRequest 包含两个数的请求对象
     * @return 两数相加的结果
     */
    @PostMapping("/addition")
    public ResultResponse<Double> addition(@RequestBody AdditionRequest additionRequest) {
        Double result = additionRequest.getNum1() + additionRequest.getNum2();
        return ResultResponse.success(result);
    }

    /**
     * 发送邮件
     *
     * @param emailRequest 邮件请求对象
     * @return 邮件发送结果
     */
    @PostMapping("/send-email")
    public ResultResponse<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest) {
        if (Objects.isNull(emailRequest) || StringUtils.isAnyBlank(emailRequest.getRecipient(),
                emailRequest.getSubject(), emailRequest.getContent())) {
            return ResultResponse.failure("400", "邮件信息不能为空");
        }
        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setStatus("success");
        emailResponse.setMessage("邮件发送成功");
        return ResultResponse.success(emailResponse);
    }
}
