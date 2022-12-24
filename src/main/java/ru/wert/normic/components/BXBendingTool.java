package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EBendingTool;


public class BXBendingTool {
    //татическое поле сохраняет последний выбранный инструмент
    public static EBendingTool LAST_VAL;
    private ComboBox<EBendingTool> cmbx;

    public void create(ComboBox<EBendingTool> cmbx, EBendingTool initVal, AbstractOpPlate counter){
        this.cmbx = cmbx;
        ObservableList<EBendingTool> values = FXCollections.observableArrayList(EBendingTool.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        if(LAST_VAL == null)
            LAST_VAL = EBendingTool.UNIVERSAL;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EBendingTool>() {
            @Override
            protected void updateItem (EBendingTool item, boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getToolName());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<EBendingTool>() {
            @Override
            public String toString(EBendingTool val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getToolName();
            }

            @Override
            public EBendingTool fromString(String string) {
                return null;
            }
        });
    }
}
