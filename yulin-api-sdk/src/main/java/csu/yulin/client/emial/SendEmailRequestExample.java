package csu.yulin.client.emial;

import csu.yulin.YulinApiSdkClient;

import java.net.ConnectException;

/**
 * @author 刘飘
 */
public class SendEmailRequestExample {

    public void example(YulinApiSdkClient client) throws ConnectException {
        // 创建请求对象
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        // 设置请求参数
        sendEmailRequest.setSubject("测试邮件");
        sendEmailRequest.setContent("测试邮件内容");
        sendEmailRequest.setRecipient("lp");
        // 执行请求
        SendEmailResponse response = client.execute(sendEmailRequest);

        // 获取响应结果
        boolean isSuccess = response.isSuccess();
        // 判断请求是否成功
        if (isSuccess) {
            String status = response.getStatus();
            String message = response.getMessage();
            String code = response.getCode();
            System.out.println("status: " + status);
            System.out.println("code: " + code);
            System.out.println("message: " + message);
        } else {
            String code = response.getCode();
            String message = response.getMessage();
            System.out.println("code: " + code);
            System.out.println("message: " + message);
        }
    }
}
