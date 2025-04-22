package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpCutCableOnMachine;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.CUTTING_CABLE_ON_MACHINE;
import static ru.wert.normic.settings.NormConstants.SOLDERING_SPEED;

/**
 * ПАЙКА ЭЛЕКТРОПАЯЛЬНИКОМ
 */
public class Plate_CutCableOnMachine_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfCuts;

    private OpCutCableOnMachine opData;

    private int cuts; //Количество элементов

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfCuts, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpCutCableOnMachine) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        cuts = IntegerParser.getValue(tfCuts);

        collectOpData();
    }

    private void collectOpData(){
        opData.setCuts(cuts);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutCableOnMachine opData = (OpCutCableOnMachine)data;

        cuts = opData.getCuts();
        tfCuts.setText(String.valueOf(cuts));

    }

    @Override
    public String helpText() {
        return String.format("Резка проводов на автомате за %s мин/рез,\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",
                CUTTING_CABLE_ON_MACHINE);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
