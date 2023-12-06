package com.axisbank.request.statement.encryption;

import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;

@Component
@Log
public class AESEncryptionUtil {

    @Value("#{${encryption.secret.key}}")
    private Map<String,String> secreteKeys;

    @Value("#{${encryption.salt.key}}")
    private Map<String,String> saltKeys;

    @Value("${encryption.iv.key}")
    private String ivKey;

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public String encrypt(String strToEncrypt,String channel) {
        try {
            byte[] bytesIV = ivKey.getBytes(StandardCharsets.UTF_8);
            IvParameterSpec ivspec = new IvParameterSpec(bytesIV);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec spec = new PBEKeySpec(secreteKeys.get(channel).toCharArray(), saltKeys.get(channel).getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.severe("Error while encrypting: " + e.toString());
        }
        return null;
    }
    public String decrypt(String strToDecrypt, String channel) {
        try {
            byte[] bytesIV = ivKey.getBytes(StandardCharsets.UTF_8);
            IvParameterSpec ivspec = new IvParameterSpec(bytesIV);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec spec = new PBEKeySpec(secreteKeys.get(channel).toCharArray(), saltKeys.get(channel).getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            log.severe("Error while decrypting: " + e.toString());
        }
        return null;

    }
}
