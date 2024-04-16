package csu.yulin.client.time;

import csu.yulin.YulinApiSdkClient;

import java.net.ConnectException;
import java.time.LocalDateTime;

/**
 * @author 刘飘
 */
public class QueryTimeByZoneRequestExample {
    public void example(YulinApiSdkClient client) throws ConnectException {
        // 创建请求对象
        QueryTimeByZoneRequest request = new QueryTimeByZoneRequest();
        // 设置请求参数
        request.setTimezone("Asia/Shanghai");
        // 执行请求
        QueryTimeByZoneResponse response = client.execute(request);

        // 获取响应结果
        boolean isSuccess = response.isSuccess();
        // 判断请求是否成功
        if (isSuccess) {
            LocalDateTime localTime = response.getLocalTime();
            String code = response.getCode();
            String message = response.getMessage();
            System.out.println("localTime: " + localTime);
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
