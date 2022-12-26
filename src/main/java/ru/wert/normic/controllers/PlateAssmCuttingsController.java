package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.TFColoredDouble;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.entities.OpAssmCutting;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

public class PlateAssmCuttingsController extends AbstractOpPlate {

    @FXML
    private ImageView ivContextMenu;

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

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
    @Override //AbstractOpPlate
    public  void countInitialValues() {

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
