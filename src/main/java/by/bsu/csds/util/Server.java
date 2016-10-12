package by.bsu.csds.util;

import by.bsu.csds.config.Config;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        try (ServerSocket serverSocket = new ServerSocket(Config.PORT)) {
            new Thread() {
                @Override
                public void run() {
                    System.out.println("print '" + Config.EXIT_COMMAND + "' to shutdown server");
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();
                    if (Config.EXIT_COMMAND.equals(input)) {
                        System.exit(0);
                    }
                }
            }.start();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ServerThread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
