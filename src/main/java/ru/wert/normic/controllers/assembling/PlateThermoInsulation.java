package ru.wert.normic.controllers.assembling;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EMaterialMeasurement;

public class PlateThermoInsulation  extends AbstractOpPlate {

    @FXML
    private TextField tfHeight;

    @FXML
    private TextField tfWidth;

    @FXML
    private TextField tfDepth;

    @FXML
    private ImageView ivHelp;

    @FXML
    private CheckBox chbxFront;

    @FXML
    private CheckBox chbxBack;

    @FXML
    private TextField plusRatio;

    @FXML
    private ComboBox<EMaterialMeasurement> cmbxMeasurement;

    @FXML
    private ComboBox<Integer> cmbxThickness;

    @FXML
    private TextField tfOutlay;

    @FXML
    private Label lblMeasurement;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfNormTime;

    @FXML
    private Label lblNormTimeMeasure;

    @FXML
    private ImageView ivDeleteOperation;



    @Override
    public void fillOpData(OpData opData) {

    }

    @Override
    public void initViews(OpData opData) {

    }

    @Override
    public void countNorm(OpData opData) {

    }

    @Override
    public void countInitialValues() {

    }

    @Override
    public String helpText() {
        return null;
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
