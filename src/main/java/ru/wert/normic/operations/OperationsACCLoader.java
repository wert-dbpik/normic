package ru.wert.normic.operations;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.othersOps.SimpleOperation;
import ru.wert.normic.enums.ECommands;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.materials.MatTypeController;
import ru.wert.normic.materials.MaterialsACCController;

import java.io.IOException;

public class OperationsACCLoader {

    @Getter private OperationsACCController mainController;

    public OperationsACCLoader(ECommands operation, TableView<SimpleOperation> tableView, TableRow<SimpleOperation> row) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/operations/operationsACC.fxml"));
            Parent parent = loader.load();
            mainController = loader.getController();

            Decoration decoration = new Decoration(
                    "МАТЕРИАЛЫ",
                    parent,
                    false,
                    (Stage)tableView.getScene().getWindow(),
                    "decoration-settings",
                    false,
                    false);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
