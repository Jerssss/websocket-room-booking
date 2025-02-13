package client.admin.controller;
import client.admin.model.ModifyTerminalStatusModel;
import client.admin.view.ModifyTerminalStatusView;

public class ModifyTerminalStatusController {
    private final ModifyTerminalStatusView modifyTerminalStatusView;
    private final ModifyTerminalStatusModel modifyTerminalStatusModel;

    public ModifyTerminalStatusController(ModifyTerminalStatusView modifyTerminalStatusView, ModifyTerminalStatusModel modifyTerminalStatusModel) {
        this.modifyTerminalStatusView = modifyTerminalStatusView;
        this.modifyTerminalStatusModel = modifyTerminalStatusModel;
    }


    //TODO to create a button in the column
    //createButton()
    //setEditInformationColumn()
    //handleEditButtonClick()

}
