package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.enums.EMatType;


public class BXMatTypes {

    public static EMatType LAST_VAL;
    private ComboBox<EMatType> cmbx;

    public void create(ComboBox<EMatType> cmbx){
        this.cmbx = cmbx;
        ObservableList<EMatType> values = FXCollections.observableArrayList(EMatType.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

        if(LAST_VAL == null)
            LAST_VAL = EMatType.LIST;

    }

    private void createCellFactory() {
        //CellFactory определяет вид элементов комбобокса - только имя префикса
        cmbx.setCellFactory(i -> new ListCell<EMatType>() {
            @Override
            protected void updateItem (EMatType item,boolean empty){
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getMatTypeName());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<EMatType>() {
            @Override
            public String toString(EMatType val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getMatTypeName();
            }

            @Override
            public EMatType fromString(String string) {
                return null;
            }
        });
    }
}
