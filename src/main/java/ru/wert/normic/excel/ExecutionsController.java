package ru.wert.normic.excel;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import lombok.Getter;
import ru.wert.normic.excel.model.EditorRow;

public class ExecutionsController {

    @FXML
    private Button btnCancel;

    @FXML
    private ComboBox<EditorRow.Execution> cmbxExecutions;

    @FXML @Getter
    private Button btnYes;

    @Getter private Integer execution = null;


    public void init(ObservableList<EditorRow.Execution> executions){

        cmbxExecutions.setItems(executions);

        btnYes.setOnAction(e->{
            execution = cmbxExecutions.getSelectionModel().getSelectedIndex();
            closeWindow(e);
        });

        btnCancel.setOnAction(e->{
            execution = null;
            closeWindow(e);
        });

    }

    private void closeWindow(Event e){
        ((Node)e.getSource()).getScene().getWindow().hide();
    }
}
