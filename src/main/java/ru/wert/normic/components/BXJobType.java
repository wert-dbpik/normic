package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.enums.EJobType;


public class BXJobType {

    private ComboBox<EJobType> cmbx;

    public void create(ComboBox<EJobType> cmbx){
        this.cmbx = cmbx;
        ObservableList<EJobType> measurements = FXCollections.observableArrayList(EJobType.values());

        cmbx.setItems(measurements);

        createCellFactory();
        createConverter();

        cmbx.setValue(EJobType.JOB_NONE);

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EJobType>() {
            @Override
            protected void updateItem (EJobType item,boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getJobName());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<EJobType>() {
            @Override
            public String toString(EJobType job) {
                return job.getJobName();
            }

            @Override
            public EJobType fromString(String string) {
                return null;
            }
        });
    }

}
