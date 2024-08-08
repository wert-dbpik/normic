package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.dataBaseEntities.db_connection.material_group.MaterialGroup;


import static ru.wert.normic.NormicServices.MATERIAL_GROUPS;


public class BXMaterialGroups {

    private static MaterialGroup LAST_VAL;
    private ComboBox<MaterialGroup> cmbx;

    public void create(ComboBox<MaterialGroup> bxMaterialGroup){
        this.cmbx = bxMaterialGroup;
        ObservableList<MaterialGroup> materials = FXCollections.observableArrayList(MATERIAL_GROUPS.findAll());
        bxMaterialGroup.setItems(materials);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = MATERIAL_GROUPS.findByName("Разное");

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
