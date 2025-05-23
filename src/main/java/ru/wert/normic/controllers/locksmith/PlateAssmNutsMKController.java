package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opLocksmith.OpAssmNutMK;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * КРЕПЕЖ (УЧАСТОК МК)
 */
public class PlateAssmNutsMKController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfOthers;

    @FXML
    private TextField tfRivets;

    @FXML
    private TextField tfRivetNuts;

    private OpAssmNutMK opData;

    private int rivets; //Количество заклепок
    private int rivetNuts; //Количество аклепочных гаек
    private int others; //Количество другого крепежа

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFNormTime(tfNormTime, prevFormController);
        new TFIntegerColored(tfRivets, this);
        new TFIntegerColored(tfRivetNuts, this);
        new TFIntegerColored(tfOthers, this);
    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpAssmNutMK)data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {

        rivets = IntegerParser.getValue(tfRivets);
        rivetNuts = IntegerParser.getValue(tfRivetNuts);
        others = IntegerParser.getValue(tfOthers);

        collectOpData();
    }


    private void collectOpData(){
        opData.setRivets(rivets);
        opData.setRivetNuts(rivetNuts);
        opData.setOthers(others);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpAssmNutMK opData = (OpAssmNutMK)data;

        rivets = opData.getRivets();
        tfRivets.setText(String.valueOf(rivets));

        rivetNuts = opData.getRivetNuts();
        tfRivetNuts.setText(String.valueOf(rivetNuts));

        others = opData.getOthers();
        tfOthers.setText(String.valueOf(others));

    }

    @Override
    public String helpText() {
        return String.format("ВЫТЯЖНЫЕ ЗАКЛЕПКИ - указывается суммарное количество в сборке.\n" +
                        "\tОдна заклепка устанавливается за %s сек.\n" +
                        "\n" +
                        "ЗАКЛЕПОЧНЫЕ ГАЙКИ - указывается суммарное количество в сборке.\n" +
                        "\tОдна заклепочная гайка устанавливается за %s сек.\n" +
                        "\n" +
                        "ДРУГОЙ КРЕПЕЖ - суммируется любой другой крепеж, не вошедший в спиок.\n" +
                        "\tОдин крепеж устанавливается за %s сек.\n",

                RIVETS_SPEED, RIVET_NUTS_SPEED, OTHERS_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }

}
