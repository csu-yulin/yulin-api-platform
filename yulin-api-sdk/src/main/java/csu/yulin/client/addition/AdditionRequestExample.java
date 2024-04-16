package csu.yulin.client.addition;

import csu.yulin.YulinApiSdkClient;

import java.net.ConnectException;

/**
 * @author 刘飘
 */
public class AdditionRequestExample {
    public void example(YulinApiSdkClient client) throws ConnectException {
        // 创建请求对象
        AdditionRequest request = new AdditionRequest();
        // 设置请求参数
        request.setNum1(1.0);
        request.setNum2(2.0);
        // 执行请求
        AdditionResponse response = client.execute(request);

        // 获取响应结果
        boolean isSuccess = response.isSuccess();
        // 判断请求是否成功
        if (isSuccess) {
            Double result = response.getResult();
            String code = response.getCode();
            String message = response.getMessage();
            System.out.println("result: " + result);
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
