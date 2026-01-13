package cn.edu.jyu.weather;

import cn.edu.jyu.weather.config.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtPropertiesTest {

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    public void testInjection() {
        System.out.println("=== 测试配置注入 ===");
        System.out.println("ALG: " + jwtProperties.getHeader().getAlg());
        System.out.println("KID: " + jwtProperties.getHeader().getKid());
        System.out.println("SUB: " + jwtProperties.getPayload().getSub());
        // 验证私钥是否读取成功（只打印长度防止泄露）
        String key = jwtProperties.getSigning().getPrivateKey();
        System.out.println("Private Key Length: " + (key != null ? key.length() : "NULL"));
    }
}
