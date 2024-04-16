package csu.yulin.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class KeyGeneratorUtils {

    private static final int ITERATIONS = 10000;
    private static final String SECRETKEY = "vavsntrh245yWEGVFBN^EJH431DSVAFSDBYT578^23";
    private static final int KEY_LENGTH = 256; // Key的长度，单位：位

    /**
     * 生成随机的 Access Key
     *
     * @return 随机生成的 Access Key
     */
    public static String generateAccessKey() {
        return generateRandomKey();
    }

    /**
     * 生成随机的 Secret Key
     *
     * @return 随机生成的 Secret Key
     */
    public static String generateSecretKey() {
        return generateRandomKey();
    }

    private static String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16]; // 16字节的盐值
        secureRandom.nextBytes(salt);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(SECRETKEY.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
}