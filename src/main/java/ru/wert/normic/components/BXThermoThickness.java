package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.enums.EMaterialMeasurement;

import java.util.Arrays;


public class BXThermoThickness {

    public static Integer LAST_VAL;
    private ComboBox<Integer> cmbx;

    public void create(ComboBox<Integer> cmbx,  Integer initVal, AbstractOpPlate counter){
        this.cmbx = cmbx;
        ObservableList<Integer> values = FXCollections.observableArrayList(Arrays.asList(10, 20, 30, 50, 100));
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        if(LAST_VAL == null)
            LAST_VAL = 50;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<Integer>() {
            @Override
            protected void updateItem (Integer item,boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return String.valueOf(val);
            }

            @Override
            public Integer fromString(String string) {
                return null;
            }
        });
    }
}
