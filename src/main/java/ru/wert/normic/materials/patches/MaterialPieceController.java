package ru.wert.normic.materials.patches;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.wert.normic.components.BXPieceMeasurement;
import ru.wert.normic.components.TFDouble;
import ru.wert.normic.dataBaseEntities.db_connection.material.Material;
import ru.wert.normic.enums.EPieceMeasurement;
import ru.wert.normic.materials.MatTypeController;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class MaterialPieceController implements MatTypeController {

    @FXML
    private TextField txtFldPieceQuantity;

    @FXML
    private ComboBox<EPieceMeasurement> bxPieceMeasurement;

    @FXML
    void initialize(){
        new BXPieceMeasurement().create(bxPieceMeasurement);
        new TFDouble(txtFldPieceQuantity);
    }

    @Override //MatTypeController
    public void fillData(Material material){
        txtFldPieceQuantity.setText(DECIMAL_FORMAT.format(material.getParamS()).trim());
        bxPieceMeasurement.getSelectionModel().select(EPieceMeasurement.values()[(int)material.getParamX()]);
    }

    @Override //MatTypeController
    public boolean checkData(){
        if(txtFldPieceQuantity.getText().isEmpty()) return false;
        else return bxPieceMeasurement.getValue() != null;
    }

    @Override //MatTypeController
    public double readParamS(){
        return DoubleParser.getValue(txtFldPieceQuantity);//paramS
    }

    @Override //MatTypeController
    public double readParamX(){
        return bxPieceMeasurement.getValue().ordinal();//paramX
    }


    @Override //MatTypeController
    public MatTypeController getController(){
        return this;
    }
}
