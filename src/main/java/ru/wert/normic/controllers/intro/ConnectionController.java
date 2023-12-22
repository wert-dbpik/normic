package ru.wert.normic.controllers.intro;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Getter;
import ru.wert.normic.controllers.list.PlateCuttingController;
import ru.wert.normic.decoration.Decoration;

import static javafx.application.Platform.exit;

public class ConnectionController {


    @Getter@FXML
    private Button btnOK;

    @FXML
    private TextField tfPort;

    @FXML
    private TextField tfIP;

    private String ip;
    private String port;

    @Getter
    private ConnectionParams finalParams = null;

    public void init(ConnectionParams params){
        this.ip = params.getIp();
        this.port = params.getPort();

        tfIP.setText(params.getIp());
        tfPort.setText(params.getPort());

        btnOK.setOnAction(e->{
            ip = tfIP.getText().isEmpty() ? ip : tfIP.getText().trim();
            port = tfPort.getText().trim();
            finalParams = new ConnectionParams(ip, port);
            ((Node)e.getSource()).getScene().getWindow().hide();
        });

    }

}

