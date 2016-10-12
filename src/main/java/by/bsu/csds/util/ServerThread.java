package by.bsu.csds.util;

import by.bsu.csds.action.RSAProcessor;
import by.bsu.csds.action.SerpentProcessor;
import by.bsu.csds.config.Config;

import javax.crypto.SecretKey;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;

public class ServerThread extends Thread {
    private Socket clientSocket;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {
            SecretKey serpentSessionKey = SerpentProcessor.generateKey();
            while (true) {
                String inputString = (String) inputStream.readObject();
                if (Config.EXIT_COMMAND.equals(inputString)) {
                    break;
                }
                Path filePath  = Paths.get(Config.FILES_FOLDER + inputString);
                PublicKey publicRSAKey = (PublicKey) inputStream.readObject();
                if (Files.notExists(filePath)) {
                    outputStream.writeInt(Config.ERROR_FILE_NOT_FOUND);
                    continue;
                }
                byte[] sessionKeyBytes = serpentSessionKey.getEncoded();
                byte[] encryptedKey = RSAProcessor.encrypt(sessionKeyBytes, publicRSAKey);
                outputStream.writeInt(encryptedKey.length);
                outputStream.write(encryptedKey);
                outputStream.flush();
                System.out.println();
                byte[] fileContent = Files.readAllBytes(filePath);
                byte[] encryptedFileContent = SerpentProcessor.encrypt(fileContent, serpentSessionKey);
                outputStream.writeInt(encryptedFileContent.length);
                outputStream.write(encryptedFileContent);
                outputStream.flush();
            }
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
