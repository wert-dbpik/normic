package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.enums.ENormType;


public class BXNormType {

    private ComboBox<ENormType> cmbx;

    public void create(ComboBox<ENormType> cmbx, ENormType normType){
        this.cmbx = cmbx;
        ObservableList<ENormType> values = FXCollections.observableArrayList(ENormType.values());
        values.removeAll(ENormType.NORM_DETAIL, ENormType.NORM_ASSEMBLE);

        cmbx.setItems(values);

        createCellFactory();
        createConverter();

        if(normType != null)
            cmbx.setValue(normType);
        else
            cmbx.setValue(ENormType.NORM_ASSEMBLING);

    }

    private void createCellFactory() {
        cmbx.setCellFactory(i -> new ListCell<ENormType>() {
            @Override
            protected void updateItem (ENormType item,boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNormName());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<ENormType>() {
            @Override
            public String toString(ENormType normType) {
                return normType.getNormName();
            }

            @Override
            public ENormType fromString(String string) {
                return null;
            }
        });
    }

}
