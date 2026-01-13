package cn.edu.jyu.weather.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data // 使用Lombok自动生成Get/Set
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private Header header;
    private Payload payload;
    private Signing signing;

    @Data
    public static class Header {
        private String alg;
        private String kid;
    }

    @Data
    public static class Payload {
        private String sub;
        private long ttl;
    }

    @Data
    public static class Signing {
        private String privateKey;
    }
}