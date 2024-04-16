package csu.yulin.model.entity.vo.apiRequestLog;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 刘飘
 */
@Data
@Builder
public class StatisticalReportVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime date;
    private Long totalRequestCount;
    private Long averageResponseTime;
    private Long slowestResponseTime;
    private Long fastestResponseTime;
    private Double successRate;
}
