package ru.wert.normic.help;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.wert.normic.decoration.ModalWindow;

import java.io.IOException;

public class HelpWindow extends ModalWindow{

    public static void create(Event event, String title, String text, Image image){

        try {
            Stage owner = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(HelpWindow.class.getResource("/fxml/help/helpWindow.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            Button btnOK = (Button)parent.lookup("#btnOK");
            btnOK.setOnAction((ev -> {
                ((Node)ev.getSource()).getScene().getWindow().hide();
            }));

            Label lblTitle = (Label)parent.lookup("#lblTitle");
            lblTitle.setText(title);

            //Находим сначала ScrollPane
            ScrollPane sp  = (ScrollPane)parent.lookup("#scrollPane");

            //ИЗОБРАЖЕНИЕ
            if(image != null){
                ImageView ivImage = (ImageView)sp.getContent().lookup("#ivImage");
                ivImage.setPreserveRatio(true);
                ivImage.setFitWidth(480);
                ivImage.setImage(image);
            }

            //ТЕКСТ
            Text taText = (Text)sp.getContent().lookup("#text");
            taText.setText(text);

            ModalWindow.setMovingPane(parent);

            Platform.runLater(()->{
                ModalWindow.centerWindow(stage, owner, null);
            });
            stage.isAlwaysOnTop();
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
