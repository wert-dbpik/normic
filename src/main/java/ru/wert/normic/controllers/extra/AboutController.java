package ru.wert.normic.controllers.extra;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.wert.normic.AppStatics;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;

public class AboutController {


    @FXML
    private Label lblVersion;

    @FXML
    private Label lblCurrentUser;

    @FXML
    private Label lblCurrentIpPort;

    @FXML
    private VBox vbAbout;

    @FXML
    void initialize(){

        lblVersion.setText("Версия " + AppStatics.CURRENT_PROJECT_VERSION);
        lblCurrentUser.setText(AppStatics.CURRENT_USER.getName());
        lblCurrentIpPort.setText("http://" +
                AppStatics.CURRENT_CONNECTION_PARAMS.getIp() + ":" +
                AppStatics.CURRENT_CONNECTION_PARAMS.getPort());

        vbAbout.setOnMouseClicked(e->
                MAIN_CONTROLLER.getVbAboutPane().setVisible(false));
    }
}
