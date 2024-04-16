package csu.yulin.utils;


import csu.yulin.exception.BaseException;

/**
 * 异常抛出工具类，用于根据条件抛出指定异常。
 */
public class ThrowUtils {
    /**
     * 根据条件抛出业务异常
     *
     * @param condition 条件表达式，如果为true，则抛出异常
     * @param exception 要抛出的业务异常对象
     */
    public static void throwIf(boolean condition, BaseException exception) {
        if (condition) {
            throw exception;
        }
    }
}
