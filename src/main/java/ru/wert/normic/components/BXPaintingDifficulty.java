package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EPaintingDifficulty;


public class BXPaintingDifficulty {

    public static EPaintingDifficulty LAST_VAL;
    private ComboBox<EPaintingDifficulty> cmbx;

    public void create(ComboBox<EPaintingDifficulty> cmbx, EPaintingDifficulty initVal, AbstractOpPlate counter){
        this.cmbx = cmbx;
        ObservableList<EPaintingDifficulty> values = FXCollections.observableArrayList(EPaintingDifficulty.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        if(LAST_VAL == null)
            LAST_VAL = EPaintingDifficulty.MIDDLE;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EPaintingDifficulty>() {
            @Override
            protected void updateItem (EPaintingDifficulty item, boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getDifficultyName());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<EPaintingDifficulty>() {
            @Override
            public String toString(EPaintingDifficulty val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getDifficultyName();
            }

            @Override
            public EPaintingDifficulty fromString(String string) {
                return null;
            }
        });
    }
}
