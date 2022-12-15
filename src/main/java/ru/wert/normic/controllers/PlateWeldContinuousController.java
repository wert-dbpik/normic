package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.*;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpWeldContinuous;
import ru.wert.normic.enums.EPartBigness;
import ru.wert.normic.utils.IntegerParser;


public class PlateWeldContinuousController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfSeamLength;

    @FXML
    private CheckBox chbxStripping;

    @FXML
    private CheckBox chbxPreEnterSeams;

    @FXML
    private Label lblNumOfSeams;

    @FXML
    private TextField tfSeams;

    @FXML
    private TextField tfMen;

    @FXML
    private Label lblConnectionLength;

    @FXML
    private TextField tfConnectionLength;

    @FXML
    private Label lblStep;

    @FXML
    private TextField tfStep;

    @FXML
    private ComboBox<EPartBigness> cmbxPartBigness;

    @FXML
    private TextField tfNormTime;

    private int seamLength; //Длина шва
    private int seamsCounted; //Количество швов расчетное
    private int seams; //Количество швов заданное пользователем
    private int men; //Число человек, работающих над операцией
    private boolean stripping; //Использовать зачистку
    private int connectionLength; //Длина сединения на которую расчитывается количество точек
    private int step; //шаг точек
    private double assemblingTime; //Время сборки свариваемого узла

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpWeldContinuous opData = (OpWeldContinuous)data;

        tfSeams.disableProperty().bind(chbxPreEnterSeams.selectedProperty().not());
        tfConnectionLength.disableProperty().bind(chbxPreEnterSeams.selectedProperty());
        tfStep.disableProperty().bind(chbxPreEnterSeams.selectedProperty());
        new BXPartBigness().create(cmbxPartBigness);
        new TFNormTime(tfNormTime, formController);
        new TFColoredInteger(tfSeamLength, this);
        new TFColoredInteger(tfSeams, this);
        new TFColoredInteger(tfMen, this);
        new TFColoredInteger(tfConnectionLength, this);
        new TFColoredInteger(tfStep, this);
        new ChBox(chbxPreEnterSeams, this);
        new ChBox(chbxStripping, this);
        new CmBx(cmbxPartBigness, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpWeldContinuous opData = (OpWeldContinuous)data;

        countInitialValues();

        final double WELDING_SPEED = 4.0; //скорость сваркм, мин/гиб

        if(!chbxPreEnterSeams.isSelected()){
            if(step == 0){ //Деление на ноль
                return;
            } else
                seamsCounted = connectionLength/step; //Получаем целу часть от деления
        } else {
            seamsCounted = seams;
        }

        int sumWeldLength = seamsCounted * seamLength;


        double strippingTime;
        if(stripping) {
            //Время на зачистку, мин
            if (sumWeldLength < 100) strippingTime = 0.5;
            else if (sumWeldLength >= 100 && sumWeldLength < 500) strippingTime = 1.8;
            else if (sumWeldLength >= 500 && sumWeldLength < 1000) strippingTime = 3.22;
            else strippingTime = sumWeldLength * MM_TO_M * 3.22;
        } else
            strippingTime = 0.0;


        double time;
        time =  men * (sumWeldLength * MM_TO_M * WELDING_SPEED + assemblingTime) + strippingTime;   //мин
        if(sumWeldLength == 0.0) time = 0.0;

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {

        seamLength = IntegerParser.getValue(tfSeamLength);
        seams = IntegerParser.getValue(tfSeams);
        men = IntegerParser.getValue(tfMen);
        connectionLength = IntegerParser.getValue(tfConnectionLength);
        step = IntegerParser.getValue(tfStep);
        stripping = chbxStripping.isSelected();
        assemblingTime = cmbxPartBigness.getValue().getTime();
    }

    private void collectOpData(OpWeldContinuous opData){
        opData.setSeamLength(seamLength);
        opData.setPartBigness(cmbxPartBigness.getValue());
        opData.setMen(men);
        opData.setStripping(stripping);
        opData.setPreEnterSeams(chbxPreEnterSeams.isSelected());
        opData.setSeams(seams);
        opData.setConnectionLength(connectionLength);
        opData.setStep(step);

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpWeldContinuous opData = (OpWeldContinuous)data;

        seamLength = opData.getSeamLength();
        tfSeamLength.setText(String.valueOf(seamLength));

        cmbxPartBigness.setValue(opData.getPartBigness());

        men = opData.getMen();
        tfMen.setText(String.valueOf(men));

        stripping = opData.isStripping();
        chbxStripping.setSelected(stripping);

        chbxPreEnterSeams.setSelected(opData.isPreEnterSeams());

        seams = opData.getSeams();
        tfSeams.setText(String.valueOf(seams));

        connectionLength = opData.getConnectionLength();
        tfConnectionLength.setText(String.valueOf(connectionLength));

        step = opData.getStep();
        tfStep.setText(String.valueOf(step));

    }

}
