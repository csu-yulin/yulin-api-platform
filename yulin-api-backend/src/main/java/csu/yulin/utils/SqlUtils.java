package csu.yulin.utils;

import org.apache.commons.lang3.StringUtils;

public class SqlUtils {
    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField 排序字段
     * @return 排序字段是否合法，true表示合法，false表示不合法
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param camelCase 驼峰命名字符串
     * @return 下划线命名字符串
     */
    public static String camelCaseToSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        StringBuilder snakeCase = new StringBuilder();
        boolean prevUpperCase = false;

        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (!prevUpperCase) {
                    if (!snakeCase.isEmpty()) {
                        snakeCase.append('_');
                    }
                }
                prevUpperCase = true;
            } else {
                prevUpperCase = false;
            }
            snakeCase.append(Character.toLowerCase(c));
        }

        return snakeCase.toString();
    }
}