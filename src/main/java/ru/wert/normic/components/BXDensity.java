package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.entities.db_connection.density.Density;


import static ru.wert.normic.ChogoriServices.CH_DENSITIES;


public class BXDensity {

    private static Density LAST_VAL;
    private ComboBox<Density> cmbx;

    public void create(ComboBox<Density> bxDensity){
        this.cmbx = bxDensity;
        ObservableList<Density> materials = FXCollections.observableArrayList(CH_DENSITIES.findAll());
        bxDensity.setItems(materials);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = CH_DENSITIES.findByName("Сталь");

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<Density>() {
            @Override
            protected void updateItem (Density item,boolean empty){
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
        cmbx.setConverter(new StringConverter<Density>() {
            @Override
            public String toString(Density density) {
                LAST_VAL = density; //последний выбранный префикс становится префиксом по умолчанию
                return density.getName();
            }

            @Override
            public Density fromString(String string) {
                return null;
            }
        });
    }
}
