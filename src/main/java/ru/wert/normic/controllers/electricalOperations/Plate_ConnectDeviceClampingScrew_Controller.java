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
import ru.wert.normic.entities.ops.electrical.OpConnectDeviceClampingScrew;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ НА ЗАЖИМНОЙ ВИНТ
 */
public class Plate_ConnectDeviceClampingScrew_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfClampingScrew;

    @FXML
    private CheckBox chbDifficult;

    private OpConnectDeviceClampingScrew opData;

    private String name; //Наименование
    private int clampingScrew = 0; //Зажимной винт
    private boolean difficult; //Сложность сборки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfClampingScrew, this);
        new ChBox(chbDifficult, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpConnectDeviceClampingScrew) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        name = tfName.getText().trim();
        clampingScrew = IntegerParser.getValue(tfClampingScrew);

        difficult = chbDifficult.isSelected();
        
        collectOpData();
    }

    private void collectOpData(){
        opData.setName(name);
        opData.setClampingScrew(clampingScrew);
        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpConnectDeviceClampingScrew opData = (OpConnectDeviceClampingScrew)data;

        name = opData.getName();
        tfName.setText(String.valueOf(name));

        clampingScrew = opData.getClampingScrew();
        tfClampingScrew.setText(String.valueOf(clampingScrew));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Подключение электроустройства на зажимной винт за %s мин/контакт.\n" +
                        "\n" +
                        "Установка в стесненных условиях - коэффициент 1,3\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                CONNECT_DEVICE_CLAMPING_SCREW);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
