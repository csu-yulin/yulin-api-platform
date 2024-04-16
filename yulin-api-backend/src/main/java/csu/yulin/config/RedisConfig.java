package csu.yulin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(new StringRedisSerializer());      // 设置默认的序列化器为 StringRedisSerializer
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());      // 设置默认的序列化器为 StringRedisSerializer

        redisTemplate.setConnectionFactory(redisConnectionFactory);                         // 设置 Redis 连接工厂
        return redisTemplate;// 返回配置好的 RedisTemplate 实例
    }
}