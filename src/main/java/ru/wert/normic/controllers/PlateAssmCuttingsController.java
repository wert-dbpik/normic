package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.AbstractOpPlate;
import ru.wert.normic.components.TFColoredDouble;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.entities.OpAssmCutting;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.interfaces.IFormController;
import ru.wert.normic.utils.DoubleParser;

public class PlateAssmCuttingsController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfSealer;

    @FXML
    private TextField tfSelfAdhSealer;

    @FXML
    private TextField tfInsulation;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private Label lblOperationName;

    private double sealer; //Уплотнитель на ребро корпуса
    private double selfAdhSealer; //Уплотнитель самоклеющийся
    private double insulation; //Утеплитель

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpAssmCutting opData = (OpAssmCutting)data;

        new TFNormTime(tfNormTime, formController);
        new TFColoredDouble(tfSealer, this);
        new TFColoredDouble(tfSelfAdhSealer, this);
        new TFColoredDouble(tfInsulation, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpAssmCutting opData = (OpAssmCutting)data;

         countInitialValues();

        final double SEALER_SPEED = 40 * SEC_TO_MIN; //скорость монтажа уплотнителя
        final double SELF_ADH_SEALER_SPEED =  20 * SEC_TO_MIN; //скорость наклейки уплотнителя
        final double INSULATION_SPEED = 5.5; //скорость разметки, резки и укладки уплотнителя

        double time;
        time =  sealer * SEALER_SPEED
                + selfAdhSealer * SELF_ADH_SEALER_SPEED
                + insulation * INSULATION_SPEED;//мин

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {

        sealer = DoubleParser.getValue(tfSealer);
        selfAdhSealer = DoubleParser.getValue(tfSelfAdhSealer);
        insulation = DoubleParser.getValue(tfInsulation);

    }

    private void collectOpData(OpAssmCutting opData){
        opData.setSealer(sealer);
        opData.setSelfAdhSealer(selfAdhSealer);
        opData.setInsulation(insulation);

        opData.setAssmTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpAssmCutting opData = (OpAssmCutting)data;

        sealer = opData.getSealer();
        tfSealer.setText(String.valueOf(sealer));

        selfAdhSealer = opData.getSelfAdhSealer();
        tfSelfAdhSealer.setText(String.valueOf(selfAdhSealer));

        insulation = opData.getInsulation();
        tfInsulation.setText(String.valueOf(insulation));
    }


}
