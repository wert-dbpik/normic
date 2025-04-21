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
import ru.wert.normic.entities.ops.electrical.OpMountOnDin;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * УСТАНОВКА НА ДИНРЕЙКУ
 */
public class PlateMountOnDinController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfAvtomats;

    @FXML
    private TextField tfHeaters;

    @FXML
    private CheckBox chbDifficult;

    private OpMountOnDin opData;

    private int avtomats; //Количество автоматов
    private int heaters; //Количество нагревателей
    private boolean difficult; //Сложность сборки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfAvtomats, this);
        new TFIntegerColored(tfHeaters, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOnDin) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        heaters = IntegerParser.getValue(tfHeaters);
        avtomats = IntegerParser.getValue(tfAvtomats);
        difficult = chbDifficult.isSelected();

        collectOpData();
    }

    private void collectOpData(){
        opData.setHeaters(heaters);
        opData.setAvtomats(avtomats);
        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountOnDin opData = (OpMountOnDin)data;

        avtomats = opData.getAvtomats();
        tfAvtomats.setText(String.valueOf(avtomats));

        heaters = opData.getHeaters();
        tfHeaters.setText(String.valueOf(heaters));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Установка на динрейку автоматов, термостатов, УЗО,\n" +
                        "\tкоммутаторов, зажимов и т.п устанавливается за %s мин/элемент.\n" +
                        "\n" +
                        "Установка на динрейку нагревателей, счетчиков.\n" +
                        "\tустанавливается за %s мин/элемент.\n" +
                        "\n" +
                        "Установка в стесненных условиях - коэффициент 1,3\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",


                MOUNT_ON_DIN_AUTOMATS, MOUNT_ON_DIN_HEATERS);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
