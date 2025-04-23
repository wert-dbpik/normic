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
import ru.wert.normic.entities.ops.electrical.OpMountOnDinAutomats;
import ru.wert.normic.entities.ops.electrical.OpMountOnDinHeaters;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_DIN_AUTOMATS;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_DIN_HEATERS;

/**
 * УСТАНОВКА НА ДИНРЕЙКУ НАГРЕВАТЕЛЕЙ
 */
public class Plate_MountOnDinHeaters_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfHeaters;

    @FXML
    private CheckBox chbDifficult;

    private OpMountOnDinHeaters opData;

    private String name; //Наименование
    private int heaters; //Количество нагревателей
    private boolean difficult; //Сложность сборки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfHeaters, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOnDinHeaters) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        name = tfName.getText().trim();
        heaters = IntegerParser.getValue(tfHeaters);
        difficult = chbDifficult.isSelected();

        collectOpData();
    }

    private void collectOpData(){
        opData.setName(name);
        opData.setHeaters(heaters);
        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountOnDinHeaters opData = (OpMountOnDinHeaters)data;

        name = opData.getName();
        tfName.setText(String.valueOf(name));

        heaters = opData.getHeaters();
        tfHeaters.setText(String.valueOf(heaters));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Установка на динрейку нагревателей, счетчиков.\n" +
                        "устанавливается за %s мин/элемент.\n" +
                        "\n" +
                        "Установка в стесненных условиях - коэффициент 1,3\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",


                MOUNT_ON_DIN_HEATERS);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
