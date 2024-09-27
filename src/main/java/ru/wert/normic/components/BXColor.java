package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EColor;


public class BXColor {

    public static EColor LAST_VAL;
    private ComboBox<EColor> cmbx;

    public void create(ComboBox<EColor> cmbx, EColor initVal, AbstractOpPlate counter){
        this.cmbx = cmbx;
        ObservableList<EColor> values = FXCollections.observableArrayList(EColor.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        if(LAST_VAL == null)
            LAST_VAL = EColor.COLOR_I;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EColor>() {
            @Override
            protected void updateItem (EColor item,boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getRal());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<EColor>() {
            @Override
            public String toString(EColor val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getRal();
            }

            @Override
            public EColor fromString(String string) {
                return null;
            }
        });
    }
}
