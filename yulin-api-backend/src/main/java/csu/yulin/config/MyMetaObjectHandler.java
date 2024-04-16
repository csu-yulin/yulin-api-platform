package csu.yulin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import csu.yulin.model.entity.User;
import csu.yulin.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author 刘飘
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 对于时间这些字段数据库设置了自动更新策略就不需要额外设置了
    // 如果有需要添加更新人的需求,需要完成相应逻辑后在相应的字段上添加一下注解
    //    @TableField(fill = FieldFill.INSERT_UPDATE)

    @Resource
    private UserService userService;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
//        this.strictInsertFill(metaObject, "createdTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
//        this.strictInsertFill(metaObject, "updatedTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)

        User loginUser = userService.getLoginUser();
        this.strictInsertFill(metaObject, "creator", Long.class, loginUser.getId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info("start update fill ....");
//
//        this.strictUpdateFill(metaObject, "updatedTime", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
    }
}