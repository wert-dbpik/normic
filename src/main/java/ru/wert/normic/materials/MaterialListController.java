package ru.wert.normic.materials;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.wert.normic.components.BXDensity;
import ru.wert.normic.entities.db_connection.density.Density;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.NormicServices.DENSITIES;
import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class MaterialListController implements MatTypeController{

    @FXML
    private TextField txtFldThickness;

    @FXML
    private ComboBox<Density> bxDensity;

    @FXML
    void initialize(){
        new BXDensity().create(bxDensity);
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
    public double[] readData(){
        double[] sx = new double[2];
        sx[0]  = DoubleParser.getValue(txtFldThickness);//paramS
        sx[1]  = bxDensity.getValue().getAmount();//paramX
        return sx;
    }



    @Override //MatTypeController
    public MatTypeController getController(){
        return this;
    }
}
