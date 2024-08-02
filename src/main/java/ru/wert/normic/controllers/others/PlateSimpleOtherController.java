package ru.wert.normic.controllers.others;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import ru.wert.normic.components.TFDoubleColored;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.db_connection.othersOps.SimpleOperation;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.other.OpSimpleOperation;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPieceMeasurement;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

import static java.lang.String.format;

/**
 * ПРОЧИЕ ПРОСТЫЕ ОПЕРАЦИИ
 */
public class PlateSimpleOtherController extends AbstractOpPlate {


    
    @FXML
    private TextField tfAmount;

    @FXML
    private Label lblOperationName;

    @FXML
    private Label lblMeasurement;

    @FXML
    private HBox hbContainer;

    @FXML
    private TextField tfParamA;

    @FXML
    private TextField tfParamB;

    @FXML
    private TextField tfParamC;

    @FXML
    private CheckBox chbInputCounted;

    @FXML
    private HBox hbHeight;


    private OpSimpleOperation opData;
    private SimpleOperation operation;
    private EPieceMeasurement measurement;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpSimpleOperation opData = (OpSimpleOperation) data;
        operation = opData.getOperation();

        lblOperationName.setText(operation.getName().toUpperCase());

        //ЕДИНИЦА ИЗМЕРЕНИЯ
        measurement = operation.getMeasurement();
        lblMeasurement.setText(measurement.getMeasureName());

        //ЧЕКБОКС "ИСПОЛЬЗОВАТЬ ПАРАМЕТРЫ А, В и С"
        chbInputCounted.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                tfAmount.setText(String.format(DOUBLE_FORMAT, opData.getCountedAmount()));
            else {
                if (measurement.equals(EPieceMeasurement.PIECE))
                    tfAmount.setText(String.format(INTEGER_FORMAT, opData.getManualAmount()).trim());
                else
                    tfAmount.setText(String.format(DOUBLE_FORMAT, opData.getManualAmount()));
            }
        });

        if(measurement.equals(EPieceMeasurement.METER) ||
                measurement.equals(EPieceMeasurement.SQUARE_METER) ||
                measurement.equals(EPieceMeasurement.CUBE_METER))
            new TFDoubleColored(tfAmount, this);
        else
            new TFIntegerColored(tfAmount, this);
        tfAmount.disableProperty().bind(chbInputCounted.selectedProperty());

        //ПАРАМЕТР А
        new TFIntegerColored(tfParamA, this);
        tfParamA.disableProperty().bind(chbInputCounted.selectedProperty().not());

        //ПАРАМЕТР B
        new TFIntegerColored(tfParamB, this);
        tfParamB.disableProperty().bind(chbInputCounted.selectedProperty().not());

        //ПАРАМЕТР С
        new TFIntegerColored(tfParamC, this);
        tfParamC.disableProperty().bind(chbInputCounted.selectedProperty().not());

        //ИТОГОВАЯ НОРМА ВРЕМЕНИ
        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            prevFormController.countSumNormTimeByShops();
        });

        //НАСТРАИВАЕМ ПЕРЕМЕННОЕ КОЛИЧЕСТВО ПАРАМЕТРОВ
        if (measurement.equals(EPieceMeasurement.PIECE)) {
            hbContainer.getChildren().clear();
            opData.setInputCounted(false);
            chbInputCounted.setSelected(false);
        } else if(measurement.equals(EPieceMeasurement.METER)){
            hbContainer.getChildren().remove(hbHeight);
            opData.setInputCounted(false);
            chbInputCounted.setSelected(false);
        }
        else if(measurement.equals(EPieceMeasurement.SQUARE_METER))
            hbContainer.getChildren().remove(hbHeight);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpSimpleOperation) data;
        ivOperation.setImage(EOpType.SIMPLE_OPERATION.getLogo());

        countInitialValues();
        opData = (OpSimpleOperation) opData.getOpType().getNormCounter().count(data);

        if(opData.isInputCounted())
            tfAmount.setText(String.format(DOUBLE_FORMAT, opData.getCountedAmount()));


        switch (operation.getNormType()) {//результат в минутах
            case NORM_MECHANICAL:
                currentNormTime = opData.getMechTime();
                break;
            case NORM_PAINTING:
                currentNormTime = opData.getPaintTime();
                break;
            case NORM_ASSEMBLE:
                currentNormTime = opData.getAssmTime();
                break;
            case NORM_PACKING:
                currentNormTime = opData.getPackTime();
                break;
        }

        setTimeMeasurement();
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        if (chbInputCounted != null && chbInputCounted.isSelected()) {
            opData.setParamA(IntegerParser.getValue(tfParamA));
            opData.setParamB(IntegerParser.getValue(tfParamB));
            if (measurement.equals(EPieceMeasurement.CUBE_METER))
                opData.setParamC(IntegerParser.getValue(tfParamC));
        } else {
            opData.setManualAmount(DoubleParser.getValue(tfAmount));
        }

        collectOpData();
    }


    private void collectOpData(){
        opData.setInputCounted(chbInputCounted.isSelected());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpSimpleOperation opData = (OpSimpleOperation)data;

        tfParamA.setText(String.valueOf(opData.getParamA()));
        tfParamB.setText(String.valueOf(opData.getParamB()));
        tfParamC.setText(String.valueOf(opData.getParamC()));

        chbInputCounted.setSelected(opData.isInputCounted());
        if(opData.isInputCounted()){
            tfAmount.setText(String.format(DOUBLE_FORMAT, opData.getCountedAmount()));
        } else {
            if (opData.getOperation().getMeasurement().equals(EPieceMeasurement.PIECE))
                tfAmount.setText(String.format(INTEGER_FORMAT, opData.getManualAmount()).trim());
            else
                tfAmount.setText(String.format(DOUBLE_FORMAT, opData.getManualAmount()));
        }

    }

    @Override
    public String helpText() {
        return operation.getDescription();
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
