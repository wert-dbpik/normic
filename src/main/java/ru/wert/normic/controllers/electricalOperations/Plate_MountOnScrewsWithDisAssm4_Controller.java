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
import ru.wert.normic.entities.ops.electrical.OpMountOnScrewsWithDisAssm4;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_2_SCREWS_NO_DISASSM;
import static ru.wert.normic.settings.NormConstants.MOUNT_ON_4_SCREWS_NO_DISASSM;

/**
 * УСТАНОВКА НА ВИНТЫ С РАЗБОРКОЙ КОРПУСА
 */
public class Plate_MountOnScrewsWithDisAssm4_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tf4Screws;

    @FXML
    private CheckBox chbDifficult;

    private OpMountOnScrewsWithDisAssm4 opData;

    private String name; //Наименование
    private int fourScrews; //Количество нагревателей
    private boolean difficult; //Сложность сборки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tf4Screws, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOnScrewsWithDisAssm4) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        name = tfName.getText().trim();
        fourScrews = IntegerParser.getValue(tf4Screws);
        difficult = chbDifficult.isSelected();
        
        collectOpData();
    }

    private void collectOpData(){
        opData.setName(name);
        opData.setFourScrews(fourScrews);
        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountOnScrewsWithDisAssm4 opData = (OpMountOnScrewsWithDisAssm4)data;

        name = opData.getName();
        tfName.setText(String.valueOf(name));

        fourScrews = opData.getFourScrews();
        tf4Screws.setText(String.valueOf(fourScrews));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Установка розеток, извещателей пожарных и пр. изделий,\n" +
                        "на винты, при установке которых требуется снятие крышки\n" +
                        "(прогонка резьбы учтена):" +
                        "\n" +
                        "\tна 4 винта %s мин/элемент.\n" +
                        "\n" +
                        "Установка в стесненных условиях - коэффициент 1,3\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                MOUNT_ON_4_SCREWS_NO_DISASSM);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
