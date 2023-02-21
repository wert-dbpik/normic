package ru.wert.normic.controllers.assembling;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.BXSealersWidth;
import ru.wert.normic.components.CmBx;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpLevelingSealer;
import ru.wert.normic.enums.ESealersWidth;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.LEVELING_SPEED;
import static ru.wert.normic.entities.settings.AppSettings.PREPARED_TIME;

/**
 * НАЛИВКА УПЛОТНИТЕЛЯ
 */
public class PlateLevelingSealerController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivHelpOnLevelingSealer;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private ComboBox<ESealersWidth> cmbxSealerWidth;

    @FXML
    private TextField tfA;

    @FXML
    private TextField tfB;

    @FXML
    private TextField tfCompA;

    @FXML
    private TextField tfCompB;

    @FXML
    private TextField tfNormTime;

    @FXML
    private Label lblNormTimeMeasure;

    private double componentA; //Компонент полиэфирный А
    private double componentB; //Компонент изоцинат B

    private int paramA; //Размер А
    private int paramB;//Размер Б
    private double perimeter; //

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpLevelingSealer opData = (OpLevelingSealer)data;

        new BXSealersWidth().create(cmbxSealerWidth, opData.getSealersWidth(), this);
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfA, this);
        new TFIntegerColored(tfB, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new CmBx(cmbxSealerWidth, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpLevelingSealer opData = (OpLevelingSealer)data;

        countInitialValues();

        double time;
        time =  perimeter * LEVELING_SPEED + PREPARED_TIME;  //мин

        if(perimeter == 0) time = 0.0;
        else {
            tfCompA.setText(String.format(DOUBLE_FORMAT, perimeter * cmbxSealerWidth.getValue().getCompA()));
            tfCompB.setText(String.format(DOUBLE_FORMAT, perimeter * cmbxSealerWidth.getValue().getCompB()));
        }

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        paramA = IntegerParser.getValue(tfA);
        paramB = IntegerParser.getValue(tfB);
        perimeter = 2 * (paramA + paramB) * MM_TO_M;
    }


    private void collectOpData(OpLevelingSealer opData){
        opData.setCompA(tfCompA.getText().isEmpty() ? 0.0 : Double.parseDouble(tfCompA.getText().replace(",", ".")));
        opData.setCompB(tfCompB.getText().isEmpty() ? 0.0 : Double.parseDouble(tfCompB.getText().replace(",", ".")));

        opData.setSealersWidth(cmbxSealerWidth.getValue());
        opData.setParamA(paramA);
        opData.setParamB(paramB);

        opData.setAssmTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLevelingSealer opData = (OpLevelingSealer)data;

        cmbxSealerWidth.setValue(opData.getSealersWidth());

//        componentA = opData.getCompA();
//        tfCompA.setText(String.valueOf(componentA));
//
//        componentB = opData.getCompA();
//        tfCompB.setText(String.valueOf(componentB));

        paramA = opData.getParamA();
        tfA.setText(String.valueOf(paramA));

        paramB = opData.getParamB();
        tfB.setText(String.valueOf(paramB));

    }
}
