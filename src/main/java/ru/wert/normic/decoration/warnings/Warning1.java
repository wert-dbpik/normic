package ru.wert.normic.decoration.warnings;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.wert.normic.decoration.ModalWindow;

import java.io.IOException;

import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;


public class Warning1 extends ModalWindow {

    public static void create(Event event, String title, String problem, String decision){


        try {
            Stage thisStage = new Stage();
            FXMLLoader userDialogLoader = new FXMLLoader(Warning1.class.getResource("/fxml/warnings/warning1.fxml"));
            Parent parent = userDialogLoader.load();
            thisStage.setScene(new Scene(parent));
            thisStage.initModality(Modality.APPLICATION_MODAL);
            thisStage.setResizable(false);
            thisStage.initStyle(StageStyle.UNDECORATED);

            Button btnOK = (Button)parent.lookup("#btnOK");
            btnOK.setOnAction((e -> {
                ((Node)e.getSource()).getScene().getWindow().hide();

            }));

            Label lblTitle = (Label)parent.lookup("#lblTitle");
            lblTitle.setText(title);
            lblTitle.setStyle("-fx-text-fill: #5d2308");

            Label lblProblem = (Label)parent.lookup("#lblProblem");
            lblProblem.setText(problem);
            lblProblem.setStyle("-fx-text-fill: #d0561d");

            Label lblDecision = (Label)parent.lookup("#lblDecision");
//            int index = parent.getChildrenUnmodifiable().indexOf(lblDecision);
            if(decision == null || decision.equals(""))
                ((AnchorPane)parent).getChildren().remove(lblDecision);
            else
                lblDecision.setText(decision);

            ModalWindow.setMovingPane(parent);

            Platform.runLater(()->{
                ModalWindow.centerModalWindowRelativeToOwner(thisStage, event);
            });
            thisStage.isAlwaysOnTop();
            thisStage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

}
