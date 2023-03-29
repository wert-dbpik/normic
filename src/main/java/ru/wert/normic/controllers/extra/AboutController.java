package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.wert.normic.AppStatics;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;

public class AboutController {


    @FXML
    private Label lblVersion;

    @FXML
    private VBox vbAbout;

    @FXML
    void initialize(){

        lblVersion.setText("Версия " + AppStatics.PROJECT_VERSION);

        vbAbout.setOnMouseClicked(e->
                MAIN_CONTROLLER.getVbAboutPane().setVisible(false));
    }
}
