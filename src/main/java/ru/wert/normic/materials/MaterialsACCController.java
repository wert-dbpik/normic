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
import ru.wert.normic.enums.EMatType;

public class MaterialsACCController {
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOk;

    @FXML
    private ComboBox<EMatType> bxMatType;

    @FXML
    private StackPane spForCalculation;

    @FXML
    private TextArea taMaterialNote;

    @FXML
    private TextField tfMaterialName;

    @FXML
    private StackPane spIndicator;

    @FXML
    private StackPane stackPaneForButtons;

    @FXML
    void initialize(){
        new BXMatTypes().create(bxMatType, EMatType.LIST, this);
        new BXDensity().create(bxMatType);
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void ok(ActionEvent event) {

    }
}
