package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.ESealersWidth;


public class BXSealersWidth {

    public static ESealersWidth LAST_VAL;
    private ComboBox<ESealersWidth> cmbx;

    public void create(ComboBox<ESealersWidth> cmbx, ESealersWidth initVal, AbstractOpPlate counter ){
        this.cmbx = cmbx;
        ObservableList<ESealersWidth> values = FXCollections.observableArrayList(ESealersWidth.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        if(LAST_VAL == null)
            LAST_VAL = ESealersWidth.W10;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<ESealersWidth>() {
            @Override
            protected void updateItem (ESealersWidth item, boolean empty){
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
        cmbx.setConverter(new StringConverter<ESealersWidth>() {
            @Override
            public String toString(ESealersWidth val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getName();
            }

            @Override
            public ESealersWidth fromString(String string) {
                return null;
            }
        });
    }
}
