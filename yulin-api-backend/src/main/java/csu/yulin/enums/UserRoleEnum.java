package csu.yulin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 */
@AllArgsConstructor
@Getter
public enum UserRoleEnum {
    USER("用户", "ROLE_USER"),
    ADMIN("管理员", "ROLE_ADMIN");

    private final String text;

    private final String value;

    /**
     * 根据字符串值获取对应的 UserRoleEnum 枚举
     *
     * @param value 字符串值
     * @return 对应的 UserRoleEnum 枚举，如果未找到则返回 null
     */
    public static UserRoleEnum fromValue(String value) {
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        return null;
    }

    // 判断角色字符串是否存在于枚举中的方法
    public static boolean containsRole(String role) {
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            if (roleEnum.getValue().equals(role)) {
                return true;
            }
        }
        return false;
    }
}


