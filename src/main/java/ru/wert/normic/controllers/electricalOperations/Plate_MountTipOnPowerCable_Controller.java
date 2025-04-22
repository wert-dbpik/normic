package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountTipOnCable;
import ru.wert.normic.entities.ops.electrical.OpMountTipOnPowerCable;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.MOUNT_TIP_ON_POWER_CABLE;
import static ru.wert.normic.settings.NormConstants.TINNING;

/**
 * ОКОНЦОВКА СИЛОВОГО КАБЕЛЯ
 */
public class Plate_MountTipOnPowerCable_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfTips;

    private OpMountTipOnPowerCable opData;

    private int tips; //Количество наконечников

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfTips, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountTipOnPowerCable) data;

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
        OpMountTipOnPowerCable opData = (OpMountTipOnPowerCable)data;

        tips = opData.getTips();
        tfTips.setText(String.valueOf(tips));

    }

    @Override
    public String helpText() {
        return String.format("Оконцовка силового кабеля наконечником наконечником \nс одной стороны за %s мин/наконечник,\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2 мин на всю партию,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * 0.06 + (Т оп * Т обсл / партия) + Т оп * 0,029 / партия",
                MOUNT_TIP_ON_POWER_CABLE);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
