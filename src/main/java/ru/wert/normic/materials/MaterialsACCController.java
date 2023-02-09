package ru.wert.normic.materials;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import ru.wert.normic.components.BXColor;
import ru.wert.normic.components.BXDensity;
import ru.wert.normic.components.BXMatTypes;
import ru.wert.normic.components.BXMaterialGroups;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.material_group.MaterialGroup;
import ru.wert.normic.enums.EMatType;

import static ru.wert.normic.NormicServices.MATERIAL_GROUPS;
import static ru.wert.normic.NormicServices.MAT_TYPES;

public class MaterialsACCController {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOk;

    @FXML
    private StackPane spForCalculation;

    @FXML
    private ComboBox<MaterialGroup> bxMaterialGroup;

    @FXML
    private ComboBox<EMatType> bxMatType;

    @FXML
    private TextField tfMaterialName;

    @FXML
    private TextArea taMaterialNote;

    @FXML
    private StackPane spIndicator;

    @FXML
    private StackPane stackPaneForButtons;

    private Material material;
    private MatTypeController matTypeController;


    public void init(Material material, MatTypeController matTypeController){
        this.material = material;
        this.matTypeController = matTypeController;

        new BXMatTypes().create(bxMatType, EMatType.LIST, this);
        new BXMaterialGroups().create(bxMaterialGroup);

        matTypeController.fillData(material);
        fillData();
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void ok(ActionEvent event) {

    }

    private void fillData(){
        tfMaterialName.setText(material.getName());
        bxMatType.setValue(EMatType.getTypeByName(material.getMatType().getName()));
        bxMaterialGroup.setValue(material.getCatalogGroup());
        taMaterialNote.setText(material.getNote());
    }

    private boolean checkData(){
        if(tfMaterialName.getText().isEmpty()) return false;
        else if(bxMatType.getValue() == null) return false;
        else if(bxMaterialGroup.getValue() == null) return false;
        return true;
    }

    private void readData(){
        material.setName(tfMaterialName.getText().trim());
        material.setNote(tfMaterialName.getText().trim());
        material.setMatType(MAT_TYPES.findByName(bxMatType.getValue().getName()));
    }

    private Material creatNewMaterial(){
        Material newMaterial = new Material();
        newMaterial.setName(tfMaterialName.getText().trim());
        newMaterial.setMatType(MAT_TYPES.findByName(bxMatType.getValue().getName()));
        newMaterial.setCatalogGroup(MATERIAL_GROUPS.findByName(bxMaterialGroup.getValue().getName()));
        newMaterial.setNote(tfMaterialName.getText().trim());

        return newMaterial;
    }




}
