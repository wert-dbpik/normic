package ru.wert.normic.materials.patches;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ru.wert.normic.components.TFDouble;
import ru.wert.normic.components.TFInteger;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.materials.MatTypeController;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;

public class MaterialProfileController implements MatTypeController {

    public static void createForm(){

    }


    @FXML
    private TextField tfPerimeter;

    @FXML
    private TextField tfMassMetre;

    @FXML
    void initialize(){
        new TFInteger(tfPerimeter);
        new TFDouble(tfMassMetre);
    }

    @Override //MatTypeController
    public void fillData(Material material){
        tfPerimeter.setText(DECIMAL_FORMAT.format(material.getParamS()));
        tfMassMetre.setText(DECIMAL_FORMAT.format(material.getParamX()));
    }

    @Override //MatTypeController
    public boolean checkData(){
        if(tfPerimeter.getText().isEmpty() || tfMassMetre.getText().isEmpty()) return false;
        return true;
    }

    @Override //MatTypeController
    public double readParamS(){
        return DoubleParser.getValue(tfPerimeter);//paramS
    }

    @Override //MatTypeController
    public double readParamX(){
        return DoubleParser.getValue(tfMassMetre);//paramX
    }

    @Override //MatTypeController
    public MatTypeController getController(){
        return this;
    }
}
