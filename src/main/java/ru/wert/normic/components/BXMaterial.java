package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.entities.db_connection.material.Material;

import java.util.Comparator;

import static ru.wert.normic.NormicServices.QUICK_MATERIALS;


public class BXMaterial {

    private static Material LAST_VAL;
    private ComboBox<Material> cmbx;

    public void create(ComboBox<Material> bxMaterial){
        this.cmbx = bxMaterial;
        ObservableList<Material> materials = FXCollections.observableArrayList(QUICK_MATERIALS.findAll());
        materials.sort(Comparator.comparing(Material::getName));
        bxMaterial.setItems(materials);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = QUICK_MATERIALS.findByName("лист 1");

        bxMaterial.getSelectionModel().select(LAST_VAL);

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<Material>() {
            @Override
            protected void updateItem (Material item,boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<Material>() {
            @Override
            public String toString(Material material) {
                LAST_VAL = material; //последний выбранный префикс становится префиксом по умолчанию
                return material.getName();
            }

            @Override
            public Material fromString(String string) {
                return null;
            }
        });
    }
}
