package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMarking;
import ru.wert.normic.entities.ops.electrical.OpMountOfCableEntries;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.MARKING_SPEED;
import static ru.wert.normic.settings.NormConstants.MOUNT_OF_CABLE_ENTRIES;

/**
 * УСТАНОВКА КАБЕЛЬНЫХ ВВОДОВ
 */
public class Plate_MountOfCableEntries_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfElements;

    private OpMountOfCableEntries opData;

    private String name; //Наименование
    private int elements; //Количество элементов

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfElements, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOfCableEntries) data;

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

        collectOpData();
    }

    private void collectOpData(){
        opData.setName(name);
        opData.setElements(elements);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountOfCableEntries opData = (OpMountOfCableEntries)data;

        name = opData.getName();
        tfName.setText(String.valueOf(name));

        elements = opData.getElements();
        tfElements.setText(String.valueOf(elements));

    }

    @Override
    public String helpText() {
        return String.format("Установка кабельных вводов, гофрированных втулок, MG, PG " +
                        "за %s мин/элемент,\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",
                MOUNT_OF_CABLE_ENTRIES);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
