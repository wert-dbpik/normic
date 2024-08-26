package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EWeldDifficulty;


public class BXWeldDifficulty {

    public static EWeldDifficulty LAST_VAL;
    private ComboBox<EWeldDifficulty> cmbx;

    public void create(ComboBox<EWeldDifficulty> cmbx, EWeldDifficulty initVal, AbstractOpPlate counter){
        this.cmbx = cmbx;
        ObservableList<EWeldDifficulty> values = FXCollections.observableArrayList(EWeldDifficulty.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        if(LAST_VAL == null)
            LAST_VAL = EWeldDifficulty.MIDDLE;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EWeldDifficulty>() {
            @Override
            protected void updateItem (EWeldDifficulty item, boolean empty){
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
        cmbx.setConverter(new StringConverter<EWeldDifficulty>() {
            @Override
            public String toString(EWeldDifficulty val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getDifficultyName();
            }

            @Override
            public EWeldDifficulty fromString(String string) {
                return null;
            }
        });
    }
}
