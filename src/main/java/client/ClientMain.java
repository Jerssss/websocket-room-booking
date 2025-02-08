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

        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        requestConnection();

        stage.getIcons().add(new Image(getClass().getResource("/images/client/app_icon.png").toExternalForm()));
        ClientView view = new ClientView(stage);
        view.runInterface();

        new ClientController(view);
    }

    //I moved the previous main method contents into a new method to be able to include the logic into the start method
    //Originally, it could not connect to the server although it could run the GUI.
    //NOT SURE IF THE GUI DOES COMMUNICATE TO THE SERVER.
    public void requestConnection () {
        Thread thread = new Thread(() -> {
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
        });
        thread.setDaemon(true);
        thread.start();
    }
}
