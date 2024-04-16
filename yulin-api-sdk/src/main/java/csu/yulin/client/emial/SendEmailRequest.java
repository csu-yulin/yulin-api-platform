package csu.yulin.client.emial;

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
public class SendEmailRequest extends AbstractRequest<SendEmailResponse> {

    /*
     * 收件人
     */
    private String recipient;

    /*
     * 主题
     */
    private String subject;

    /*
     * 内容
     */
    private String content;

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
        return "/send-email";
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
                "recipient", recipient,
                "subject", subject,
                "content", content
        );
    }

    /**
     * 请求响应实体
     *
     * @return 请求响应实体的Class对象
     */
    @Override
    public Class<SendEmailResponse> getResponseClass() {
        return SendEmailResponse.class;
    }
}
