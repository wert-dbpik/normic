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
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.BATCHNESS;
import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.BENDING_SERVICE_RATIO;
import static ru.wert.normic.settings.NormConstants.UNIVERSAL_BENDING_SPEED;

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
    private TextField tfTurns;

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
    private int turns;
    private int men;
    private double toolRatio;

    @Override //AbstractOpPlate
    public void initViews(OpData opData){

        new ChBox(chDifficulty, this);
        new BXBendingTool().create(cmbxBendingTool, ((OpBending)opData).getTool(), this);
        new TFNormTime(tfNormTime, prevFormController);
        new TFIntegerColored(tfBends, this);
        new TFIntegerColored(tfTurns, this);
        new TFIntegerColored(tfMen, this);

        vbDifficultyness.setVisible(BATCHNESS.get());
    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpBending)data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        bends = IntegerParser.getValue(tfBends);

        turns = IntegerParser.getValue(tfTurns);
        tfTurns.disableProperty().bind(cmbxBendingTool.valueProperty().isEqualTo(EBendingTool.UNIVERSAL));

        men = IntegerParser.getValue(tfMen);
        tfMen.disableProperty().bind(cmbxBendingTool.valueProperty().isEqualTo(EBendingTool.PANELEGIB));

        toolRatio = cmbxBendingTool.getValue().getToolRatio();
        chDifficulty.disableProperty().bind(cmbxBendingTool.valueProperty().isEqualTo(EBendingTool.PANELEGIB));

        collectOpData();
    }

    private void collectOpData(){
        opData.setDifficult(chDifficulty.isSelected());
        opData.setBends(bends);
        opData.setTurns(turns);
        opData.setMen(men);
        opData.setTool(cmbxBendingTool.getValue());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpBending opData = (OpBending)data;

        chDifficulty.setSelected(opData.isDifficult());

        bends = opData.getBends();
        tfBends.setText(String.valueOf(bends));

        turns = opData.getTurns();
        tfTurns.setText(String.valueOf(turns));

        men = opData.getMen();
        tfMen.setText(String.valueOf(men));

        if(opData.getTool() != null){
            toolRatio = opData.getTool().getToolRatio();
            cmbxBendingTool.setValue(opData.getTool());
        }
    }

    public String helpText() {
        return String.format(
                "Расчет нормы времени зависит от оборудования.\n" +
                        "Для гибки на универсальном оборудовании формуладля оперативного времени:.\n" +
                        "\t\t\tТоп = N гибов х Vгиб х N человек х Кобсл , мин\n" +
                        "где\n" +
                        "\tN гибов - число гибов;\n" +
                        "\tN человек - число человек выполняющих гибку (2 - для крупных деталей);\n" +
                        "\tV гиб = %s скорость гибки, мин/гиб ;\n" +
                        "\tКобсл = %s - коэффициент обслуживания;\n" +

                        "Окончательно норма времени вычисляется как:.\n" +
                        "\t\t\tТшт = Топ + Тпз / партия, мин\n" +
                        "где\n" +

                        "\tТпз - время зависит от сложности детали \n" +
                        "\t\t(12 мин для сложных, 3мин - для простых);\n\n" +
                        "\t\tпартия - число деталей в партии. \n" +
                        "Расчет оперативного времени на панелегибе:\n" +
                        "\t\t\tТоп = N гибов х Vгиб +  N поворот х Vповорот , мин + Твсп, мин\n" +
                        "где\n" +
                        "\tVгиб - скорость гибки на панелегибе (4с/гиб);\n" +
                        "\tN поворот - число поворотов детали на станке (3 поворота для гибки с 3х сторон);\n" +
                        "\tVповорот - скорость этих поворотов (8с/поворот);\n" +
                        "\tТвсп - вспомогательное время (1.28 мин).\n" +
                        "Окончательно норма времени вычисляется как:\n" +
                        "\t\t\tТшт = Топ + Топ х Кобсл + Топ х Котд + Тпз / партия, мин,\n" +
                        "где\n" +
                        "\tКобсл - коэф. обслуживания (0.04);\n" +
                        "\tКотд - коэф. отдыха (0.07);\n",
                UNIVERSAL_BENDING_SPEED, BENDING_SERVICE_RATIO
        );
    }

    public Image helpImage() {
        return null;
    }
}
