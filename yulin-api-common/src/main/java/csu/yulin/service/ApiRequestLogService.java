package csu.yulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.model.entity.ApiRequestLog;
import csu.yulin.model.entity.vo.apiRequestLog.StatisticalReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 刘飘
 * @description 针对表【api_request_log】的数据库操作Service
 * @createDate 2024-04-04 19:56:45
 */
public interface ApiRequestLogService extends IService<ApiRequestLog> {

    /**
     * 获取指定日期的接口请求日志
     *
     * @param date 指定日期
     * @return 指定日期的接口请求日志
     */
    StatisticalReportVO getStatisticalReportOnSpecifiedDate(LocalDate date);
}
