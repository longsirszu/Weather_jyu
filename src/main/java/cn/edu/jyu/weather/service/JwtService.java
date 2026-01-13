package cn.edu.jyu.weather.service;

import cn.edu.jyu.weather.config.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    @Autowired
    private JwtProperties jwtProperties;

    // Jackson 对象复用，提高性能
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken() {
        try {
            // 准备私钥
            // 从配置读取私钥字符串
            String rawKey = jwtProperties.getSigning().getPrivateKey();

            // 标准清洗逻辑：去除头尾、换行和空格，兼容各种 YAML 写法
            String privateKeyPem = rawKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            // 解析为 Ed25519 私钥参数
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
            AsymmetricKeyParameter privateKeyParams = PrivateKeyFactory.createKey(keyBytes);

            // 确保是 Ed25519 格式
            if (!(privateKeyParams instanceof Ed25519PrivateKeyParameters)) {
                throw new RuntimeException("提供的私钥不是 Ed25519 格式！");
            }


            // 构造 JWT (标准逻辑)
            // 获取当前系统时间
            long now = Instant.now().getEpochSecond();

            // 设置30s的时间容错
            long iat = now - 30;

            // 计算过期时间
            long ttl = jwtProperties.getPayload().getTtl();
            long exp = iat + ttl;

            // Header: 包含 alg 和 kid
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("alg", "EdDSA");
            headerMap.put("kid", jwtProperties.getHeader().getKid());

            // Payload: 包含 sub, iat, exp
            Map<String, Object> payloadMap = new HashMap<>();
            payloadMap.put("sub", jwtProperties.getPayload().getSub());
            payloadMap.put("iat", iat);
            payloadMap.put("exp", exp);

            // 转JSON
            String headerJson = objectMapper.writeValueAsString(headerMap);
            String payloadJson = objectMapper.writeValueAsString(payloadMap);

            // Base64URL 编码 (无填充)
            String headerBase64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadBase64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));


            // 签名
            String contentToSign = headerBase64 + "." + payloadBase64;
            byte[] dataToSign = contentToSign.getBytes(StandardCharsets.UTF_8);

            Ed25519Signer signer = new Ed25519Signer();
            signer.init(true, privateKeyParams); // true 表示签名模式
            signer.update(dataToSign, 0, dataToSign.length);
            byte[] signature = signer.generateSignature();

            String signatureBase64 = base64UrlEncode(signature);

            // 拼接最终 Token
            return contentToSign + "." + signatureBase64;

        } catch (Exception e) {
            log.error("JWT生成失败", e);
            throw new RuntimeException("JWT生成异常: " + e.getMessage());
        }
    }

    /**
     * Base64URL 编码工具方法
     */
    private String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }
}