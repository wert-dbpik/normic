package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOfSignalEquip;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.MOUNT_OF_SIGNAL_EQUIP_SPEED;

/**
 * УСТАНОВКА И ПОДКЛЮЧЕНИЕ СИГНАЛЬНОЙ АППАРАТУРЫ
 */
public class Plate_MountOfSignalEquip_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfElements;

    private OpMountOfSignalEquip opData;

    private String name; //Наименование
    private int elements; //Количество элементов

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfElements, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOfSignalEquip) data;

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
        OpMountOfSignalEquip opData = (OpMountOfSignalEquip)data;

        name = opData.getName();
        tfName.setText(String.valueOf(name));

        elements = opData.getElements();
        tfElements.setText(String.valueOf(elements));

    }

    @Override
    public String helpText() {
        return String.format("Установка и подключение сигнальной аппаратуры:\n" +
                        "сигнальных ламп, кнопок с накидной гайкой -  за %s мин/элемент\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",
                MOUNT_OF_SIGNAL_EQUIP_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
