package ru.wert.normic.materials;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class MaterialProfileController implements MatTypeController{


    @FXML
    private TextField tfPerimeter;

    @FXML
    private TextField tfMassMetre;

    @FXML
    void initialize(){
    }

    @Override //MatTypeController
    public void fillData(Material material){
        tfPerimeter.setText(String.format(String.valueOf((DECIMAL_FORMAT)), material.getParamS()));
        tfMassMetre.setText(String.format(String.valueOf((DECIMAL_FORMAT)), material.getParamX()));
    }

    @Override //MatTypeController
    public boolean checkData(){
        if(tfPerimeter.getText().isEmpty() || tfMassMetre.getText().isEmpty()) return false;
        return true;
    }

    @Override //MatTypeController
    public double[] readData(){
        double[] sx = new double[2];
        sx[0]  = DoubleParser.getValue(tfPerimeter);//paramS
        sx[1]  = DoubleParser.getValue(tfMassMetre);//paramX
        return sx;
    }

    @Override //MatTypeController
    public MatTypeController getController(){
        return this;
    }
}
