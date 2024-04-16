package csu.yulin.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 刘飘
 */
@Data
public class EmailResponse implements Serializable {

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

