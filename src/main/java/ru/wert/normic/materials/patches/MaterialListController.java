package ru.wert.normic.materials.patches;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.wert.normic.components.BXDensity;
import ru.wert.normic.components.TFInteger;
import ru.wert.normic.entities.db_connection.density.Density;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.materials.MatTypeController;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.NormicServices.DENSITIES;
import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class MaterialListController implements MatTypeController {

    @FXML
    private TextField txtFldThickness;

    @FXML
    private ComboBox<Density> bxDensity;

    @FXML
    void initialize(){
        new BXDensity().create(bxDensity);
        new TFInteger(txtFldThickness);
    }

    @Override //MatTypeController
    public void fillData(Material material){
        txtFldThickness.setText(DECIMAL_FORMAT.format(material.getParamS()).trim());
        bxDensity.getSelectionModel().select(DENSITIES.findByValue(material.getParamX()));
    }

    @Override //MatTypeController
    public boolean checkData(){
        if(txtFldThickness.getText().isEmpty()) return false;
        else if(bxDensity.getValue() == null) return false;
        return true;
    }

    @Override //MatTypeController
    public double readParamS(){
        return DoubleParser.getValue(txtFldThickness);//paramS
    }

    @Override //MatTypeController
    public double readParamX(){
        return bxDensity.getValue().getAmount();//paramX
    }


    @Override //MatTypeController
    public MatTypeController getController(){
        return this;
    }
}
