package ru.wert.normic.controllers.welding;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.welding.counters.OpWeldContinuousCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opWelding.OpWeldContinuous;
import ru.wert.normic.enums.EPartBigness;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.settings.NormConstants.WELDING_SPEED;

/**
 * СВАРКА ТЕПРЕРЫВНЫМ ШВОМ
 */
public class PlateWeldContinuousController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfSeamLength;

    @FXML
    private CheckBox chbxStripping;

    @FXML
    private CheckBox chbxPreEnterSeams;

    @FXML
    private TextField tfSeams;

    @FXML
    private TextField tfMen;

    @FXML
    private TextField tfConnectionLength;

    @FXML
    private TextField tfStep;

    @FXML
    private ComboBox<EPartBigness> cmbxPartBigness;

    @FXML
    private TextField tfNormTime;

    private OpWeldContinuous opData;

    private String name; //наименование
    private int seamLength; //Длина шва
    private int seams; //Количество швов заданное пользователем
    private int men; //Число человек, работающих над операцией
    private boolean stripping; //Использовать зачистку
    private int connectionLength; //Длина сединения на которую расчитывается количество точек
    private int step; //шаг точек

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpWeldContinuous opData = (OpWeldContinuous)data;

        tfSeams.disableProperty().bind(chbxPreEnterSeams.selectedProperty().not());
        tfConnectionLength.disableProperty().bind(chbxPreEnterSeams.selectedProperty());
        tfStep.disableProperty().bind(chbxPreEnterSeams.selectedProperty());

        new TfString(tfName, this);
        new BXPartBigness().create(cmbxPartBigness, opData.getPartBigness(), this);
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfSeamLength, this);
        new TFIntegerColored(tfSeams, this);
        new TFIntegerColored(tfMen, this);
        new TFIntegerColored(tfConnectionLength, this);
        new TFIntegerColored(tfStep, this);
        new ChBox(chbxPreEnterSeams, this);
        new ChBox(chbxStripping, this);
        new CmBx(cmbxPartBigness, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
       opData = (OpWeldContinuous)data;

        countInitialValues();

        currentNormTime = OpWeldContinuousCounter.count((OpWeldContinuous) data).getMechTime();//результат в минутах

        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        name = tfName.getText().trim();
        seamLength = IntegerParser.getValue(tfSeamLength);
        seams = IntegerParser.getValue(tfSeams);
        men = IntegerParser.getValue(tfMen);
        connectionLength = IntegerParser.getValue(tfConnectionLength);
        step = IntegerParser.getValue(tfStep);
        stripping = chbxStripping.isSelected();

        collectOpData();
    }


    private void collectOpData(){
        opData.setName(name);
        opData.setSeamLength(seamLength);
        opData.setPartBigness(cmbxPartBigness.getValue());
        opData.setMen(men);
        opData.setStripping(stripping);
        opData.setPreEnterSeams(chbxPreEnterSeams.isSelected());
        opData.setSeams(seams);
        opData.setConnectionLength(connectionLength);
        opData.setStep(step);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpWeldContinuous opData = (OpWeldContinuous)data;

        name = opData.getName();
        tfName.setText(name);

        seamLength = opData.getSeamLength();
        tfSeamLength.setText(String.valueOf(seamLength));

        cmbxPartBigness.setValue(opData.getPartBigness());

        men = opData.getMen();
        tfMen.setText(String.valueOf(men));

        stripping = opData.isStripping();
        chbxStripping.setSelected(stripping);

        chbxPreEnterSeams.setSelected(opData.isPreEnterSeams());

        seams = opData.getSeams();
        tfSeams.setText(String.valueOf(seams));

        connectionLength = opData.getConnectionLength();
        tfConnectionLength.setText(String.valueOf(connectionLength));

        step = opData.getStep();
        tfStep.setText(String.valueOf(step));

    }

    @Override
    public String helpText() {
        return String.format("Возможны два варианта указания данных для рассчета.\n\n" +
                        "1 вариант для сплошных швов - указать L шва и N швов, где\n" +
                        "\tL шва - длина непрерывного шва, мм.\n" +
                        "\tN швов - количество непрерывных швов, шт;\n\n" +
                        "2 вариант для прерывистого шва с шагом - указать L шва, L соед и L шаг, где\n" +
                        "\tL соед - длина соединенения с прерывным швом, мм;\n" +
                        "\tL шаг - шаг прерывистого шва, мм.\n\n" +
                        "Остальные поля:\n" +
                        "Габаритность сборки - от нее зависит ПЗ время;\n" +
                        "N человек - количество работников, задействованных в операции,\n" +
                        "\tне забываем о слесаре - подержать, помочь перевернуть - это к нему.\n" +
                        "Зачистка - для видовых швов обычно применяется зачистка.\n\n" +
                        "Норма времени вычисляется по формуле:\n\n" +
                        "\t\t\tT св.непр.= N человек x L sum x V св + T зачистки, мин\n" +
                        "где\n\n" +
                        "\tL sum - сумма длин всех швов, м;\n" +
                        "\tV св = %s - скорость сварки, мин/м;\n" +
                        "\tT зачистки - время зачистки дискретно зависит от L sum.:\n" +
                        "\t\tL sum < 0.1        ->  T зачистки = 0.5 мин; \n" +
                        "\t\t0.1 <= L sum < 0.5 ->  T зачистки = 1.8 мин; \n" +
                        "\t\t0.5 <= L sum < 1.0 ->  T зачистки = 3.22 мин; \n" +
                        "\t\tL sum > 1000       ->  T зачистки = 3.22 мин x L sum.",
                WELDING_SPEED);

    }

    @Override
    public Image helpImage() {
        return null;
    }
}
