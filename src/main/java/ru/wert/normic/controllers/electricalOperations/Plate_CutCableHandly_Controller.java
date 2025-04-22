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
import ru.wert.normic.entities.ops.electrical.OpCutCableHandly;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * ПОДКЛЮЧЕНИЕ ЭЛЕКТРОУСТРОЙСТВ
 */
public class Plate_CutCableHandly_Controller extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfMultiCoreCable6mm;

    @FXML
    private TextField tfMultiCoreCable11mm;

    @FXML
    private TextField tfSingleCoreCable;

    @FXML
    private CheckBox chbDifficult;

    private OpCutCableHandly opData;

    private int multiCoreCable6mm; //Многожильный провод Дн=6 мм
    private int multiCoreCable11mm; //Многожильный провод Дн=11..15 мм
    private int singleCoreCable; //Одножильный провод
    private boolean difficult; //Резка проводов по месту, К = 1,2

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfMultiCoreCable6mm, this);
        new TFIntegerColored(tfMultiCoreCable11mm, this);
        new TFIntegerColored(tfSingleCoreCable, this);
        new ChBox(chbDifficult, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpCutCableHandly) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        multiCoreCable6mm = IntegerParser.getValue(tfMultiCoreCable6mm);
        multiCoreCable11mm = IntegerParser.getValue(tfMultiCoreCable11mm);
        singleCoreCable = IntegerParser.getValue(tfSingleCoreCable);

        difficult = chbDifficult.isSelected();
        
        collectOpData();
    }

    private void collectOpData(){
        opData.setMultiCoreCable6mm(multiCoreCable6mm);
        opData.setMultiCoreCable11mm(multiCoreCable11mm);
        opData.setSingleCoreCable(singleCoreCable);

        opData.setDifficult(difficult);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutCableHandly opData = (OpCutCableHandly)data;

        multiCoreCable6mm = opData.getMultiCoreCable6mm();
        tfMultiCoreCable6mm.setText(String.valueOf(multiCoreCable6mm));

        multiCoreCable11mm = opData.getMultiCoreCable11mm();
        tfMultiCoreCable11mm.setText(String.valueOf(multiCoreCable11mm));

        singleCoreCable = opData.getSingleCoreCable();
        tfSingleCoreCable.setText(String.valueOf(singleCoreCable));

        difficult = opData.isDifficult();
        chbDifficult.setSelected(difficult);
    }

    @Override
    public String helpText() {
        return String.format("Резка кабеля и снятие изоляции вручную:\n" +
                        "\n" +
                        "\tМногожильный провод Дн=6 мм - %s мин/рез.\n" +
                        "\tМногожильный провод Дн=11..15 мм - %s мин/рез.\n" +
                        "\tОдножильный провод - %s мин/рез.\n" +
                        "\n" +
                        "Резка проводов по месту - коэффициент 1,2\n" +
                        "\n" +
                        "Окончательная формула учитывает время обслуживания Т обсл = 2,4%%,\n" +
                        "время отдыха Т отд = 6%% и подготовительно-заключительное время Т пз = 2.9%%\n" +
                        "в формуле:\n" +
                        "\n" +
                        "\t\tТ монт = Т оп + Т оп * (0,024 + 0.06) + Т оп * 0,029 / партия",

                CUTTING_MULTI_CORE_CABLE_6MM, CUTTING_MULTI_CORE_CABLE_11MM, CUTTING_SINGLE_CORE_CABLE);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
