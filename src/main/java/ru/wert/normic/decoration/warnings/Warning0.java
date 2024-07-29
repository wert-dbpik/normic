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
            Stage thisStage = new Stage();
            FXMLLoader userDialogLoader = new FXMLLoader(Warning0.class.getResource("/fxml/warnings/warning0.fxml"));
            Parent parent = userDialogLoader.load();
            thisStage.setScene(new Scene(parent));
            thisStage.initModality(Modality.APPLICATION_MODAL);
            thisStage.setResizable(false);
            thisStage.initStyle(StageStyle.UNDECORATED);

            Button btnOK = (Button)parent.lookup("#btnOK");
            btnOK.setOnAction((event -> {
                ((Node)event.getSource()).getScene().getWindow().hide();

            }));

            Label lblTitle = (Label)parent.lookup("#lblTitle");
            lblTitle.setText(title);
            lblTitle.setStyle("-fx-text-fill: #5d2308");

            Label lblProblem = (Label)parent.lookup("#lblProblem");
            lblProblem.setText(problem);
            lblProblem.setStyle("-fx-text-fill: #d0561d");

            ModalWindow.setMovingPane(parent);

            Platform.runLater(()->{
                ModalWindow.centerModalWindowRelativeToOwner(thisStage, null);
            });
            thisStage.isAlwaysOnTop();
            thisStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
