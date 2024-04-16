package csu.yulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import csu.yulin.model.entity.UserApiRelation;

/**
 * @author 刘飘
 * @description 针对表【user_api_relation】的数据库操作Service
 * @createDate 2024-04-04 17:43:56
 */
public interface UserApiRelationService extends IService<UserApiRelation> {

    /**
     * 根据用户ID和API ID查询用户API关系
     *
     * @param userId 用户ID
     * @param apiId  API ID
     * @return 用户API关系
     */
    UserApiRelation getByUserIdAndApiId(Long userId, Long apiId);

    /**
     * 判断用户是否还有次数调用接口
     *
     * @param id 用户ID
     * @return 是否还有次数调用接口
     */
    boolean isCouldInvoke(Long id);

    /**
     * 增加调用次数
     *
     * @param id 用户ID
     */
    void increaseCallCount(Long id);
}
