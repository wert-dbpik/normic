package ru.wert.normic.decoration.warnings;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.wert.normic.decoration.ModalWindow;

import java.io.IOException;

import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

/**
 * Простое сообщение без решения проблемы
 */
public class Warning0 extends ModalWindow {

    public static void create(String title, String problem){


        try {
            Stage stage = new Stage();
            FXMLLoader userDialogLoader = new FXMLLoader(Warning0.class.getResource("/fxml/warnings/warning0.fxml"));
            Parent parent = userDialogLoader.load();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            Button btnOK = (Button)parent.lookup("#btnOK");
            btnOK.setOnAction((event -> {
                ((Node)event.getSource()).getScene().getWindow().hide();

            }));

            Label lblTitle = (Label)parent.lookup("#lblTitle");
            lblTitle.setText(title);

            Label lblProblem = (Label)parent.lookup("#lblProblem");
            lblProblem.setText(problem);

            ModalWindow.setMovingPane(parent);

            Platform.runLater(()->{
                ModalWindow.centerWindow(stage, MAIN_STAGE, null);
            });
            stage.isAlwaysOnTop();
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
