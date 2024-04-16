package csu.yulin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.model.entity.ApiInfo;

/**
 * @author 刘飘
 * @description 针对表【api_info】的数据库操作Service
 * @createDate 2024-04-04 12:29:50
 */
public interface ApiInfoService extends IService<ApiInfo> {

    /**
     * 获取用于查询API信息的查询包装器。
     *
     * @param apiInfo   API信息对象，用于构建查询条件。
     * @param sortField 排序字段，如果为 null 或空字符串，则不排序。
     * @param sortOrder 排序顺序，可选值为 "asc"（升序）或 "desc"（降序）。
     *                  如果为 null 或空字符串，则不排序。
     * @return 查询包装器
     */
    QueryWrapper<ApiInfo> getQueryWrapper(ApiInfo apiInfo, String sortField, String sortOrder);

    /**
     * 判断API是否存在
     *
     * @param path API路径
     * @return 是否存在
     */
    boolean isApiExist(String path);

    /**
     * 根据API路径获取API ID
     *
     * @param path API路径
     * @return API ID
     */
    Long getApiIdByPath(String path);
}
