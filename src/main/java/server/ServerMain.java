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
    private static boolean isRunning = true; // Server control flag

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            System.out.println("Type 'stop' to shut down the server.");

            // Start a separate thread for console commands
            new Thread(() -> handleConsoleInput(serverSocket, threadPool)).start();

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    threadPool.execute(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    if (isRunning) {
                        System.out.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            threadPool.shutdown();
            System.out.println("Server shutting down...");
        }
    }

    private static void handleConsoleInput(ServerSocket serverSocket, ExecutorService threadPool) {
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("stop")) {
                System.out.println("Stopping server...");
                isRunning = false;
                try {
                    serverSocket.close(); // Force stop accept() loop
                } catch (IOException e) {
                    System.out.println("Error closing server socket: " + e.getMessage());
                }
                threadPool.shutdown();
                System.out.println("Server has stopped.");
                break;
            }
        }
        scanner.close();
    }
}
