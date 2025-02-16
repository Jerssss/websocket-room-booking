package client.admin.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import client.admin.view.ModifyTerminalStatusView;
import client.admin.model.ModifyTerminalStatusModel;
import server.utility.Terminal;
import javax.swing.*;

public class ModifyTerminalStatusController {

    private final ModifyTerminalStatusView view;
    private final ModifyTerminalStatusModel model;

    private ObservableList<Terminal> terminalData = FXCollections.observableArrayList();

    public ModifyTerminalStatusController(ModifyTerminalStatusView view) {
        this.view = view;
        this.model = new ModifyTerminalStatusModel();
    }

    public void loadTerminalData() {
        ObservableList<Terminal> terminalData = model.loadTerminalData();
        view.setTerminalData(terminalData);
    }

    public void saveChanges() {
        model.saveTerminalData(view.getTerminalData());
        JOptionPane.showMessageDialog(null, "Changes have been successfully saved!",
                "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }
}
