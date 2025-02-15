// File: client/admin/controller/ReservationApprovalController.java
package client.admin.controller;

import client.admin.model.ReservationApprovalModel;
import client.admin.view.ReservationApprovalView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.utility.ApprovalTerminal;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationApprovalController {

    private final ReservationApprovalView view;
    private final ReservationApprovalModel model;
    private final ObservableList<ApprovalTerminal> allTerminals = FXCollections.observableArrayList();

    // Constructor
    public ReservationApprovalController(ReservationApprovalView view) {
        this.view = view;
        this.model = new ReservationApprovalModel();

        loadTerminals();           // Load all terminals
        setupSearchFunctionality(); // Set up search functionality

        this.view.setActionRefreshButton(event -> loadTerminals());
    }

    /** Load all terminals from the model */
    private void loadTerminals() {
        List<ApprovalTerminal> terminals = model.fetchApprovalTerminals();
        allTerminals.setAll(terminals);
        view.displayApprovalReservations(allTerminals);
    }

    /** Search terminals by room */
    private void setupSearchFunctionality() {
        view.setActionSearchButton(event -> {
            String searchQuery = view.getSearchStudResTextField().getText().trim();
            filterTerminals(searchQuery);
        });
    }

    /** Filter terminals by search query */
    private void filterTerminals(String searchQuery) {
        if (searchQuery.isEmpty()) {
            view.displayApprovalReservations(allTerminals);
            return;
        }

        List<ApprovalTerminal> filteredList = allTerminals.stream()
                .filter(terminal -> terminal.getTerminalRoom().equalsIgnoreCase(searchQuery))
                .collect(Collectors.toList());

        view.displayApprovalReservations((ObservableList<ApprovalTerminal>) filteredList);
    }
}
