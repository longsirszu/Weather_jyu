package cn.edu.jyu.weather;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGeneratorTest {

    @Test
    public void generateStandardPemKeys() throws Exception {
        // 生成器初始化
        Ed25519KeyPairGenerator generator = new Ed25519KeyPairGenerator();
        generator.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();

        // 1. 生成符合 PKCS8 标准的私钥 (给 YAML 用)
        PrivateKeyInfo privInfo = PrivateKeyInfoFactory.createPrivateKeyInfo(keyPair.getPrivate());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privInfo.getEncoded());

        // 2. 生成符合 X.509 标准的公钥 (给和风控制台用)
        SubjectPublicKeyInfo pubInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(keyPair.getPublic());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(pubInfo.getEncoded());

        System.out.println("========= 这里的公钥上传给和风控制台 =========");
        System.out.println("-----BEGIN PUBLIC KEY-----");
        System.out.println(publicKeyBase64);
        System.out.println("-----END PUBLIC KEY-----");

        System.out.println("\n========= 这里的私钥填入 application.yml =========");
        System.out.println("-----BEGIN PRIVATE KEY-----");
        System.out.println(privateKeyBase64);
        System.out.println("-----END PRIVATE KEY-----");
    }
}