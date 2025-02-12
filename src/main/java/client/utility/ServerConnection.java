package client.utility;


import javax.swing.JOptionPane;
import javafx.application.Platform;
import java.io.*;
import java.net.Socket;

public class ServerConnection {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ServerConnection() throws IOException {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read and print welcome message
            System.out.println(reader.readLine());
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
            throw e; // Throw exception to be handled by caller
        }
    }

    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public String readMessage() throws IOException {
        return (reader != null) ? reader.readLine() : null;
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}

