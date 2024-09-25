package ru.wert.normic.components;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.db_connection.material.Material;

import java.io.IOException;
import java.util.Comparator;

import static ru.wert.normic.AppStatics.NO_MATERIAL;
import static ru.wert.normic.NormicServices.QUICK_MATERIALS;

public class BtnAddMaterial{

    public BtnAddMaterial(Button btnAddMaterial, ComboBox<Material> cmbxMaterial) {
        btnAddMaterial.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/materials.png")), 18,18, true, true)));
        btnAddMaterial.setTooltip(new Tooltip("Добавить материал"));
        btnAddMaterial.setOnAction(e->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialsTV.fxml"));
                Parent parent = loader.load();
                Decoration decoration = new Decoration(
                        "МАТЕРИАЛЫ",
                        parent,
                        false,
                        (Stage) ((Node)e.getSource()).getScene().getWindow(),
                        "decoration-settings",
                        false,
                        false);

                decoration.getWindow().setOnHiding(r->{
                    Material chosenMaterial = cmbxMaterial.getValue();
                    ObservableList<Material> materials = FXCollections.observableArrayList(QUICK_MATERIALS.findAll());
                    materials.sort(Comparator.comparing(Material::getName));
                    cmbxMaterial.getItems().clear();
                    cmbxMaterial.getItems().addAll(materials);
                    if(chosenMaterial.equals(NO_MATERIAL))
                        materials.add(0, NO_MATERIAL);

                    if(materials.contains(chosenMaterial))
                        cmbxMaterial.getSelectionModel().select(chosenMaterial);
                    else
                        cmbxMaterial.getSelectionModel().select(0);

                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
