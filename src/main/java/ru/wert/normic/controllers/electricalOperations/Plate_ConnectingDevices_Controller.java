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
import ru.wert.normic.entities.ops.electrical.OpConnectingDevices;
import ru.wert.normic.entities.ops.electrical.OpMountOnScrewsWithDisAssm;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ
 */
public class Plate_ConnectingDevices_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfMortiseContact;

    @FXML
    private TextField tfSpringClamp;

    @FXML
    private TextField tfClampingScrew;

    @FXML
    private TextField tfVshg;

    @FXML
    private CheckBox chbDifficult;

    private OpConnectingDevices opData;

    private int mortiseContact; //Врезной контакт (без снятия изоляции)
    private int springClamp; //Пружинный зажим
    private int clampingScrew = 0; //Зажимной винт
    private int vshg = 0; //ВШГ, наконечник кольцо
    private boolean difficult; //Сложность сборки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfMortiseContact, this);
        new TFIntegerColored(tfSpringClamp, this);
        new TFIntegerColored(tfClampingScrew, this);
        new TFIntegerColored(tfVshg, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpConnectingDevices) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        mortiseContact = IntegerParser.getValue(tfMortiseContact);
        springClamp = IntegerParser.getValue(tfSpringClamp);
        clampingScrew = IntegerParser.getValue(tfClampingScrew);
        vshg = IntegerParser.getValue(tfVshg);

        difficult = chbDifficult.isSelected();
        
        collectOpData();
    }

    private void collectOpData(){
        opData.setMortiseContact(mortiseContact);
        opData.setSpringClamp(springClamp);
        opData.setClampingScrew(clampingScrew);
        opData.setVshg(vshg);

        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpConnectingDevices opData = (OpConnectingDevices)data;

        mortiseContact = opData.getMortiseContact();
        tfMortiseContact.setText(String.valueOf(mortiseContact));

        springClamp = opData.getSpringClamp();
        tfSpringClamp.setText(String.valueOf(springClamp));

        clampingScrew = opData.getClampingScrew();
        tfClampingScrew.setText(String.valueOf(clampingScrew));

        vshg = opData.getVshg();
        tfVshg.setText(String.valueOf(vshg));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Для подключение электроприборов используются:\n" +
                        "\n" +
                        "\tВрезной контакт (без снятия изоляции) - %s мин/контакт.\n" +
                        "\tПружинный зажим - %s мин/контакт.\n" +
                        "\tЗажимной винт - %s мин/контакт.\n" +
                        "\tВШГ, (наконечник кольцо) - %s мин/контакт.\n" +
                        "\n" +
                        "Установка в стесненных условиях - коэффициент 1,3\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                CONNECTING_DEVICES_WITH_MORTISE_CONTACT, CONNECTING_DEVICES_WITH_SPRING_CLAMP, CONNECTING_DEVICES_WITH_CLAMPING_SCREW, CONNECTING_DEVICES_WITH_VSHG);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
