package csu.yulin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import csu.yulin.common.response.ResultCode;
import csu.yulin.enums.UserRoleEnum;
import csu.yulin.exception.UserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 工具类，用于生成和解析 JSON Web Token。
 */
@Component
public class JwtUtil {

    /**
     * JWT 密钥
     */
    @Value("{jwt.secret}")
    private String secret;

    /**
     * 生成 JWT Token
     *
     * @param username 用户名
     * @param role     用户角色
     * @return 生成的 JWT Token
     */
    public String generateToken(String username, UserRoleEnum role) {
        Date now = new Date();
        // 设置 Token 的过期时间为一个小时
        long expire = 60 * 60 * 1000L;
        Date expireDate = new Date(now.getTime() + expire);

        return JWT.create()
                .setHeader("typ", "jwt")
                .setHeader("alg", "HS256")
                .setPayload("username", username)
                .setPayload("role", role.getValue())
                .setIssuedAt(now)
                .setExpiresAt(expireDate)
                .setKey(secret.getBytes())
                .sign();
    }

    /**
     * 解析 JWT Token
     *
     * @param token JWT Token
     * @return 解析后的 JWT 对象
     */
    public JWT parseJWT(String token) {
        try {
            // 验证 Token 是否过期
            JWTValidator.of(token).validateAlgorithm(getJwtSigner()).validateDate(DateUtil.date());
            return JWT.of(token);
        } catch (Exception e) {
            // 解析失败抛出未授权异常
            throw new UserException(ResultCode.USER_TOKEN_INVALID);
        }
    }

    /**
     * 获取 JWT 签名器
     *
     * @return JWT 签名器
     */
    public JWTSigner getJwtSigner() {
        return JWTSignerUtil.hs256(secret.getBytes());
    }
}