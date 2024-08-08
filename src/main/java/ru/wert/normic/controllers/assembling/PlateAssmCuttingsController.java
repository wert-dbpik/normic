package ru.wert.normic.controllers.assembling;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFDoubleColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.dataBaseEntities.ops.opAssembling.OpAssmCutting;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.settings.NormConstants.*;

/**
 * СБОРКА РАСКРОЙНЫХ МАТЕРИАЛОВ
 */
public class PlateAssmCuttingsController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfSealer;

    @FXML
    private TextField tfSelfAdhSealer;

    @FXML
    private TextField tfInsulation;

    private OpAssmCutting opData;

    private double sealer; //Уплотнитель на ребро корпуса
    private double selfAdhSealer; //Уплотнитель самоклеющийся
    private double insulation; //Утеплитель

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        new TFNormTime(tfNormTime, prevFormController);
        new TFDoubleColored(tfSealer, this);
        new TFDoubleColored(tfSelfAdhSealer, this);
        new TFDoubleColored(tfInsulation, this);
    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpAssmCutting)data;

         countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getAssmTime();//результат в минутах

        setTimeMeasurement();
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        sealer = DoubleParser.getValue(tfSealer);
        selfAdhSealer = DoubleParser.getValue(tfSelfAdhSealer);
        insulation = DoubleParser.getValue(tfInsulation);

        collectOpData();

    }

    private void collectOpData(){
        opData.setSealer(sealer);
        opData.setSelfAdhSealer(selfAdhSealer);
        opData.setInsulation(insulation);
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

    @Override
    public String helpText() {
        return String.format("УПЛОТНИТЕЛЬ НА РЕБРО КОРПУСА - указывается длина утплотнителя в метрах.\n" +
                        "\tСкорость укладки уплотнителя %s сек/м.\n" +
                        "\n" +
                        "УПЛОТНИТЕЛЬ САМОКЛЕЮЩИЙСЯ - указывается длина утплотнителя в метрах.\n" +
                        "\tСкорость наклеивания уплотнителя %s сек/м.\n" +
                        "\n" +
                        "УКЛАДКА УТЕПЛИТЕЛЯ - указывается общая площадь в м.кв.,\n" +
                        "\tтип утеплителя любой (Изолон, пенопласт и т.д.).\n" +
                        "\tСкорость укладки утеплителя %s мин/м.кв.\n",
                SEALER_SPEED, SELF_ADH_SEALER_SPEED, INSULATION_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
