package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.enums.ENormType;


public class BXNormType {

    private static ENormType LAST_VAL;
    private ComboBox<ENormType> cmbx;

    public void create(ComboBox<ENormType> cmbx){
        this.cmbx = cmbx;
        ObservableList<ENormType> measurements = FXCollections.observableArrayList(ENormType.values());

        cmbx.setItems(measurements);

        createCellFactory();
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = ENormType.NORM_ASSEMBLE;

        cmbx.getSelectionModel().select(LAST_VAL);

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
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
                LAST_VAL = normType; //последний выбранный префикс становится префиксом по умолчанию
                return normType.getNormName();
            }

            @Override
            public ENormType fromString(String string) {
                return null;
            }
        });
    }

}
