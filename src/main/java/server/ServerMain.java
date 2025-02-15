// File: server/ServerMain.java
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static final int PORT = 4321;
    private static final int THREAD_POOL_SIZE = 10;
    private static boolean isRunning = false;
    private static ExecutorService threadPool;
    private static ServerSocket serverSocket;


    public static void main(String[] args) {
        System.out.println("Server started via console...");
        startServer();
    }

    /** Start Server Method */
    public static void startServer() {
        if (isRunning) {
            System.out.println("Server is already running.");
            return;
        }

        isRunning = true;
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        new Thread(ServerMain::handleConsoleInput).start(); // Console commands

        try (ServerSocket socket = new ServerSocket(PORT)) {
            serverSocket = socket;
            System.out.println("Server started on port " + PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            if (isRunning) {
                System.out.println("Server error: " + e.getMessage());
            }
        } finally {
            threadPool.shutdown();
            System.out.println("Server shutting down...");
        }
    }

    /** Stop Server Method */
    public static void stopServer() {
        if (!isRunning) {
            System.out.println("Server is not running.");
            return;
        }

        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing server socket: " + e.getMessage());
        }
        threadPool.shutdown();
        System.out.println("Server has stopped.");
    }


    private static void handleConsoleInput() {
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("stop")) {
                stopServer();
                break;
            }
        }
        scanner.close();
    }
}
