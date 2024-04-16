package csu.yulin.common.response;

/**
 * 基础结果接口，定义了获取响应码和错误描述的方法。
 */
public interface BaseResultInterface {

    /**
     * 获取响应码。
     *
     * @return 响应码
     */
    String getResultCode();

    /**
     * 获取错误描述。
     *
     * @return 错误描述
     */
    String getResultMsg();
}