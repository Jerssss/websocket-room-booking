package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static final int PORT = 4321; // Port to listen on
    private static final int THREAD_POOL_SIZE = 10; // Thread pool size for handling clients
    private static volatile boolean isRunning = false; // Server state flag
    private static ExecutorService threadPool; // Thread pool for client handlers
    private static ServerSocket serverSocket; // Server socket

    public static void main(String[] args) {
        System.out.println("Server started via console...");
        startServer();
    }

    /**
     * Starts the server.
     */
    public static void startServer() {
        if (isRunning) {
            System.out.println("Server is already running.");
            return;
        }

        isRunning = true;
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Start a thread to handle console input (e.g., "stop" command)
        new Thread(ServerMain::handleConsoleInput).start();

        try (ServerSocket socket = new ServerSocket(PORT)) {
            serverSocket = socket;
            System.out.println("Server started on port " + PORT);

            // Main server loop: accept client connections
            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    threadPool.execute(new ClientHandler(clientSocket)); // Handle client in a new thread
                } catch (IOException e) {
                    if (isRunning) {
                        System.out.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            if (isRunning) {
                System.out.println("Server error: " + e.getMessage());
            }
        } finally {
            // Cleanup resources
            if (threadPool != null) {
                threadPool.shutdownNow(); // Forcefully terminate all tasks
            }
            System.out.println("Server shutting down...");
        }
    }

    /**
     * Stops the server.
     */
    public static void stopServer() {
        if (!isRunning) {
            System.out.println("Server is not running.");
            return;
        }

        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close(); // Close the server socket
            }
        } catch (IOException e) {
            System.out.println("Error closing server socket: " + e.getMessage());
        }
        if (threadPool != null) {
            threadPool.shutdownNow(); // Forcefully terminate all tasks
        }
        System.out.println("Server has stopped.");
    }

    /**
     * Handles console input for server commands (e.g., "stop").
     */
    private static void handleConsoleInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (isRunning) {
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("stop")) {
                    stopServer();
                    break;
                }
            }
        } finally {
            scanner.close(); // Ensure the scanner is always closed
        }
    }
}