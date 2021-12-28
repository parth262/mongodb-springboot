package com.example.mongospringboottest.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Component
public class DecryptionbUtil {

    // private final Encoder BASE_64_ENCODER = Base64.getEncoder();
    private final Decoder BASE_64_DECODER = Base64.getDecoder();
    
    @Value("${app.cipher.algorithm}")
    private String CIPHER_ALGORITHM;
    @Value("${app.cipher.key}")
    private String CIPHER_KEY;
    @Value("${app.cipher.iv-length}")
    private Integer CIPHER_IV_LENGTH;

    private SecretKeySpec secretKeySpec;

    @PostConstruct
    public void init() {
        byte[] encrytionKeyBytes = BASE_64_DECODER.decode(CIPHER_KEY);
        this.secretKeySpec = new SecretKeySpec(encrytionKeyBytes, "AES");
    }

    public String decrypt(String encryptedValue) 
        throws NoSuchAlgorithmException, NoSuchPaddingException, 
            InvalidKeyException, InvalidAlgorithmParameterException, 
            IllegalBlockSizeException, BadPaddingException {

        byte[] encryptedValueBytes = BASE_64_DECODER.decode(encryptedValue);
        byte[] ivBytes = Arrays.copyOfRange(encryptedValueBytes, 0, CIPHER_IV_LENGTH);
        byte[] bytesToBeDecrypt = Arrays.copyOfRange(encryptedValueBytes, CIPHER_IV_LENGTH, encryptedValueBytes.length);

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(bytesToBeDecrypt);
        return new String(decryptedBytes);
    }
    
}
