<#-- 定义模板变量，用于填充生成的Java类名 -->
package ${packageName}.client.${subPackageName};

<#-- 导入必要的类 -->
<#list imports as imp>
import ${imp};
</#list>

import ${packageName}.YulinApiSdkClient;

import java.net.ConnectException;

/**
 * 作者: 刘飘
 */
public class ${className}Example {

    public void example(YulinApiSdkClient client) throws ConnectException {
        // 创建请求对象
    ${className} request = new ${className}();

    <#-- 设置请求参数 -->
        <#list fields as field>
        request.set${field.name?cap_first}(${field.example});
        </#list>

        // 执行请求
    ${className?replace("Request", "Response")} response = client.execute(request);

        // 获取响应结果
        boolean isSuccess = response.isSuccess();
        // 判断请求是否成功
        if (isSuccess) {
            <#list responseFields as field>
                ${field.type} ${field.name} =response.get${field.name?cap_first}();
            </#list>
            String code = response.getCode();
            String message = response.getMessage();
            <#list responseFields as field>
            System.out.println("${field.name}: " + ${field.name});
            </#list>
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
