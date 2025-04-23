package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnVSHG;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * УСТАНОВКА НА ВШГ (4 шт)
 */
public class Plate_MountOnVSHG_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;


    @FXML
    private TextField tfName;

    @FXML
    private TextField tfElements;

    @FXML
    private CheckBox chbDifficult;


    private OpMountOnVSHG opData;

    private String name; //Наименование
    private int elements; //Количество элементов
    private boolean difficult; //Сложность сборки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfElements, this);
        new ChBox(chbDifficult, this);
    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOnVSHG) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        name = tfName.getText().trim();
        elements = IntegerParser.getValue(tfElements);
        difficult = chbDifficult.isSelected();

        collectOpData();
    }

    private void collectOpData(){
        opData.setName(name);
        opData.setElements(elements);
        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountOnVSHG opData = (OpMountOnVSHG)data;

        name = opData.getName();
        tfName.setText(String.valueOf(name));

        elements = opData.getElements();
        tfElements.setText(String.valueOf(elements));


        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Установка на ВШГ(4 шт) изделий при установке на кронштейны,\n" +
                        "монтажные панели за %s мин/элемент.\n" +
                        "\n" +
                        "Установка в стесненных условиях - коэффициент 1,3\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",
                MOUNT_ON_VSHG);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
