package ru.wert.normic.controllers.assembling;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PlateThermoInsulation {



    @FXML
    private ImageView ivHelp;

    @FXML
    private TextField plusRatio;

    @FXML
    private CheckBox chbxFront;

    @FXML
    private TextField tfWidth;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private ComboBox<String> cmbxMeasurement;

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfOutlay;

    @FXML
    private CheckBox chbxBack;

    @FXML
    private TextField tfHeight;

    @FXML
    private ComboBox<?> cmbxThickness;

    @FXML
    private Label lblNormTimeMeasure;

    @FXML
    private Label lblMeauserment;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfDepth;

}
