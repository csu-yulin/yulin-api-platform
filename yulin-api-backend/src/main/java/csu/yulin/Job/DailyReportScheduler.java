package csu.yulin.Job;

import csu.yulin.model.entity.vo.apiRequestLog.StatisticalReportVO;
import csu.yulin.service.ApiRequestLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 * @author 刘飘
 */
@Component
@Slf4j
public class DailyReportScheduler {
    @Resource
    private ApiRequestLogService apiRequestLogService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${report.path}")
    private String reportPath;

    //    @Scheduled(cron = "0 0 0 * * ?") // 每天0:00执行
    @Scheduled(cron = "0 50 0 * * ?")
    public void execute() {
        log.info("开始执行每日报表任务");
        // 业务逻辑
        LocalDate yesterday = LocalDate.now().minusDays(1);
        StatisticalReportVO report =
                apiRequestLogService.getStatisticalReportOnSpecifiedDate(yesterday);
        // 将报表存入Redis
        redisTemplate.opsForValue().set(yesterday.toString(), report);
        // 同时将报表进行持久化存储,以文件的形式存储在服务器上
        // 构建文件名
        String fileName = reportPath + yesterday + ".csv";

        try (FileWriter writer = new FileWriter(fileName)) {
            // 写入表头
            writer.append("TotalRequestCount,AverageResponseTime,SlowestResponseTime,FastestResponseTime,SuccessRate\n");

            // 写入数据行
            writer.append(String.format("%d,%d,%d,%d,%.2f\n",
                    report.getTotalRequestCount(),
                    report.getAverageResponseTime(),
                    report.getSlowestResponseTime(),
                    report.getFastestResponseTime(),
                    report.getSuccessRate()));

            // 提示文件写入成功
            System.out.println("Report file has been written: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing report file: " + e.getMessage());
        }

        log.info("每日报表任务执行完成");
    }
}