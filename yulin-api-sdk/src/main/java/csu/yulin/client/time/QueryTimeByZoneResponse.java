package csu.yulin.client.time;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import csu.yulin.client.AbstractResponse;
import csu.yulin.serialization.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 刘飘
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryTimeByZoneResponse extends AbstractResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 本地时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("data")
    private LocalDateTime localTime;
}
