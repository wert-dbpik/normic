package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.material_group.MaterialGroup;

import java.util.Arrays;
import java.util.Collections;

import static ru.wert.normic.NormicServices.MATERIAL_GROUPS;


public class BXMaterialGroupsWithAll {

    private static MaterialGroup LAST_VAL;
    private ComboBox<MaterialGroup> cmbx;

    public void create(ComboBox<MaterialGroup> bxMaterialGroup){
        this.cmbx = bxMaterialGroup;

        MaterialGroup all = new MaterialGroup(0L, "Все");
        ObservableList<MaterialGroup> materials = FXCollections.observableArrayList(Collections.singletonList(all));
        materials.addAll(FXCollections.observableArrayList(MATERIAL_GROUPS.findAll()));
        bxMaterialGroup.setItems(materials);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = all;

        bxMaterialGroup.setValue(LAST_VAL);

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<MaterialGroup>() {
            @Override
            protected void updateItem (MaterialGroup item,boolean empty){
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
        cmbx.setConverter(new StringConverter<MaterialGroup>() {
            @Override
            public String toString(MaterialGroup material) {
                LAST_VAL = material; //последний выбранный префикс становится префиксом по умолчанию
                return material.getName();
            }

            @Override
            public MaterialGroup fromString(String string) {
                return null;
            }
        });
    }
}
