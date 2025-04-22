package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpIsolateWithThermTube10;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.ISOLATE_WITH_THERM_TUBE_10;

/**
 * ИЗОЛЯЦИЯ ПРОВОДОВ ТЕРМОУСАДОЧНОЙ ТРУБКОЙ 2-10 ММ
 */
public class Plate_IsolateWithThermotube10_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfPins;

    private OpIsolateWithThermTube10 opData;

    private int pins; //Количество наконечников

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfPins, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpIsolateWithThermTube10) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        pins = IntegerParser.getValue(tfPins);

        collectOpData();
    }

    private void collectOpData(){
        opData.setPins(pins);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpIsolateWithThermTube10 opData = (OpIsolateWithThermTube10)data;

        pins = opData.getPins();
        tfPins.setText(String.valueOf(pins));

    }

    @Override
    public String helpText() {
        return String.format("Изоляция провода термоизоляционной трубкой 2-10 мм за %s мин/место,\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",
                ISOLATE_WITH_THERM_TUBE_10);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
