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
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ МНОГОЖИЛЬНЫЙ 6 ММ
 */
public class Plate_CutCableHandlyMC6_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfMultiCoreCable6mm;

    @FXML
    private CheckBox chbDifficult;

    private OpCutCableHandlyMC6 opData;

    private int multiCoreCable6mm; //Многожильный провод Дн=6 мм
    private boolean difficult; //Резка проводов по месту, К = 1,2

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfMultiCoreCable6mm, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpCutCableHandlyMC6) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        multiCoreCable6mm = IntegerParser.getValue(tfMultiCoreCable6mm);

        difficult = chbDifficult.isSelected();
        
        collectOpData();
    }

    private void collectOpData(){
        opData.setMultiCoreCable6mm(multiCoreCable6mm);

        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutCableHandlyMC6 opData = (OpCutCableHandlyMC6)data;

        multiCoreCable6mm = opData.getMultiCoreCable6mm();
        tfMultiCoreCable6mm.setText(String.valueOf(multiCoreCable6mm));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Указывается количество резов. \nРезка кабеля и снятие изоляции вручную:\n" +
                        "\n" +
                        "\tМногожильный провод Дн=6 мм - %s мин/рез.\n" +
                        "\n" +
                        "Резка проводов по месту - коэффициент 1,2\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                CUT_CABLE_HANDLY_MC6);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
