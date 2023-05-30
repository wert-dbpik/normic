package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opLocksmith.OpLocksmith;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.db_connection.constants.NormConstants.*;

/**
 * СЛЕСАРНЫЕ ОПЕРАЦИИ
 */
public class PlateLocksmithController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfCountersinkings;

    @FXML
    private TextField tfThreadings;

    @FXML
    private TextField tfRivets;

    private int rivets; //Количество заклепок
    private int countersinkings; //количество зенкуемых отверстий
    private int threadings; //Количество нарезаемых резьб

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfRivets, this);
        new TFIntegerColored(tfCountersinkings, this);
        new TFIntegerColored(tfThreadings, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpLocksmith opData = (OpLocksmith)data;
        ivOperation.setImage(EOpType.LOCKSMITH.getLogo());

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
     * Устанавливает и рассчитывает значения, заданные пользователем
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

    @Override
    public String helpText() {
        return String.format("УСТАНОВКА ВЫТЯЖНЫХ ЗАКЛЕПОК - указывается суммарное количество.\n" +
                        "\tОдна заклепка ставится за %s сек.\n" +
                        "\n" +
                        "ЗЕНКОВАНИЕ ОТВЕРСТИЙ - указывается суммарное количество отверстий.\n" +
                        "\tЗенкование одного отверстия осуществляется за %s мин.\n" +
                        "\n" +
                        "НАРЕЗАНИЕ РЕЗЬБЫ - указывается суммарное количество отверстий.\n" +
                        "\tНарезание резьбы одного отверстия осуществляется за %s мин.\n",

                RIVETS_SPEED, COUNTERSINKING_SPEED, THREADING_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }

}
