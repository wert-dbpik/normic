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
import ru.wert.normic.entities.ops.electrical.OpMountOnScrewsWithDisAssm2;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_SCREWS_NO_DISASSM_2;

/**
 * УСТАНОВКА НА ВИНТЫ С РАЗБОРКОЙ КОРПУСА
 */
public class Plate_MountOnScrewsWithDisAssm2_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tf2Screws;

    @FXML
    private CheckBox chbDifficult;

    private OpMountOnScrewsWithDisAssm2 opData;

    private String name; //Наименование
    private int twoScrews; //Количество автоматов
    private boolean difficult; //Сложность сборки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tf2Screws, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOnScrewsWithDisAssm2) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        name = tfName.getText().trim();
        twoScrews = IntegerParser.getValue(tf2Screws);
        difficult = chbDifficult.isSelected();
        
        collectOpData();
    }

    private void collectOpData(){
        opData.setName(name);
        opData.setTwoScrews(twoScrews);
        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountOnScrewsWithDisAssm2 opData = (OpMountOnScrewsWithDisAssm2)data;

        name = opData.getName();
        tfName.setText(String.valueOf(name));

        twoScrews = opData.getTwoScrews();
        tf2Screws.setText(String.valueOf(twoScrews));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Установка розеток, извещателей пожарных и пр. изделий,\n" +
                        "на винты, при установке которых требуется снятие крышки\n" +
                        "(прогонка резьбы учтена):" +
                        "\n" +
                        "\tна 2 винта %s мин/элемент.\n" +
                        "\n" +
                        "Установка в стесненных условиях - коэффициент 1,3\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                MOUNT_ON_SCREWS_NO_DISASSM_2);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
