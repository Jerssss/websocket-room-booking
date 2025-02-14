package client.student.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DuplicateAccountErrorView {

    public static void showDupeAccErrorUI() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(DuplicateAccountErrorView.class.getResource("/fxml/client/duplicate_acc_error_window.fxml")));
            Scene duplicateAccountErrorScene = new Scene(root);
            Stage popupStage = new Stage();
            popupStage.setScene(duplicateAccountErrorScene);
            popupStage.centerOnScreen();
            popupStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
