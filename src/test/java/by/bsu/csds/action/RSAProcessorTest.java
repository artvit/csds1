package by.bsu.csds.action;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.Security;
import java.util.Arrays;

public class RSAProcessorTest {
    @BeforeClass
    public static void initClass() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void encryptDecryptTest() {
        String testString = "89748923873209147073409871043710897430710934709173409870173410974091";
        byte[] testData = testString.getBytes();
        SecretKey secretKey = SerpentProcessor.generateKey();
        byte[] encryptedBytes = SerpentProcessor.encrypt(testData, secretKey);
        byte[] decryptedBytes = SerpentProcessor.decrypt(encryptedBytes, secretKey);
        Assert.assertTrue(Arrays.equals(testData, decryptedBytes));
    }
}
