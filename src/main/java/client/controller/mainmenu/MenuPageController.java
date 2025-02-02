package client.controller.mainmenu;

import client.view.clientview.MenuPageView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;


public class MenuPageController {

    private final MenuPageView menuPageView;
    private Timer timer;
    public MenuPageController(MenuPageView menuPageView){
        this.menuPageView = menuPageView;

        setupClock();
        setupDate();

//        TODO: retrieve user name from server

        timer= new Timer();

    }
    public void setupClock() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateClock()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    } // end of setDateAndTime

    public void updateClock() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = currentTime.format(formatter);
        menuPageView.setTimeLabel(formattedTime);
    } // end of updateClock

    private void setupDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = currentDate.format(formatter);
        menuPageView.setDateLabel(formattedDate);
    } // end of setupDate



}
