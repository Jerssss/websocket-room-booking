package client.view.clientview;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class MenuPageView {

    @FXML
    private Button menuEquipmentButton;
    @FXML
    private Button menuTerminalsButton;
    @FXML
    private Button menuHistoryButton;
    @FXML
    private Button menuCheckoutButton;
    @FXML
    private Button menuClearCartButton;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;



















    public void setDateLabel(String value) {
        dateLabel.setText(value);
    }

    public void setTimeLabel(String value) {
        timeLabel.setText(value);
    }

    //effects only
    public void EquipmentButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuEquipmentButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void EquipmentButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuEquipmentButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void TerminalsButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuTerminalsButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void TerminalsButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuTerminalsButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void HistoryButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuHistoryButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void HistoryButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuHistoryButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void ClearCartButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuClearCartButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void ClearCartButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuClearCartButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void CheckoutButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuCheckoutButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void CheckoutButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), menuCheckoutButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }



}
