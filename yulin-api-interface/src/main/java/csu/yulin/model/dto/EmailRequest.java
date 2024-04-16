package csu.yulin.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 刘飘
 */
@Data
public class EmailRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
    
}