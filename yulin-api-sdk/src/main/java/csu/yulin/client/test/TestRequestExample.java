package csu.yulin.client.test;

import com.fasterxml.jackson.annotation.JsonProperty;;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;;
import csu.yulin.serialization.LocalDateTimeDeserializer;;
import com.fasterxml.jackson.annotation.JsonProperty;;
import java.time.LocalDateTime;;

import csu.yulin.YulinApiSdkClient;

import java.net.ConnectException;

/**
 * 作者: 刘飘
 */
public class TestRequestExample {

    public void example(YulinApiSdkClient client) throws ConnectException {
        // 创建请求对象
    TestRequest request = new TestRequest();

        request.setTimezone("Asia/Shanghai");

        // 执行请求
    TestResponse response = client.execute(request);

        // 获取响应结果
        boolean isSuccess = response.isSuccess();
        // 判断请求是否成功
        if (isSuccess) {
                LocalDateTime localTime =response.getLocalTime();
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
