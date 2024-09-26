package ru.wert.normic.controllers.assembling;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpThermoInsulation;
import ru.wert.normic.enums.EMaterialMeasurement;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.INSULATION_SPEED;

public class PlateThermoInsulationController extends AbstractOpPlate {

    @FXML
    private TextField tfHeight;

    @FXML
    private TextField tfWidth;

    @FXML
    private TextField tfDepth;

    @FXML
    private ImageView ivHelp;

    @FXML
    private CheckBox chbxFront;

    @FXML
    private CheckBox chbxBack;

    @FXML
    private TextField tfPlusRatio;

    @FXML
    private ComboBox<EMaterialMeasurement> cmbxMeasurement;

    @FXML
    private ComboBox<Integer> cmbxThickness;

    @FXML
    private TextField tfOutlay;

    @FXML
    private Label lblMeasurement;

    @FXML
    private CheckBox chbxUseScotch;

    @FXML
    private TextField tfScotchOutlay;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfNormTime;

    @FXML
    private Label lblNormTimeMeasure;

    @FXML
    private ImageView ivDeleteOperation;

    private OpThermoInsulation opData;
    private double plusRatio; //Запас изоляцияонного материала
    private int height, width, depth; //габариты шкафа


    @Override//AbstractOpPlate
    public void initViews(OpData data) {
        OpThermoInsulation opData = (OpThermoInsulation)data;

        new TFIntegerColored(tfHeight, this);
        new TFIntegerColored(tfWidth, this);
        new TFIntegerColored(tfDepth, this);
        new ChBox(chbxFront, this);
        new ChBox(chbxBack, this);
        new BXMaterialMeasurement().create(cmbxMeasurement, opData.getMeasurement(), this);
        new BXThermoThickness().create(cmbxThickness, opData.getThickness(), this);
        new CmBx(cmbxThickness, this);
        new TFDoubleColored(tfPlusRatio, this);

        new ChBox(chbxUseScotch, this);

    }

    @Override
    public void countNorm(OpData data) {
        opData = (OpThermoInsulation)data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);

        tfOutlay.setText(String.format(DOUBLE_FORMAT, opData.getOutlay()));
        lblMeasurement.setText(opData.getMeasurement().getMeasure());

        tfScotchOutlay.setText(String.format(DOUBLE_FORMAT, opData.getScotchOutlay()));


    }


    @Override
    public void countInitialValues() {
        height = IntegerParser.getValue(tfHeight);
        width = IntegerParser.getValue(tfWidth);
        depth = IntegerParser.getValue(tfDepth);

        plusRatio = DoubleParser.getValue(tfPlusRatio);

        collectOpData();
    }

    private void collectOpData(){
        opData.setThickness(cmbxThickness.getValue());

        opData.setHeight(height);
        opData.setWidth(width);
        opData.setDepth(depth);

        opData.setFront(chbxFront.isSelected());
        opData.setBack(chbxBack.isSelected());

        opData.setMeasurement(cmbxMeasurement.getValue());
        opData.setThickness(cmbxThickness.getValue());

        opData.setPlusRatio(plusRatio);

        opData.setUseScotch(chbxUseScotch.isSelected());

    }

    @Override
    public void fillOpData(OpData data) {
        OpThermoInsulation opData = (OpThermoInsulation)data;

        tfHeight.setText(String.valueOf(opData.getHeight()));
        tfWidth.setText(String.valueOf(opData.getWidth()));
        tfDepth.setText(String.valueOf(opData.getDepth()));

        chbxFront.setSelected(opData.getFront());
        chbxBack.setSelected(opData.getBack());

        cmbxThickness.setValue(opData.getThickness());
        cmbxMeasurement.setValue(opData.getMeasurement());

        tfPlusRatio.setText(String.valueOf(opData.getPlusRatio()));
        lblMeasurement.setText(opData.getMeasurement().getMeasure());

        chbxUseScotch.setSelected(opData.isUseScotch());
    }

    @Override
    public String helpText() {
        return String.format("Расчет расхода и норм времени на монтаж термоизоляции ведется исходя из габаритов шкафа,\n" +
                "где В - высота, Ш - ширина, Г - глубина шкафа.\n" +
                "При расчете можно вычесть фронтальную и заднюю стенки (снять галочки ФРОНТ и ЗАД).\n" +
                "Запас термоизоляции выбирается исходя из особенности конструкции и может достигать 30%% - коэффициент 1,3.\n" +
                "Единицы измерения предназначены: м2 - для листовой термоизоляции типа фольгированного изолона,\n" +
                "м3 - для толстой термоизоляции типа пеноплекса - выбираются самостоятельно.\n" +
                "Норма времени на монтаж термоизоляции расчитывается из площади шкафа по формуле:\n" +
                "\t\t\tT монт = S шк х V монт,\n" +
                "где V монт = %s мин/м2 - скорость монтажа термоизоляции.\n\n", INSULATION_SPEED) +
                "Расход металлизированного скотча расчитывается по формуле :\n" +
                "\t\t\t((2В + 2Ш) * n + 4Г) / L, где\n" +
                "L = 50 - длина металлизированного скотча в рулоне, м;\n" +
                "n = 3 - количество контуров оклеивания скотчем, \n" +
                "n = 2 - если фронтальная стенка не учитывается\n";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
