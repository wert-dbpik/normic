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
import ru.wert.normic.entities.ops.electrical.OpCutCableHandlyMC15;
import ru.wert.normic.entities.ops.electrical.OpCutCableHandlyMC6;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * РЕЗКА КАБЕЛЯ И СНЯТИЕ ИЗОЛЯЦИИ ВРУЧНУЮ МНОГОЖИЛЬНЫЙ 11-15 ММ
 */
public class Plate_CutCableHandlyMC15_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfMultiCoreCable11mm;

    @FXML
    private CheckBox chbDifficult;

    private OpCutCableHandlyMC15 opData;

    private int multiCoreCable11mm; //Многожильный провод Дн=11..15 мм
    private boolean difficult; //Резка проводов по месту, К = 1,2

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfMultiCoreCable11mm, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpCutCableHandlyMC15) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        multiCoreCable11mm = IntegerParser.getValue(tfMultiCoreCable11mm);

        difficult = chbDifficult.isSelected();
        
        collectOpData();
    }

    private void collectOpData(){
        opData.setMultiCoreCable11mm(multiCoreCable11mm);

        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutCableHandlyMC15 opData = (OpCutCableHandlyMC15)data;

        multiCoreCable11mm = opData.getMultiCoreCable11mm();
        tfMultiCoreCable11mm.setText(String.valueOf(multiCoreCable11mm));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Указывается количество резов. \nРезка кабеля и снятие изоляции вручную:\n" +
                        "\n" +
                        "\tМногожильный провод Дн=11..15 мм - %s мин/рез.\n" +
                        "\n" +
                        "Резка проводов по месту - коэффициент 1,2\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                CUTTING_MULTI_CORE_CABLE_11MM);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
