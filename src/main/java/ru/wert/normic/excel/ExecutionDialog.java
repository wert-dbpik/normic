package ru.wert.normic.excel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.excel.model.EditorRow;

import java.io.IOException;
import java.util.stream.IntStream;

import static ru.wert.normic.decoration.DecorationStatic.MAIN_STAGE;

public class ExecutionDialog {

    private static Integer execution;


    public static Integer create(ObservableList<EditorRow.Execution> executions){

        execution = null;
        try {
            FXMLLoader loader = new FXMLLoader(ExecutionDialog.class.getResource("/fxml/extra/executions.fxml"));
            Parent parent = loader.load();

            ComboBox<String> cmbxExecutions = (ComboBox<String>)parent.lookup("#cmbxExecutions");
            int[] range = IntStream.rangeClosed(0, executions.size() - 1).toArray();
            ObservableList<String> items =  FXCollections.observableArrayList();
            for(int i : range) items.add("исп. " + String.format("%02d",i));


            cmbxExecutions.getItems().addAll(items);
            cmbxExecutions.getSelectionModel().select(0);

            Button btnYes = (Button)parent.lookup("#btnYes");
            btnYes.setOnAction((event -> {
                execution = cmbxExecutions.getSelectionModel().getSelectedIndex();
                ((Node)event.getSource()).getScene().getWindow().hide();
            }));

            Button btnCancel = (Button)parent.lookup("#btnCancel");
            btnCancel.setOnAction((event -> {
                execution = null;
                ((Node)event.getSource()).getScene().getWindow().hide();
            }));

//            Platform.runLater(()->{
                new Decoration("Исполнение",
                        parent,
                        false,
                        MAIN_STAGE,
                        "decoration-settings",
                        false,
                        true);
//            });


        } catch (IOException e) {
            e.printStackTrace();
        }
        return execution;
    }
}
