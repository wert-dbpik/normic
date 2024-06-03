package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.entities.db_connection.density.Density;
import ru.wert.normic.enums.EAssemblingType;
import ru.wert.normic.enums.EPieceMeasurement;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ru.wert.normic.NormicServices.DENSITIES;


public class BXPieceMeasurement {

    private static EPieceMeasurement LAST_VAL;
    private ComboBox<EPieceMeasurement> cmbx;

    public void create(ComboBox<EPieceMeasurement> cmbx){
        this.cmbx = cmbx;
        ObservableList<EPieceMeasurement> measurements = FXCollections.observableArrayList(EPieceMeasurement.values());

        cmbx.setItems(measurements);

        createCellFactory();
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = EPieceMeasurement.PIECE;

        cmbx.getSelectionModel().select(LAST_VAL);

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EPieceMeasurement>() {
            @Override
            protected void updateItem (EPieceMeasurement item,boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getMeasureName());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<EPieceMeasurement>() {
            @Override
            public String toString(EPieceMeasurement measurement) {
                LAST_VAL = measurement; //последний выбранный префикс становится префиксом по умолчанию
                return measurement.getMeasureName();
            }

            @Override
            public EPieceMeasurement fromString(String string) {
                return null;
            }
        });
    }

}
