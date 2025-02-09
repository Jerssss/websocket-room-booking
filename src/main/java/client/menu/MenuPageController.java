package client.menu;

import client.login.LoginController;
import client.login.LoginModel;
import client.student.controller.CreateReservationController;
import client.student.controller.ModifyReservationController;
import client.student.controller.ViewReservationController;
import client.student.model.CreateReservationModel;
import client.student.model.ModifyReservationModel;
import client.student.model.ViewReservationModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MenuPageController {

    private final MenuPageModel menuPageModel;
    private final MenuPageView menuPageView;
    private Stage stage;
    private Parent root;
    private FXMLLoader fxmlLoader;


    public MenuPageController(MenuPageModel menuPageModel, MenuPageView menuPageView) {

        this.menuPageModel = menuPageModel;
        this.menuPageView = menuPageView;

        setupTime();
        setupDate();


        //can move this later onto different methods or another method for modularity????/?
        this.menuPageView.setActionCreateReservationButton((ActionEvent event) -> {
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
                root = fxmlLoader.load(); // saves loaded fxml file's root node into the object root

                new CreateReservationController(fxmlLoader.getController(), new CreateReservationModel()); // allows mutation and display

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // sets the stage or window to display the scene
                Scene scene = new Scene(root); // a new scene or window is created with the root node (which contains all the elements in the fxml file)
                stage.setScene(scene);
                stage.show(); // display
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.menuPageView.setActionViewReservationButton((ActionEvent event) -> {
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
                root = fxmlLoader.load(); // saves loaded fxml file's root node into the object root

                new ViewReservationController(fxmlLoader.getController(), new ViewReservationModel()); // allows mutation and display

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // sets the stage or window to display the scene
                Scene scene = new Scene(root); // a new scene or window is created with the root node (which contains all the elements in the fxml file)
                stage.setScene(scene);
                stage.show(); // display
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.menuPageView.setActionModifyReservationButton((ActionEvent event) -> {
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
                root = fxmlLoader.load(); // saves loaded fxml file's root node into the object root

                new ModifyReservationController(fxmlLoader.getController(), new ModifyReservationModel()); // allows mutation and display

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // sets the stage or window to display the scene
                Scene scene = new Scene(root); // a new scene or window is created with the root node (which contains all the elements in the fxml file)
                stage.setScene(scene);
                stage.show(); // display
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.menuPageView.setActionLogOutButton((ActionEvent event) -> {
            //add logs here
            showLoginPage(event);
        });

    }

    //sets the time up
    public void setupTime(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), event -> updateClock()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void updateClock(){
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = currentTime.format(timeFormatter);
        menuPageView.setTimeLabel(formattedTime);
    }

    public void setupDate(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = currentDate.format(dateFormatter);
        menuPageView.setDateLabel(formattedDate);
    }

    public void showLoginPage (ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
            root = fxmlLoader.load(); // saves loaded fxml file's root node into the object root

            new LoginController(fxmlLoader.getController(), new LoginModel()); // allows mutation and display

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // sets the stage or window to display the scene
            Scene scene = new Scene(root); // a new scene or window is created with the root node (which contains all the elements in the fxml file)
            stage.setScene(scene);
            stage.show(); // display
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
