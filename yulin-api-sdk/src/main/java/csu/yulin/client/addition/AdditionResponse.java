package csu.yulin.client.addition;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AdditionResponse extends AbstractResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private Double result;
}
