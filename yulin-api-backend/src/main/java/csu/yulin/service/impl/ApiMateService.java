package csu.yulin.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.model.entity.ApiMate;

/**
 * @author 刘飘
 * @description 针对表【api_mate】的数据库操作Service
 * @createDate 2024-04-15 15:27:51
 */
public interface ApiMateService extends IService<ApiMate> {

    /**
     * 根据apiId获取ApiMate
     *
     * @param apiId apiId
     * @return ApiMate
     */
    ApiMate getApiMateByApiId(Long apiId);
}
