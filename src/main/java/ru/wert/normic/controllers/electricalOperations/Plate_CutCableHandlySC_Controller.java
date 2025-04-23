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
import ru.wert.normic.entities.ops.electrical.OpCutCableHandlyMC6;
import ru.wert.normic.entities.ops.electrical.OpCutCableHandlySC;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ ОДНОЖИЛЬНЫЙ
 */
public class Plate_CutCableHandlySC_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfSingleCoreCable;

    @FXML
    private CheckBox chbDifficult;

    private OpCutCableHandlySC opData;

    private int singleCoreCable; //Одножильный провод
    private boolean difficult; //Резка проводов по месту, К = 1,2

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfSingleCoreCable, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpCutCableHandlySC) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        singleCoreCable = IntegerParser.getValue(tfSingleCoreCable);

        difficult = chbDifficult.isSelected();
        
        collectOpData();
    }

    private void collectOpData(){
        opData.setSingleCoreCable(singleCoreCable);

        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutCableHandlySC opData = (OpCutCableHandlySC)data;

        singleCoreCable = opData.getSingleCoreCable();
        tfSingleCoreCable.setText(String.valueOf(singleCoreCable));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Указывается количество резов. \nРезка кабеля и снятие изоляции вручную:\n" +
                        "\n" +
                        "\tОдножильный провод - %s мин/рез.\n" +
                        "\n" +
                        "Резка проводов по месту - коэффициент 1,2\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                CUTTING_SINGLE_CORE_CABLE);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
