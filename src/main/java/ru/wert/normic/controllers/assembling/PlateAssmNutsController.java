package ru.wert.normic.controllers.assembling;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.assembling.countings.OpAssmNutsCounter;
import ru.wert.normic.entities.ops.opAssembling.OpAssmNut;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.settings.NormConstants.*;

/**
 * СБОРКА КРЕПЕЖА
 */
public class PlateAssmNutsController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfOthers;

    @FXML
    private TextField tfGroundSets;

    @FXML
    private TextField tfVSHGs;

    @FXML
    private TextField tfRivets;

    @FXML
    private TextField tfRivetNuts;

    @FXML
    private TextField tfScrews;

    private OpAssmNut opData;

    private int screws; //Количество винтов
    private int vshgs; //Количество комплектов ВШГ
    private int rivets; //Количество заклепок
    private int rivetNuts; //Количество аклепочных гаек
    private int groundSets; //Количество комплектов заземления с этикеткой
    private int others; //Количество другого крепежа

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfScrews, this);
        new TFIntegerColored(tfVSHGs, this);
        new TFIntegerColored(tfRivets, this);
        new TFIntegerColored(tfRivetNuts, this);
        new TFIntegerColored(tfGroundSets, this);
        new TFIntegerColored(tfOthers, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpAssmNut)data;

        countInitialValues();

        currentNormTime = OpAssmNutsCounter.count((OpAssmNut) data).getAssmTime();//результат в минутах

        setTimeMeasurement();
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        screws = IntegerParser.getValue(tfScrews);
        vshgs = IntegerParser.getValue(tfVSHGs);
        rivets = IntegerParser.getValue(tfRivets);
        rivetNuts = IntegerParser.getValue(tfRivetNuts);
        groundSets = IntegerParser.getValue(tfGroundSets);
        others = IntegerParser.getValue(tfOthers);

        collectOpData();
    }

    private void collectOpData(){
        opData.setScrews(screws);
        opData.setVshgs(vshgs);
        opData.setRivets(rivets);
        opData.setRivetNuts(rivetNuts);
        opData.setGroundSets(groundSets);
        opData.setOthers(others);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpAssmNut opData = (OpAssmNut)data;

        screws = opData.getScrews();
        tfScrews.setText(String.valueOf(screws));

        vshgs = opData.getVshgs();
        tfVSHGs.setText(String.valueOf(vshgs));

        rivets = opData.getRivets();
        tfRivets.setText(String.valueOf(rivets));

        rivetNuts = opData.getRivetNuts();
        tfRivetNuts.setText(String.valueOf(rivetNuts));

        groundSets = opData.getGroundSets();
        tfGroundSets.setText(String.valueOf(groundSets));

        others = opData.getOthers();
        tfOthers.setText(String.valueOf(others));

    }

    @Override
    public String helpText() {
        return String.format("ВИНТЫ - указывается суммарное количество в сборке.\n" +
                        "\tОдин винт устанавливается за %s мин.\n" +
                        "\n" +
                        "ВИНТ-ШАЙБА-ГАЙКА - указывается суммарное количество в сборке.\n" +
                        "\tОдин комплект устанавливается за %s мин.\n" +
                        "\n" +
                        "ВЫТЯЖНЫЕ ЗАКЛЕПКИ - указывается суммарное количество в сборке.\n" +
                        "\tОдна заклепка устанавливается за %s сек.\n" +
                        "\n" +
                        "ЗАКЛЕПОЧНЫЕ ГАЙКИ - указывается суммарное количество в сборке.\n" +
                        "\tОдна заклепочная гайка устанавливается за %s сек.\n" +
                        "\n" +
                        "ЗАЗЕМЛЕНИЕ (гайка-шайба-этикетка) - указывается суммарное количество в сборке.\n" +
                        "\tОдин комплект устанавливается за %s мин.\n" +
                        "\n" +
                        "ДРУГОЙ КРЕПЕЖ - суммируется любой другой крепеж, не вошедший в спиок.\n" +
                        "\tОдин крепеж устанавливается за %s сек.\n",

                SCREWS_SPEED, VSHGS_SPEED, RIVETS_SPEED, RIVET_NUTS_SPEED, GROUND_SETS_SPEED, OTHERS_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }

}
