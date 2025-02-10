package client.student.controller;

import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;

public class StudentMainMenuController {

    private final StudentMainMenuView StudentMainMenuView;
    private final StudentMainMenuModel StudentMainMenuModel;

    public StudentMainMenuController(client.student.view.StudentMainMenuView studentMainMenuView, client.student.model.StudentMainMenuModel studentMainMenuModel) {
        StudentMainMenuView = studentMainMenuView;
        StudentMainMenuModel = studentMainMenuModel;
    }
}
