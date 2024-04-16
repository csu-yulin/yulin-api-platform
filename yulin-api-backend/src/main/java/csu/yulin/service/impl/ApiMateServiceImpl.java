package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.mapper.ApiMateMapper;
import csu.yulin.model.entity.ApiMate;
import org.springframework.stereotype.Service;

/**
 * @author 刘飘
 * @description 针对表【api_mate】的数据库操作Service实现
 * @createDate 2024-04-15 15:27:51
 */
@Service
public class ApiMateServiceImpl extends ServiceImpl<ApiMateMapper, ApiMate>
        implements ApiMateService {

    /**
     * 根据apiId获取ApiMate
     *
     * @param apiId apiId
     * @return ApiMate
     */
    @Override
    public ApiMate getApiMateByApiId(Long apiId) {
        return getOne(Wrappers.lambdaQuery(ApiMate.class)
                .eq(ApiMate::getApiId, apiId));
    }
}




