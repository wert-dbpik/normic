package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFDoubleColored;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpFixOfCables;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.FIX_OF_CABLES_SPEED;

/**
 * МАРКИРОВКА
 */
public class PlateFixOfCablesController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfLength;

    private OpFixOfCables opData;

    private double length; //Длина жгута

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFDoubleColored(tfLength, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpFixOfCables) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        length = DoubleParser.getValue(tfLength);

        collectOpData();
    }

    private void collectOpData(){
        opData.setLength(length);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpFixOfCables opData = (OpFixOfCables)data;

        length = opData.getLength();
        tfLength.setText(String.valueOf(length));

    }

    @Override
    public String helpText() {
        return String.format("Укладка жгутов с помощью бандажа, стяжек каждые 0,3 м за V укл = %s мин/элемент.\n" +
                        "\n\t\t\tT норм = L /0.3 * V укл, мин\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",
                FIX_OF_CABLES_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
