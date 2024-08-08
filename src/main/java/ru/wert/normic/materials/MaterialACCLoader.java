package ru.wert.normic.materials;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.dataBaseEntities.db_connection.material.Material;
import ru.wert.normic.enums.ECommands;
import ru.wert.normic.enums.EMatType;

import java.io.IOException;

public class MaterialACCLoader {

    @Getter private MaterialsACCController mainController;
    @Getter private MatTypeController matTypeController;

    public MaterialACCLoader(ECommands operation, TableView<Material> tableView, TableRow<Material> row) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialACC.fxml"));
            Parent parent = loader.load();
            mainController = loader.getController();
            final StackPane sp = (StackPane) parent.lookup("#spForCalculation");

            if(operation.equals(ECommands.COPY) || operation.equals(ECommands.CHANGE)) {
                String matTypePath = EMatType.getPathByName(row.getItem().getMatType().getName());
                FXMLLoader typeLoader = new FXMLLoader(getClass().getResource(matTypePath));
                Parent typeParent = typeLoader.load();
                matTypeController = typeLoader.getController();
                sp.getChildren().add(typeParent);
            }

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
