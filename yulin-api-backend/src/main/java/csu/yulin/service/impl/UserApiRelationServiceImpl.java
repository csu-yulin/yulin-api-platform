package csu.yulin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import csu.yulin.common.response.ResultCode;
import csu.yulin.exception.DataBaseException;
import csu.yulin.mapper.UserApiRelationMapper;
import csu.yulin.model.entity.UserApiRelation;
import csu.yulin.service.UserApiRelationService;
import csu.yulin.utils.ThrowUtils;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Objects;

/**
 * @author 刘飘
 * @description 针对表【user_api_relation】的数据库操作Service实现
 * @createDate 2024-04-04 17:43:56
 */
@DubboService
public class UserApiRelationServiceImpl extends ServiceImpl<UserApiRelationMapper, UserApiRelation>
        implements UserApiRelationService {

    /**
     * 根据用户ID和API ID查询用户API关系
     *
     * @param userId 用户ID
     * @param apiId  API ID
     * @return 用户API关系
     */
    @Override
    public UserApiRelation getByUserIdAndApiId(Long userId, Long apiId) {
        UserApiRelation userApiRelation = getOne(Wrappers.lambdaQuery(UserApiRelation.class)
                .eq(UserApiRelation::getUserId, userId)
                .eq(UserApiRelation::getApiId, apiId));

        ThrowUtils.throwIf(Objects.isNull(userApiRelation), new DataBaseException(ResultCode.ENTITY_NOT_EXISTED));

        return userApiRelation;
    }

    /**
     * 判断用户是否还有次数调用接口
     *
     * @param id 用户ID
     * @return 是否还有次数调用接口
     */
    @Override
    public boolean isCouldInvoke(Long id) {
        UserApiRelation userApiRelation = getById(id);

        Integer total = userApiRelation.getTotal();
        Integer called = userApiRelation.getCalled();

        return (total - called > 0);
    }

    /**
     * 增加调用次数
     *
     * @param id 用户ID
     */
    @Override
    public void increaseCallCount(Long id) {
        UserApiRelation userApiRelation = getById(id);
        userApiRelation.setCalled(userApiRelation.getCalled() + 1);
        updateById(userApiRelation);
    }
}




