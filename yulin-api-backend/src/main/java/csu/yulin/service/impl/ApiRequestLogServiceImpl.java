package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.mapper.ApiRequestLogMapper;
import csu.yulin.model.entity.ApiRequestLog;
import csu.yulin.model.entity.vo.apiRequestLog.StatisticalReportVO;
import csu.yulin.service.ApiRequestLogService;
import org.apache.dubbo.config.annotation.DubboService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

/**
 * @author 刘飘
 * @description 针对表【api_request_log】的数据库操作Service实现
 * @createDate 2024-04-04 19:56:45
 */
@DubboService
public class ApiRequestLogServiceImpl extends ServiceImpl<ApiRequestLogMapper, ApiRequestLog>
        implements ApiRequestLogService {

    /**
     * 获取指定日期的接口请求日志
     *
     * @param date 指定日期
     * @return 指定日期的接口请求日志
     */
    @Override
    public StatisticalReportVO getStatisticalReportOnSpecifiedDate(LocalDate date) {
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.MAX);

        List<ApiRequestLog> list = list(Wrappers.lambdaQuery(ApiRequestLog.class)
                .ge(ApiRequestLog::getRequestTime, startDateTime)
                .le(ApiRequestLog::getRequestTime, endDateTime));

        StatisticalReportVO.StatisticalReportVOBuilder builder = StatisticalReportVO.builder();
        builder.date(startDateTime);

        if (!list.isEmpty()) {
            LongSummaryStatistics stats = list.stream()
                    .collect(Collectors.summarizingLong(ApiRequestLog::getResponseTime));

            long successCount = list.stream()
                    .filter(log -> "Success".equals(log.getRequestResult()))
                    .count();

            double successRate = (double) successCount / list.size() * 100;

            builder.totalRequestCount((long) list.size())
                    .averageResponseTime((long) stats.getAverage())
                    .slowestResponseTime(stats.getMax())
                    .fastestResponseTime(stats.getMin())
                    .successRate(successRate);
        } else {
            builder.totalRequestCount(0L)
                    .averageResponseTime(0L)
                    .slowestResponseTime(0L)
                    .fastestResponseTime(0L)
                    .successRate(0.0);
        }

        return builder.build();
    }
}




