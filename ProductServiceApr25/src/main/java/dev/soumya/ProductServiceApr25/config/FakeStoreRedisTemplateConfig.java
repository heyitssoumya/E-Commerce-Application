package dev.soumya.ProductServiceApr25.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.soumya.ProductServiceApr25.dto.FakeStoreProductDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class FakeStoreRedisTemplateConfig {
    @Bean
    public RedisTemplate<String, FakeStoreProductDTO> fakeStoreRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, FakeStoreProductDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //Pass mapper in constructor instead of using setObjectMapper()
        Jackson2JsonRedisSerializer<FakeStoreProductDTO> serializer =
                new Jackson2JsonRedisSerializer<>(mapper, FakeStoreProductDTO.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
