package com.kakao.assignment;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CocoaGinkgoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void jasyptEncoding() {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword("kakaoassignment");

        String apiKey = "KakaoAK 332bb307e35d65b793cdeac9e3de85a7";
        String enc_apiKey = pbeEnc.encrypt(apiKey);
        System.out.println("enc_apiKey = " + enc_apiKey);
//        tREsoGFN9qGQHCGofeS4orD2wOwPpoZFM0o0dAz83y7miOx2zgteOxQEdTzXD+xlJdK0XSKbLd0=

        String clientId = "gWm0ElB1RX6DAjcxGfQ8";
        String enc_clientId = pbeEnc.encrypt(clientId);
        System.out.println("enc_clientId = " + enc_clientId);
//        3m3wvKvqqgxdHj8lPvShcw8WZ8TsfgHNz3XieSPcETDGJKKWm5D3jid9ipigE5rEHAzt8bDrn9M=

        String clientSecret = "v5sxoZKdW6";
        String enc_clientSecret = pbeEnc.encrypt(clientSecret);
        System.out.println("enc_clientSecret = " + enc_clientSecret);
//        XlmN+h7DWAY6LegZZclAc5m6nXL2qYsmWoH0FoBd08XCEueCjcAn4KFiH/Lz9uSnQWpGktDykv8=
    }

}
