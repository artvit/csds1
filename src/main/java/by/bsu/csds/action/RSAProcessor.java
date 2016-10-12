package by.bsu.csds.action;

import by.bsu.csds.config.Config;

import javax.crypto.Cipher;
import java.security.*;

public class RSAProcessor {
    public static byte[] encrypt(byte[] data, PublicKey key) {
        byte[] cipherText = null;
        try {
            Cipher cipher = Cipher.getInstance(Config.RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static byte[] decrypt(byte[] text, PrivateKey key) {
        byte[] decryptedText = null;
        try {
            Cipher cipher = Cipher.getInstance(Config.RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedText = cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Config.RSA_KEYGEN_ALGORITHM);
            keyGen.initialize(Config.RSA_KEY_LENGTH);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
