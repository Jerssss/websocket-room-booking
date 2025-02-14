package client.admin.model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import server.utility.StudentReservation;
import server.admin.ViewStudentReservationsProcessor;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ViewStudentReservationsModel {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4321;
    private List<StudentReservation> studentReservations = new ArrayList<>();

    public ViewStudentReservationsModel() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            showErrorDialog("Server is down or unreachable. Please try again later.");
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}