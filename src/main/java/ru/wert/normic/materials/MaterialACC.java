package ru.wert.normic.materials;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EMatOperations;
import ru.wert.normic.enums.EMatType;

import java.io.IOException;

public class MaterialACC {

    public MaterialACC(EMatOperations operation, TableView<Material> tableView, TableRow<Material> row) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialACC.fxml"));
            Parent parent = loader.load();
            final StackPane sp = (StackPane) parent.lookup("#spForCalculation");

            if(operation.equals(EMatOperations.COPY) || operation.equals(EMatOperations.CHANGE)) {
                String matTypePath = EMatType.getPathByName(row.getItem().getMatType().getName());
                FXMLLoader typeLoader = new FXMLLoader(getClass().getResource(matTypePath));
                Parent typeParent = typeLoader.load();
                sp.getChildren().add(typeParent);
            }

            Decoration decoration = new Decoration(
                    "МАТЕРИАЛЫ",
                    parent,
                    false,
                    (Stage)tableView.getScene().getWindow(),
                    "decoration-settings",
                    false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
