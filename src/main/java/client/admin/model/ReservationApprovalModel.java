// File: client/admin/model/ReservationApprovalModel.java
package client.admin.model;

import client.utility.ServerConnection;
import client.utility.ServerConnectionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.admin.ReservationApprovalProcessor;
import server.utility.ApprovalReservation;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ReservationApprovalModel {

    private ServerConnection serverConnection;

    public ReservationApprovalModel() {

        try {
            serverConnection = ServerConnectionManager.getConnection();
        } catch (IOException e) {
            showErrorDialog("Server connection failed.");
        }
    }

    public ObservableList<ApprovalReservation> loadReservationData() {
        if (serverConnection != null) {
            List<ApprovalReservation> reservations = ReservationApprovalProcessor.parseXML();
            if (reservations == null) {
                System.out.println("No reservation data received from server!");
                return FXCollections.observableArrayList();
            }
            return FXCollections.observableArrayList(reservations);
        }
        return FXCollections.observableArrayList();
    }

    public void saveReservationData(ObservableList<ApprovalReservation> reservations) {
        if (serverConnection != null) {
            ReservationApprovalProcessor.saveToXML(reservations);
        }
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(
                null, message, "Server Error", JOptionPane.ERROR_MESSAGE)
        );
    }
}