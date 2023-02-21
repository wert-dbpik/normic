package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpLocksmith;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * СЛЕСАРНЫЕ ОПЕРАЦИИ
 */
public class PlateLocksmithController extends AbstractOpPlate {

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
    private ImageView ivDeleteOperation;

    @FXML
    private Label lblOperationName;

    private int rivets; //Количество заклепок
    private int countersinkings; //количество зенкуемых отверстий
    private int threadings; //Количество нарезаемых резьб

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpLocksmith opData = (OpLocksmith)data;

        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfRivets, this);
        new TFIntegerColored(tfCountersinkings, this);
        new TFIntegerColored(tfThreadings, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpLocksmith opData = (OpLocksmith)data;

        countInitialValues();

        double time;
        time =  rivets * RIVETS_SPEED  * SEC_TO_MIN
                + countersinkings * COUNTERSINKING_SPEED
                + threadings * THREADING_SPEED;   //мин

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

    }

    private void collectOpData(OpLocksmith opData){
        opData.setRivets(rivets);
        opData.setCountersinkings(countersinkings);
        opData.setThreadings(threadings);

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

    }


}
