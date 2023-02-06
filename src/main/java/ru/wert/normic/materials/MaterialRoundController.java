package ru.wert.normic.materials;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.wert.normic.components.BXDensity;
import ru.wert.normic.entities.db_connection.density.Density;
import ru.wert.normic.entities.db_connection.material.Material;

import static ru.wert.normic.ChogoriServices.CH_DENSITIES;
import static ru.wert.normic.controllers.AbstractOpPlate.DECIMAL_FORMAT;
import static ru.wert.normic.entities.settings.AppSettings.PREPARED_TIME;

public class MaterialRoundController implements MatTypeController{

    @FXML
    private TextField txtFldMassMetre;

    @FXML
    private TextField txtFldDiametre;

    @FXML
    void initialize(){
    }

    @Override //MatTypeController
    public void fillData(Material material){
        txtFldDiametre.setText(DECIMAL_FORMAT.format(material.getParamS()).trim());
        txtFldMassMetre.setText(DECIMAL_FORMAT.format(material.getParamX()).trim());
    }

    @Override //MatTypeController
    public MatTypeController getController(){
        return this;
    }
}
