package by.bsu.csds.action;

import by.bsu.csds.config.Config;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SerpentProcessor {
    public static byte[] encrypt(byte[] data, SecretKey key) {
        byte[] cipherText = null;
        try {
            Cipher cipher = Cipher.getInstance(Config.SERPENT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(Config.INIT_VECTOR));
            cipherText = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException |
                InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static byte[] decrypt(byte[] data, SecretKey key) {
        byte[] decryptedText = null;
        try {
            Cipher cipher = Cipher.getInstance(Config.SERPENT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Config.INIT_VECTOR));
            decryptedText = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException |
                InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    public static SecretKey generateKey() {
        SecretKey serpentSessionKey = null;
        try {
            KeyGenerator serpentKeyGenerator = KeyGenerator.getInstance(Config.SERPENT_KEYGEN_ALGORITHM);
            serpentKeyGenerator.init(128, new SecureRandom());
            serpentSessionKey = serpentKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return serpentSessionKey;
    }

    public static SecretKey decodeKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, Config.SERPENT_KEYGEN_ALGORITHM);
    }
}
