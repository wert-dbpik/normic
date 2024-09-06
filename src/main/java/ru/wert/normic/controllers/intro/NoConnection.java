package ru.wert.normic.controllers.intro;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.decoration.ModalWindow;
import ru.wert.normic.entities.db_connection.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class NoConnection extends ModalWindow {

    private TextField tfAddressIP;
    private TextField tfPort;
    private Stage stage;

    public NoConnection(Stage stage) {
        this.stage = stage;
    }

    public boolean create() {
        AtomicBoolean answer = new AtomicBoolean(false);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/intro/connection.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
            stage.setResizable(false);

            tfAddressIP = ((TextField) parent.lookup("#tfIP"));
            tfAddressIP.setText(RetrofitClient.params.getIp());
            Platform.runLater(() -> tfAddressIP.requestFocus());

            tfPort = ((TextField) parent.lookup("#tfPort"));
            tfPort.setText(RetrofitClient.params.getPort());

            Button btnOK = ((Button) parent.lookup("#btnOK"));
            btnOK.setOnAction((e) -> {
                String newIp = tfAddressIP.getText().trim();
                String newPort = tfPort.getText().trim();
                answer.set(true);
                RetrofitClient.getInstance();
                RetrofitClient.restartWithNewUrl(new ConnectionParams(newIp, newPort));
                ((Node) e.getSource()).getScene().getWindow().hide();
            });

            new Decoration(
                    "Внимание!",
                    parent,
                    false,
                    MAIN_STAGE,
                    "decoration-login",
                    false,
                    true);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer.get();
    }

    public void close(){
        stage.hide();
    }
}
