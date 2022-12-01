package ru.wert.normik.decoration.warnings;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.wert.normik.decoration.DecorationStatic;
import ru.wert.normik.decoration.ModalWindow;


import java.io.IOException;

import static ru.wert.normik.decoration.DecorationStatic.WF_MAIN_STAGE;


public class Warning2 extends ModalWindow {

    private static final BooleanProperty agreevation = new SimpleBooleanProperty();

    public static boolean create(String title, String problem, String decision){

        try {
            Stage stage = new Stage();
            FXMLLoader userDialogLoader = new FXMLLoader(Warning2.class.getResource("/fxml/warnings/warning2.fxml"));
            Parent parent = userDialogLoader.load();
            parent.getStylesheets().add(Warning2.class.getResource("/css/pik-dark.css").toString());
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            Button btnOK = (Button)parent.lookup("#btnYes");
            btnOK.setOnAction((event -> {
                agreevation.set(true);
                DecorationStatic.closeWindow(event);

            }));

            Button btnCancel = (Button)parent.lookup("#btnCancel");
            btnCancel.setOnAction((event -> {
                agreevation.set(false);
                DecorationStatic.closeWindow(event);

            }));

            Label lblTitle = (Label)parent.lookup("#lblWarningTitle");
            lblTitle.setText(title);
            lblTitle.setStyle("-fx-text-fill: #fcce45");

            Label lblProblem = (Label)parent.lookup("#lblProblem");
            lblProblem.setText(problem);
            lblProblem.setStyle("-fx-text-fill: #fcce45");

            Label lblDecision = (Label)parent.lookup("#lblDecision");
            if(decision != null) lblDecision.setText(decision);
            else ((AnchorPane)parent).getChildren().remove(lblDecision);


            ModalWindow.setMovingPane(parent);

            Platform.runLater(()->{
                ModalWindow.centerWindow(stage, WF_MAIN_STAGE, null);
            });

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return agreevation.get();
    }

}
