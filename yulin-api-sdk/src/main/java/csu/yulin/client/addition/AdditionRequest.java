package csu.yulin.client.addition;

import csu.yulin.client.AbstractRequest;
import csu.yulin.enums.HttpMethod;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘飘
 */
@EqualsAndHashCode(callSuper = true)
@Setter
public class AdditionRequest extends AbstractRequest<AdditionResponse> {

    /**
     * 加数1
     */
    private Double num1;

    /**
     * 加数2
     */
    private Double num2;


    /**
     * 接口请求方法
     *
     * @return 请求方法
     */
    @Override
    public String getMethod() {
        return HttpMethod.POST.getValue();
    }

    /**
     * 接口请求路径
     *
     * @return 请求路径
     */
    @Override
    public String getPath() {
        return "/addition";
    }

    /**
     * 接口请求参数
     *
     * @return 请求参数
     */
    @Override
    public Map<String, String> getApiParams() {
        return null;
    }

    /**
     * 请求实体
     *
     * @return 请求实体
     */
    @Override
    public Object getBody() {
        return Map.of(
                "num1", Double.toString(num1),
                "num2", Double.toString(num2));
    }

    /**
     * 请求响应实体
     *
     * @return 请求响应实体的Class对象
     */
    @Override
    public Class<AdditionResponse> getResponseClass() {
        return AdditionResponse.class;
    }
}
