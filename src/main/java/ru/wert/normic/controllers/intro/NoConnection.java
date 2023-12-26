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
import ru.wert.normic.entities.db_connection.retrofit.AppProperties;
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
            tfAddressIP.setText(AppProperties.getInstance().getIpAddress());
            Platform.runLater(() -> tfAddressIP.requestFocus());

            tfPort = ((TextField) parent.lookup("#tfPort"));
            tfPort.setText(AppProperties.getInstance().getPort());

            Button btnOK = ((Button) parent.lookup("#btnOK"));
            btnOK.setOnAction((e) -> {
                AppProperties.getInstance().setIpAddress(tfAddressIP.getText().trim());
                AppProperties.getInstance().setPort(tfPort.getText().trim());
                answer.set(true);
                RetrofitClient.getInstance();
                RetrofitClient.restartWithNewUrl();
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
}
