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
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.entities.ops.opAssembling.OpThermoInsulation;
import ru.wert.normic.enums.EMaterialMeasurement;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

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
    public void initViews(OpData opData) {

        new TFIntegerColored(tfHeight, this);
        new TFIntegerColored(tfWidth, this);
        new TFIntegerColored(tfDepth, this);
        new ChBox(chbxFront, this);
        new ChBox(chbxBack, this);
        new BXMaterialMeasurement().create(cmbxMeasurement, EMaterialMeasurement.M2, this);
        new BXThermoThickness().create(cmbxThickness, 50, this);
        new CmBx(cmbxThickness, this);
        new TFDoubleColored(tfPlusRatio, this);


    }

    @Override
    public void countNorm(OpData data) {
        opData = (OpThermoInsulation)data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getAssmTime();//результат в минутах

        tfOutlay.setText(String.format(DOUBLE_FORMAT, opData.getOutlay()));
        lblMeasurement.setText(opData.getMeasurement().getMeasure());

        setTimeMeasurement();
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

        opData.setPlusRatio(plusRatio);

    }

    @Override
    public void fillOpData(OpData data) {
        OpThermoInsulation opData = (OpThermoInsulation)data;

        tfHeight.setText(String.valueOf(opData.getHeight()));
        tfWidth.setText(String.valueOf(opData.getWidth()));
        tfDepth.setText(String.valueOf(opData.getDepth()));

        cmbxThickness.getSelectionModel().select(opData.getThickness());
        cmbxMeasurement.getSelectionModel().select(opData.getMeasurement());

        chbxFront.setSelected(opData.getFront());
        chbxBack.setSelected(opData.getBack());

        tfPlusRatio.setText(String.format(DOUBLE_FORMAT, opData.getPlusRatio()));
        lblMeasurement.setText(opData.getMeasurement().getMeasure());
    }

    @Override
    public String helpText() {
        return String.format("Расчет расхода и норм времени на монтаж термоизоляции ведется исходя из габаритов шкафа," +
                "где В - высота, Ш - ширина, Г - глубина шкафа.\n" +
                "При расчете можно вычесть фронтальную и заднюю стенки (снять галочки ФРОНТ и ЗАД)." +
                "Запас термоизоляции выбирается исходя из особенности конструкции и может достигать 30%% - коэффициент 1,3." +
                "Единицы измерения предназначены: м2 - для листовой термоизоляции типа фольгированного изолона," +
                "м3 - для толстой термоизоляции типа пеноплекса - выбираются самостоятельно." +
                "Норма времени на монтаж термоизоляции расчитывается из площади шкафа по формуле:" +
                "\t\t\tT монт = S шк х V монт," +
                "где V монт = %s мин/м2 - скорость монтажа термоизоляции.", INSULATION_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
