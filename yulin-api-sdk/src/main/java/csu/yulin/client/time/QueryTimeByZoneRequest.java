package csu.yulin.client.time;

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
public class QueryTimeByZoneRequest extends AbstractRequest<QueryTimeByZoneResponse> {

    /**
     * 时区
     */
    private String timezone;

    /**
     * 接口请求方法
     *
     * @return 请求方法
     */
    @Override
    public String getMethod() {
        return HttpMethod.GET.getValue();
    }

    /**
     * 接口请求路径
     *
     * @return 请求路径
     */
    @Override
    public String getPath() {
        return "/local-time";
    }

    /**
     * 接口请求参数
     *
     * @return 请求参数
     */
    @Override
    public Map<String, String> getApiParams() {
        return Map.of("timezone", timezone);
    }

    /**
     * 请求实体
     *
     * @return 请求实体
     */
    @Override
    public Object getBody() {
        return null;
    }

    /**
     * 请求响应实体
     *
     * @return 请求响应实体的Class对象
     */
    @Override
    public Class<QueryTimeByZoneResponse> getResponseClass() {
        return QueryTimeByZoneResponse.class;
    }
}
