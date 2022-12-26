package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.TFColoredInteger;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpLocksmith;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

public class PlateLocksmithController extends AbstractOpPlate {

    @FXML
    private ImageView ivContextMenu;

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfCountersinkings;

    @FXML
    private TextField tfThreadings;

    @FXML
    private TextField tfRivets;

    @FXML
    private TextField tfSmallSawings;

    @FXML
    private TextField tfBigSawings;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private Label lblOperationName;

    private int rivets; //Количество заклепок
    private int countersinkings; //количество зенкуемых отверстий
    private int threadings; //Количество нарезаемых резьб
    private int smallSawings; //Количество резов на малой пиле
    private int bigSawings; //Количество резов на большой пиле

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpLocksmith opData = (OpLocksmith)data;

        new TFNormTime(tfNormTime, formController);
        new TFColoredInteger(tfRivets, this);
        new TFColoredInteger(tfCountersinkings, this);
        new TFColoredInteger(tfThreadings, this);
        new TFColoredInteger(tfSmallSawings, this);
        new TFColoredInteger(tfBigSawings, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpLocksmith opData = (OpLocksmith)data;

        countInitialValues();

        double time;
        time =  rivets * RIVETS_SPEED
                + countersinkings * COUNTERSINKING_SPEED
                + threadings * THREADING_SPEED
                + smallSawings * SMALL_SAWING_SPEED
                + bigSawings * BIG_SAWING_SPEED;   //мин

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        rivets = IntegerParser.getValue(tfRivets);
        countersinkings = IntegerParser.getValue(tfCountersinkings);
        threadings = IntegerParser.getValue(tfThreadings);
        smallSawings = IntegerParser.getValue(tfSmallSawings);
        bigSawings = IntegerParser.getValue(tfBigSawings);
    }

    private void collectOpData(OpLocksmith opData){
        opData.setRivets(rivets);
        opData.setCountersinkings(countersinkings);
        opData.setThreadings(threadings);
        opData.setSmallSawings(smallSawings);
        opData.setBigSawings(bigSawings);

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLocksmith opData = (OpLocksmith)data;

        rivets = opData.getRivets();
        tfRivets.setText(String.valueOf(rivets));

        countersinkings = opData.getCountersinkings();
        tfCountersinkings.setText(String.valueOf(countersinkings));

        threadings = opData.getThreadings();
        tfThreadings.setText(String.valueOf(threadings));

        smallSawings = opData.getSmallSawings();
        tfSmallSawings.setText(String.valueOf(smallSawings));

        bigSawings = opData.getBigSawings();
        tfBigSawings.setText(String.valueOf(bigSawings));
    }


}
