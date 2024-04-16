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
public enum UserGenderEnum {
    MALE("男性", "male"),
    FEMALE("女性", "female"),
    SECRET("保密", "secret");

    private final String text;

    private final String value;

    // 判断性别字符串是否存在于枚举中的方法
    public static boolean containsGender(String gender) {
        for (UserGenderEnum genderEnum : UserGenderEnum.values()) {
            if (genderEnum.getValue().equals(gender)) {
                return true;
            }
        }
        return false;
    }
}