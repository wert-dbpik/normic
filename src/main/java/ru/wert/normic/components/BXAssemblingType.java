package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EAssemblingType;


public class BXAssemblingType {

    public static EAssemblingType LAST_VAL;
    private ComboBox<EAssemblingType> bx;

    public void create(ComboBox<EAssemblingType> cmbx, EAssemblingType initVal, AbstractOpPlate counter){
        this.bx = cmbx;
        ObservableList<EAssemblingType> values = FXCollections.observableArrayList(EAssemblingType.values());
        cmbx.setItems(values);

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = EAssemblingType.SOLID;

        cmbx.setValue(LAST_VAL);

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        bx.setCellFactory(i -> new ListCell<EAssemblingType>() {
            @Override
            protected void updateItem (EAssemblingType item, boolean empty){
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
        bx.setConverter(new StringConverter<EAssemblingType>() {
            @Override
            public String toString(EAssemblingType val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getName();
            }

            @Override
            public EAssemblingType fromString(String string) {
                return null;
            }
        });
    }
}
