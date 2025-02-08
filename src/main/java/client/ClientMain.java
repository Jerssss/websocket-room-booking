package client;

import java.io.*;
import java.net.Socket;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientMain extends Application{
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             PrintWriter writer = new PrintWriter(output, true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server.");

            // Read server welcome message
            System.out.println(reader.readLine());

            String userInput;
            while ((userInput = consoleReader.readLine()) != null) {
                writer.println(userInput);
                String serverResponse = reader.readLine();
                System.out.println(serverResponse);

                if ("Goodbye!".equalsIgnoreCase(serverResponse)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
        }

        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {

        stage.getIcons().add(new Image(getClass().getResource("/images/client/app_icon.png").toExternalForm()));
        ClientView view = new ClientView(stage);
        view.runInterface();

        new ClientController(view);
    }
}
