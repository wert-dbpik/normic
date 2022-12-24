package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EPartBigness;


public class BXPartBigness {

    public static EPartBigness LAST_VAL;
    private ComboBox<EPartBigness> cmbx;

    public void create(ComboBox<EPartBigness> cmbx, EPartBigness initVal, AbstractOpPlate counter){
        this.cmbx = cmbx;
        ObservableList<EPartBigness> values = FXCollections.observableArrayList(EPartBigness.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        if(LAST_VAL == null)
            LAST_VAL = EPartBigness.SMALL;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EPartBigness>() {
            @Override
            protected void updateItem (EPartBigness item, boolean empty){
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
        cmbx.setConverter(new StringConverter<EPartBigness>() {
            @Override
            public String toString(EPartBigness val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getName();
            }

            @Override
            public EPartBigness fromString(String string) {
                return null;
            }
        });
    }
}
