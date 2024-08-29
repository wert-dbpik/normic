package ru.wert.normic.controllers.listOperations;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.BXBendingTool;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.BATCHNESS;
import static ru.wert.normic.settings.NormConstants.BENDING_SERVICE_RATIO;
import static ru.wert.normic.settings.NormConstants.BENDING_SPEED;

/**
 * ГИБКА ЛИСТА НА ЛИСТОГИБЕ
 */
public class PlateBendController extends AbstractOpPlate {

    @FXML@Getter
    private ImageView ivHelp;

    @FXML
    private CheckBox chDifficulty;

    @FXML
    private TextField tfBends;

    @FXML
    private TextField tfMen;

    @FXML
    private ComboBox<EBendingTool> cmbxBendingTool;

    @FXML
    private TextField tfNormTime;

    @FXML
    private VBox vbDifficultyness;

    private OpBending opData;

    private int bends;
    private int men;
    private double toolRatio;

    @Override //AbstractOpPlate
    public void initViews(OpData opData){

        new ChBox(chDifficulty, this);
        new BXBendingTool().create(cmbxBendingTool, ((OpBending)opData).getTool(), this);
        new TFNormTime(tfNormTime, prevFormController);
        new TFIntegerColored(tfBends, this);
        new TFIntegerColored(tfMen, this);

        vbDifficultyness.setVisible(BATCHNESS.get());
    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpBending)data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

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

        collectOpData();
    }

    private void collectOpData(){
        opData.setDifficult(chDifficulty.isSelected());
        opData.setBends(bends);
        opData.setMen(men);
        opData.setTool(cmbxBendingTool.getValue());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpBending opData = (OpBending)data;

        chDifficulty.setSelected(opData.isDifficult());

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
                        "Время гибки вычисляется по формулам:\n\n" +
                        "\t\t\tТоп = N гибов х Vгиб х К обор х N человек х Кобсл, мин\n" +
                        "где\n" +
                        "\tV гиб = %s скорость гибки, мин/гиб ;\n" +
                        "\tК пз = %s - коэффициент обслуживания;\n" +
                        "\t\t\tТшт = Топ + Tсл / партия\n" +
                        "где\n" +
                        "\tTсл - 3мин - для простых деталей, 12 мин - для сложных;\n" +
                        "\tпартия - число деталей в партии",
                BENDING_SPEED, BENDING_SERVICE_RATIO
                );
    }

    public Image helpImage() {
        return null;
    }
}
