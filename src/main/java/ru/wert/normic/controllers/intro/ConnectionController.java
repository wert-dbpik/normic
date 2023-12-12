package ru.wert.normic.controllers.intro;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.wert.normic.decoration.Decoration;

import static javafx.application.Platform.exit;

public class ConnectionController {


    @FXML
    private Button btnOK;

    @FXML
    private TextField tfPort;

    @FXML
    private TextField tfIP;

    private String ip;
    private String port;

    public ConnectionData init(ConnectionData data, Decoration decoration){
        this.ip = data.getIp();
        this.port = data.getPort();

        tfIP.setText(data.getIp());
        tfPort.setText(data.getPort());

        decoration.getWindow().setOnCloseRequest(e->{
            exit();
        });

        btnOK.setOnAction(e->{
            ip = tfIP.getText().isEmpty() ? ip : tfIP.getText().trim();
            port = tfPort.getText().trim();
        });

        return new ConnectionData(ip, port);
    }

}

