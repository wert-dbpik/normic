package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EMaterialMeasurement;
import ru.wert.normic.enums.ESealersWidth;


public class BXMaterialMeasurement {

    public static EMaterialMeasurement LAST_VAL;
    private ComboBox<EMaterialMeasurement> cmbx;

    public void create(ComboBox<EMaterialMeasurement> cmbx, EMaterialMeasurement initVal, AbstractOpPlate counter ){
        this.cmbx = cmbx;
        ObservableList<EMaterialMeasurement> values = FXCollections.observableArrayList(EMaterialMeasurement.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        if(LAST_VAL == null)
            LAST_VAL = EMaterialMeasurement.M2;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EMaterialMeasurement>() {
            @Override
            protected void updateItem (EMaterialMeasurement item, boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getMeasure());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<EMaterialMeasurement>() {
            @Override
            public String toString(EMaterialMeasurement val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getMeasure();
            }

            @Override
            public EMaterialMeasurement fromString(String string) {
                return null;
            }
        });
    }
}
