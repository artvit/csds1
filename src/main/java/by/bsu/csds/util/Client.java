package by.bsu.csds.util;

import by.bsu.csds.action.RSAProcessor;
import by.bsu.csds.action.SerpentProcessor;
import by.bsu.csds.config.Config;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Scanner;

public class Client {
    private static Scanner inputScanner;

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        inputScanner = new Scanner(System.in);
        KeyPair rsaKeys = getRSAKeyPair();
        if (rsaKeys == null) {
            System.err.println("No rsa keys");
            return;
        }
        try (Socket socket = new Socket("localhost", Config.PORT);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
            String userInputString;
            while (true) {
                System.out.println("Print filename or '" + Config.EXIT_COMMAND + "'");
                userInputString = inputScanner.nextLine();
                if (userInputString == null || userInputString.isEmpty()) {
                    continue;
                }
                userInputString = userInputString.trim();
                outputStream.writeObject(userInputString);
                if (Config.EXIT_COMMAND.equals(userInputString)) {
                    break;
                }
                outputStream.writeObject(rsaKeys.getPublic());
                outputStream.flush();
                int dataLength = inputStream.readInt();
                byte[] keyData = new byte[dataLength];
                inputStream.readFully(keyData);
                keyData = RSAProcessor.decrypt(keyData, rsaKeys.getPrivate());
                SecretKey sessionKey = SerpentProcessor.decodeKey(keyData);
                int encryptedDataLength = inputStream.readInt();
                if (encryptedDataLength == Config.ERROR_FILE_NOT_FOUND) {
                    System.out.println("File not found!");
                    continue;
                }
                byte[] encryptedData = new byte[encryptedDataLength];
                inputStream.readFully(encryptedData);
                byte[] decryptedData = SerpentProcessor.decrypt(encryptedData, sessionKey);
                String fileText = new String(decryptedData);
                System.out.println(fileText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static KeyPair getRSAKeyPair() {
        KeyPair rsaKeys = null;
        Path rsaSavedKeysPath = Paths.get(Config.RSA_CLIENT_KEY_FILE);
        boolean generateNewKeys = false;
        if (Files.exists(rsaSavedKeysPath)) {
            System.out.println("Generate new key?(yes|no)");
            String result = inputScanner.nextLine();
            if ("yes".equals(result)) {
                generateNewKeys = true;
            } else {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(Files.newInputStream(rsaSavedKeysPath))) {
                    rsaKeys = (KeyPair) objectInputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            generateNewKeys = true;
        }
        if (generateNewKeys) {
            try {
                rsaKeys = RSAProcessor.generateKeyPair();
                if (rsaKeys != null) {
                    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(rsaSavedKeysPath))) {
                        objectOutputStream.writeObject(rsaKeys);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rsaKeys;
    }
}
