package client.student.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.admin.ModifyTerminalProcessor;
import server.student.CreateReservationProcessor;
import server.student.ViewReservationProcessor;
import server.utility.Terminal;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateReservationModel {
    private ServerConnection serverConnection;


    public CreateReservationModel() {
        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Terminal> loadTerminalData() {
        if (serverConnection == null) {
            System.err.println("Server connection is not established!");
            return FXCollections.observableArrayList();
        }

        try {
            List<Terminal> terminals = CreateReservationProcessor.parseXML();
            if (terminals == null) {
                System.out.println("No terminal data received from server!");
                return FXCollections.observableArrayList();
            }
            return FXCollections.observableArrayList(terminals);
        } catch (Exception e) {
            System.err.println("Error loading terminal data: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }


}
