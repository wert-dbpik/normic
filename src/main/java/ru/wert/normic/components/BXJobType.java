package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.enums.EJobType;


public class BXJobType {

    private static EJobType LAST_VAL;
    private ComboBox<EJobType> cmbx;

    public void create(ComboBox<EJobType> cmbx){
        this.cmbx = cmbx;
        ObservableList<EJobType> measurements = FXCollections.observableArrayList(EJobType.values());

        cmbx.setItems(measurements);

        createCellFactory();
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = EJobType.JOB_NONE;

        cmbx.getSelectionModel().select(LAST_VAL);

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
                LAST_VAL = job; //последний выбранный префикс становится префиксом по умолчанию
                return job.getJobName();
            }

            @Override
            public EJobType fromString(String string) {
                return null;
            }
        });
    }

}
