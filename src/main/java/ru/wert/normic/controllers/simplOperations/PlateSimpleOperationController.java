package ru.wert.normic.controllers.simplOperations;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.BXMaterial;
import ru.wert.normic.components.BtnAddMaterial;
import ru.wert.normic.components.TFDoubleColored;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.simpleOperations.OpSimpleOperation;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPieceMeasurement;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

import static java.lang.String.format;
import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

/**
 * ПРОЧИЕ ПРОСТЫЕ ОПЕРАЦИИ
 */
public class PlateSimpleOperationController extends AbstractOpPlate {
    @FXML
    private VBox vbOperation;

    @FXML
    private HBox hbMaterialContainer;

    @FXML
    private Separator separatorMaterial;

    @FXML
    private ComboBox<Material> bxMaterial;

    @FXML
    private Button btnAddMaterial;

    @FXML
    private TextField tfAmount;

    @FXML
    private TextField tfN;

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

    @FXML
    private TextField tfNormTime;

    private OpSimpleOperation opData;
    private SimpleOperation operation;
    private EPieceMeasurement measurement;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        opData = (OpSimpleOperation) data;
        operation = opData.getOperationPrototype();

        //Материал
        if(opData.getOperationPrototype().isCountMaterial()){
            new BXMaterial().create(bxMaterial, true, opData.getMaterial());
            //Кнопка Добавить материал
            new BtnAddMaterial(btnAddMaterial, bxMaterial);
        }
        else
            vbOperation.getChildren().removeAll(hbMaterialContainer, separatorMaterial);

        //Количество
        new TFIntegerColored(tfN, this);

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

        //1 шт
        tfAmount.disableProperty().bind(chbInputCounted.selectedProperty());
        if(measurement.equals(EPieceMeasurement.METER) ||
                measurement.equals(EPieceMeasurement.SQUARE_METER) ||
                measurement.equals(EPieceMeasurement.CUBE_METER))
            new TFDoubleColored(tfAmount, this);
        else
            new TFIntegerColored(tfAmount, this);

        //ПАРАМЕТР А
        new TFIntegerColored(tfParamA, this);
        tfParamA.disableProperty().bind(chbInputCounted.selectedProperty().not());

        //ПАРАМЕТР B
        new TFIntegerColored(tfParamB, this);
        tfParamB.disableProperty().bind(chbInputCounted.selectedProperty().not());

        //ПАРАМЕТР С
        new TFIntegerColored(tfParamC, this);
        tfParamC.disableProperty().bind(chbInputCounted.selectedProperty().not());

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

        if(opData.isInputCounted())
            tfAmount.setText(String.format(DOUBLE_FORMAT, opData.getCountedAmount()));

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
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
        opData.setNum(IntegerParser.getValue(tfN));

        collectOpData();
    }


    private void collectOpData(){
        opData.setInputCounted(chbInputCounted.isSelected());
        opData.setMaterial(bxMaterial.getValue());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpSimpleOperation opData = (OpSimpleOperation)data;

        bxMaterial.setValue(opData.getMaterial());
        tfParamA.setText(String.valueOf(opData.getParamA()));
        tfParamB.setText(String.valueOf(opData.getParamB()));
        tfParamC.setText(String.valueOf(opData.getParamC()));
        tfN.setText(String.valueOf(opData.getNum()));

        chbInputCounted.setSelected(opData.isInputCounted());
        if(opData.isInputCounted()){
            tfAmount.setText(String.format(DOUBLE_FORMAT, opData.getCountedAmount()));
        } else {
            if (opData.getOperationPrototype().getMeasurement().equals(EPieceMeasurement.PIECE))
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
