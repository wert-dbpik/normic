package ru.wert.normic.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.materials.MaterialsACCController;


public class BXMatTypes {

    public static EMatType LAST_VAL;
    private ComboBox<EMatType> cmbx;

    public void create(ComboBox<EMatType> cmbx, EMatType initVal, MaterialsACCController controller){
        this.cmbx = cmbx;
        ObservableList<EMatType> values = FXCollections.observableArrayList(EMatType.values());
        cmbx.setItems(values);

        createCellFactory();
        //Выделяем префикс по умолчанию
        createConverter();

//        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
//            controller.countNorm(controller.getOpData());
//        });

        if(LAST_VAL == null)
            LAST_VAL = EMatType.LIST;

        if (initVal == null) {
            cmbx.setValue(LAST_VAL);
        } else {
            cmbx.setValue(initVal);
        }

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
                    setText(item.getName());
                }
            }

        });
    }

    private void createConverter() {
        cmbx.setConverter(new StringConverter<EMatType>() {
            @Override
            public String toString(EMatType val) {
                LAST_VAL = val; //последний выбранный префикс становится префиксом по умолчанию
                return val.getName();
            }

            @Override
            public EMatType fromString(String string) {
                return null;
            }
        });
    }
}
