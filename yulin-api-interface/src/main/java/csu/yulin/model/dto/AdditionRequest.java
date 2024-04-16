package csu.yulin.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 刘飘
 */
@Data
public class AdditionRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 第一个数
     */
    private double num1;

    /**
     * 第二个数
     */
    private double num2;

}
