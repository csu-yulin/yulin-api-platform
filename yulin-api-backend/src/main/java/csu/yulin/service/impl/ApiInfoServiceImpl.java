package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.common.response.ResultCode;
import csu.yulin.constant.CommonConstant;
import csu.yulin.exception.DataBaseException;
import csu.yulin.mapper.ApiInfoMapper;
import csu.yulin.model.entity.ApiInfo;
import csu.yulin.service.ApiInfoService;
import csu.yulin.utils.SqlUtils;
import csu.yulin.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Objects;

/**
 * @author 刘飘
 * @description 针对表【api_info】的数据库操作Service实现
 * @createDate 2024-04-04 12:29:50
 */
@DubboService
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo>
        implements ApiInfoService {

    /**
     * 获取用于查询API信息的查询包装器。
     *
     * @param apiInfo   API信息对象，用于构建查询条件。
     * @param sortField 排序字段，如果为 null 或空字符串，则不排序。
     * @param sortOrder 排序顺序，可选值为 "asc"（升序）或 "desc"（降序）。
     *                  如果为 null 或空字符串，则不排序。
     * @return 查询包装器
     */
    @Override
    public QueryWrapper<ApiInfo> getQueryWrapper(ApiInfo apiInfo, String sortField, String sortOrder) {
        Long id = apiInfo.getId();
        String name = apiInfo.getName();
        String description = apiInfo.getDescription();
        String path = apiInfo.getPath();
        String httpMethod = apiInfo.getHttpMethod();
        Integer status = apiInfo.getStatus();

        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(Objects.nonNull(status), "status", status);
        queryWrapper.eq(StringUtils.isNotBlank(httpMethod), "http_method", httpMethod);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(path), "path", path);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                SqlUtils.camelCaseToSnakeCase(sortField));

        return queryWrapper;
    }

    /**
     * 判断API是否存在
     *
     * @param path API路径
     * @return 是否存在
     */
    @Override
    public boolean isApiExist(String path) {
        ApiInfo apiInfo = getOne(new QueryWrapper<ApiInfo>().eq("path", path));

        return Objects.nonNull(apiInfo);
    }

    /**
     * 根据API路径获取API ID
     *
     * @param path API路径
     * @return API ID
     */
    @Override
    public Long getApiIdByPath(String path) {
        ApiInfo apiInfo = getOne(new QueryWrapper<ApiInfo>().eq("path", path).select("id"));
        ThrowUtils.throwIf(Objects.isNull(apiInfo), new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return apiInfo.getId();
    }
}




