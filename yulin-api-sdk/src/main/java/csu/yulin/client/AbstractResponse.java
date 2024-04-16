package csu.yulin.client;

import lombok.Data;

import java.util.Objects;

/**
 * @author 刘飘
 */
@Data
public class AbstractResponse {
    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return Objects.equals(code, "200");
    }
}