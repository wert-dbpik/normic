package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.enums.EColor;


public class BXColor {

    public static EColor LAST_TIME_MEASUREMENT;
    private ComboBox<EColor> bxTimeMeasurement;

    public void create(ComboBox<EColor> bxTimeMeasurement){
        this.bxTimeMeasurement = bxTimeMeasurement;
        ObservableList<EColor> timeMeasurements = FXCollections.observableArrayList(EColor.values());
        bxTimeMeasurement.setItems(timeMeasurements);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        if(LAST_TIME_MEASUREMENT == null)
            LAST_TIME_MEASUREMENT = EColor.COLOR_I;
        bxTimeMeasurement.setValue(LAST_TIME_MEASUREMENT);

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        bxTimeMeasurement.setCellFactory(i -> new ListCell<EColor>() {
            @Override
            protected void updateItem (EColor item,boolean empty){
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
        bxTimeMeasurement.setConverter(new StringConverter<EColor>() {
            @Override
            public String toString(EColor timeMeasurement) {
                LAST_TIME_MEASUREMENT = timeMeasurement; //последний выбранный префикс становится префиксом по умолчанию
                return timeMeasurement.getName();
            }

            @Override
            public EColor fromString(String string) {
                return null;
            }
        });
    }
}
