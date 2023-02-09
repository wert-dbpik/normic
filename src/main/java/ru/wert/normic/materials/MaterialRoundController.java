package ru.wert.normic.materials;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class MaterialRoundController implements MatTypeController{

    @FXML
    private TextField txtFldDiametre;

    @FXML
    private TextField txtFldMassMetre;

    @FXML
    void initialize(){
    }

    @Override //MatTypeController
    public void fillData(Material material){
        txtFldDiametre.setText(DECIMAL_FORMAT.format(material.getParamS()).trim());
        txtFldMassMetre.setText(DECIMAL_FORMAT.format(material.getParamX()).trim());
    }

    @Override //MatTypeController
    public boolean checkData(){
        if(txtFldDiametre.getText().isEmpty() || txtFldMassMetre.getText().isEmpty()) return false;
        return true;
    }

    @Override //MatTypeController
    public double[] readData(){
        double[] sx = new double[2];
        sx[0]  = DoubleParser.getValue(txtFldDiametre);//paramS
        sx[1]  = DoubleParser.getValue(txtFldMassMetre);//paramX
        return sx;
    }

    @Override //MatTypeController
    public MatTypeController getController(){
        return this;
    }
}
