package csu.yulin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author 刘飘
 */
@AllArgsConstructor
@Getter
public enum UserStatusEnum {
    ENABLED("启用", 1),
    DISABLED("禁用", 0);

    private final String text;

    private final int value;
}
