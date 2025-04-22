package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountTipOnCable;
import ru.wert.normic.entities.ops.electrical.OpTinning;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * ОКОНЦОВКА ПРОВОДА
 */
public class Plate_MountTipOnCable_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfTips;

    private OpMountTipOnCable opData;

    private int tips; //Количество наконечников

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfTips, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountTipOnCable) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        tips = IntegerParser.getValue(tfTips);

        collectOpData();
    }

    private void collectOpData(){
        opData.setTips(tips);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountTipOnCable opData = (OpMountTipOnCable)data;

        tips = opData.getTips();
        tfTips.setText(String.valueOf(tips));

    }

    @Override
    public String helpText() {
        return String.format("Оконцовка монтажного провода наконечником \nНШВ, РП, НК, RJ-45 за %s мин/наконечник,\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",
                MOUNT_TIP_ON_CABLE);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
