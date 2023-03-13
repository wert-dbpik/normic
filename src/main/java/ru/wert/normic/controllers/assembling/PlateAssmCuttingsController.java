package ru.wert.normic.controllers.assembling;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.TFDoubleColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.opAssembling.OpAssmCutting;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.utils.DoubleParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

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

    @FXML
    private Label lblOperationName;

    private double sealer; //Уплотнитель на ребро корпуса
    private double selfAdhSealer; //Уплотнитель самоклеющийся
    private double insulation; //Утеплитель

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpAssmCutting opData = (OpAssmCutting)data;

        new TFNormTime(tfNormTime, formController);
        new TFDoubleColored(tfSealer, this);
        new TFDoubleColored(tfSelfAdhSealer, this);
        new TFDoubleColored(tfInsulation, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpAssmCutting opData = (OpAssmCutting)data;

         countInitialValues();

        double time;
        time =  sealer * SEALER_SPEED * SEC_TO_MIN
                + selfAdhSealer * SELF_ADH_SEALER_SPEED * SEC_TO_MIN
                + insulation * INSULATION_SPEED;//мин

        currentNormTime = time;
        collectOpData(opData);
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
