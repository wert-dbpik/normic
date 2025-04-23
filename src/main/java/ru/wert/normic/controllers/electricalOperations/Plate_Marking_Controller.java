package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMarking;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.MARKING;

/**
 * МАРКИРОВКА
 */
public class Plate_Marking_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfElements;

    private OpMarking opData;

    private int elements; //Количество элементов

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfElements, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMarking) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        elements = IntegerParser.getValue(tfElements);

        collectOpData();
    }

    private void collectOpData(){
        opData.setElements(elements);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMarking opData = (OpMarking)data;

        elements = opData.getElements();
        tfElements.setText(String.valueOf(elements));

    }

    @Override
    public String helpText() {
        return String.format("Маркировка одной единицы номенклатуры за %s мин/элемент,\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",
                MARKING);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
