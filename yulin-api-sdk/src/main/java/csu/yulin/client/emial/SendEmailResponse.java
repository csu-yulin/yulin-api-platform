package csu.yulin.client.emial;

import csu.yulin.client.AbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 刘飘
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendEmailResponse extends AbstractResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态
     */
    private String status;

    /**
     * 消息
     */
    private String message;
}
