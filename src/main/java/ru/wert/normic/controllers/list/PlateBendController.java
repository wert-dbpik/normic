package ru.wert.normic.controllers.list;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import ru.wert.normic.components.BXBendingTool;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.list.counters.OpBendingCounter;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.settings.NormConstants.BENDING_SERVICE_RATIO;
import static ru.wert.normic.settings.NormConstants.BENDING_SPEED;

/**
 * ГИБКА ЛИСТА НА ЛИСТОГИБЕ
 */
public class PlateBendController extends AbstractOpPlate {

    @FXML@Getter
    private ImageView ivHelp;

    @FXML
    private TextField tfBends;

    @FXML
    private TextField tfMen;

    @FXML
    private ComboBox<EBendingTool> cmbxBendingTool;

    @FXML
    private TextField tfNormTime;

    private OpBending opData;

    private int bends;
    private int men;
    private double toolRatio;

    @Override //AbstractOpPlate
    public void initViews(OpData opData){

        new BXBendingTool().create(cmbxBendingTool, ((OpBending)opData).getTool(), this);
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfBends, this);
        new TFIntegerColored(tfMen, this);
    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpBending)data;

        countInitialValues();

        currentNormTime = OpBendingCounter.count((OpBending) data).getMechTime();//результат в минутах

        setTimeMeasurement();
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        bends = IntegerParser.getValue(tfBends);
        men = IntegerParser.getValue(tfMen);
        toolRatio = cmbxBendingTool.getValue().getToolRatio();

        opData.setBends(bends);
        opData.setMen(men);
        opData.setTool(cmbxBendingTool.getValue());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpBending opData = (OpBending)data;

        bends = opData.getBends();
        tfBends.setText(String.valueOf(bends));

        men = opData.getMen();
        tfMen.setText(String.valueOf(men));

        if(opData.getTool() != null){
            toolRatio = opData.getTool().getToolRatio();
            cmbxBendingTool.setValue(opData.getTool());
        }
    }

    public String helpText() {
        return String.format("N гибов - число гибов;\n" +
                        "N человек - число человек выполняющих гибку (2 - для крупных деталей);\n" +
                        "Оборудование - коэффициент, учитывающий оборудование\n" +
                        "\t(универсал : К обор = 2, панелегиб : К обор = 1).\n\n" +
                        "Время гибки вычисляется по формуле:\n\n" +
                        "\t\t\tТгиб = N гибов х Vгиб х К обор х N человек х Кпз, мин\n" +
                        "где\n" +
                        "\tV гиб = %s скорость гибки, мин/гиб ;\n" +
                        "\tК пз = %s - коэффициент ПЗ времени.",
                BENDING_SPEED, BENDING_SERVICE_RATIO
                );
    }

    public Image helpImage() {
        return null;
    }
}
