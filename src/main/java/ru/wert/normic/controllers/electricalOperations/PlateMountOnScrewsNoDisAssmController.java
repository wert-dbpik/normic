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
import ru.wert.normic.entities.ops.electrical.OpMountOnScrewsNoDisAssm;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * УСТАНОВКА НА ВИНТЫ БЕЗ РАЗБОРКИ КОРПУСА
 */
public class PlateMountOnScrewsNoDisAssmController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tf2Screws;

    @FXML
    private TextField tf4Screws;

    @FXML
    private CheckBox chbDifficult;

    private OpMountOnScrewsNoDisAssm opData;

    private int twoScrews; //Количество автоматов
    private int fourScrews; //Количество нагревателей
    private boolean difficult; //Сложность сборки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tf2Screws, this);
        new TFIntegerColored(tf4Screws, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOnScrewsNoDisAssm) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        twoScrews = IntegerParser.getValue(tf2Screws);
        fourScrews = IntegerParser.getValue(tf4Screws);
        difficult = chbDifficult.isSelected();

        collectOpData();
    }

    private void collectOpData(){
        opData.setTwoScrews(twoScrews);
        opData.setFourScrews(fourScrews);
        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountOnScrewsNoDisAssm opData = (OpMountOnScrewsNoDisAssm)data;

        twoScrews = opData.getTwoScrews();
        tf2Screws.setText(String.valueOf(twoScrews));

        fourScrews = opData.getFourScrews();
        tf4Screws.setText(String.valueOf(fourScrews));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Установка различных изделий с отверстиями для установки\n" +
                        "на монтажную панель на винты без разборки корпуса\n" +
                        "(прогонка резьбы учтена):" +
                        "\n" +
                        "\tна 2 винта %s мин/элемент.\n" +
                        "\tна 4 винта %s мин/элемент.\n" +
                        "\n" +
                        "Установка в стесненных условиях - коэффициент 1,3\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4,\n" +
                        "время отдыха Т отд = 6 и подготовительно-заключительное время Т пз = 2.9\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                MOUNT_ON_2_SCREWS_NO_DISASSM, MOUNT_ON_4_SCREWS_NO_DISASSM);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
