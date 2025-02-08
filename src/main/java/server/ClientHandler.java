package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             PrintWriter writer = new PrintWriter(output, true)) {

            writer.println("Welcome to the Server! Type 'exit' to disconnect.");

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received: " + clientMessage);

                if ("exit".equalsIgnoreCase(clientMessage)) {
                    writer.println("Goodbye!");
                    break;
                }

                writer.println("Server Echo: " + clientMessage);
            }

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}