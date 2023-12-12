package ru.wert.normic.controllers.intro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConnectionController {


    @FXML
    private Button btnOK;

    @FXML
    private TextField tfPort;

    @FXML
    private TextField tfIP;

    @FXML
    void initialize(){}

    public void init(ConnectionData data){

        tfIP.setText(data.getIp());
        tfPort.setText(data.getPort());

        btnOK.setOnAction(e->{

        });
    }


}

