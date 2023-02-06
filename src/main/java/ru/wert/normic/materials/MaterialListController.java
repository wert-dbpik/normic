package ru.wert.normic.materials;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ru.wert.normic.components.BXDensity;
import ru.wert.normic.entities.db_connection.density.Density;
import ru.wert.normic.entities.db_connection.material.Material;

import static ru.wert.normic.ChogoriServices.CH_DENSITIES;
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
        System.out.println(CH_DENSITIES.findByValue(material.getParamX()).getName());
        bxDensity.getSelectionModel().select(CH_DENSITIES.findByValue(material.getParamX()));
    }

    @Override //MatTypeController
    public MatTypeController getController(){
        return this;
    }
}
