package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.ETimeMeasurement;


public class BXTimeMeasurement {

    public static ETimeMeasurement LAST_VAL;
    private ComboBox<ETimeMeasurement> cmbx;

    public void create(ComboBox<ETimeMeasurement> cmbx, ETimeMeasurement initVal){
        this.cmbx = cmbx;
        ObservableList<ETimeMeasurement> values = FXCollections.observableArrayList(ETimeMeasurement.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = ETimeMeasurement.MIN;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<ETimeMeasurement>() {
            @Override
            protected void updateItem (ETimeMeasurement item,boolean empty){
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
        cmbx.setConverter(new StringConverter<ETimeMeasurement>() {
            @Override
            public String toString(ETimeMeasurement val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getName();
            }

            @Override
            public ETimeMeasurement fromString(String string) {
                return null;
            }
        });
    }
}
