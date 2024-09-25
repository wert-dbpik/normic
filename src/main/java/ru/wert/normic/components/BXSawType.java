package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.ESawType;


public class BXSawType {

    private ComboBox<ESawType> cmbx;

    public void create(ComboBox<ESawType> cmbx, AbstractOpPlate counter, ESawType sawToBeSelected){
        this.cmbx = cmbx;
        ObservableList<ESawType> values = FXCollections.observableArrayList(ESawType.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

        cmbx.setValue(sawToBeSelected == null ? ESawType.SMALL_SAW : sawToBeSelected);


    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<ESawType>() {
            @Override
            protected void updateItem (ESawType item,boolean empty){
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
        cmbx.setConverter(new StringConverter<ESawType>() {
            @Override
            public String toString(ESawType val) {
                return val.getName();
            }

            @Override
            public ESawType fromString(String string) {
                return null;
            }
        });
    }
}
