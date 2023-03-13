package ru.wert.normic.controllers.assembling;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.BXSealersWidth;
import ru.wert.normic.components.CmBx;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.enums.ESealersWidth;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;
import static ru.wert.normic.entities.settings.AppSettings.OTHERS_SPEED;

/**
 * НАЛИВКА УПЛОТНИТЕЛЯ
 */
public class PlateLevelingSealerController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private ComboBox<ESealersWidth> cmbxSealerWidth;

    @FXML
    private TextField tfA;

    @FXML
    private TextField tfB;

    @FXML
    private TextField tfCompA;

    @FXML
    private TextField tfCompB;

    @FXML
    private TextField tfNormTime;

    private int paramA; //Размер А
    private int paramB;//Размер Б
    private double perimeter; //

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpLevelingSealer opData = (OpLevelingSealer)data;

        new BXSealersWidth().create(cmbxSealerWidth, opData.getSealersWidth(), this);
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfA, this);
        new TFIntegerColored(tfB, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new CmBx(cmbxSealerWidth, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpLevelingSealer opData = (OpLevelingSealer)data;

        countInitialValues();

        double time;
        time =  perimeter * LEVELING_SPEED +
                Math.ceil(perimeter / 6.0) * LEVELING_PREPARED_TIME;  //мин

        System.out.println(Math.ceil(perimeter / 6.0) );

        if(perimeter == 0) time = 0.0;
        else {
            tfCompA.setText(String.format(DOUBLE_FORMAT, perimeter * cmbxSealerWidth.getValue().getCompA()));
            tfCompB.setText(String.format(DOUBLE_FORMAT, perimeter * cmbxSealerWidth.getValue().getCompB()));
        }

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        paramA = IntegerParser.getValue(tfA);
        paramB = IntegerParser.getValue(tfB);
        perimeter = (paramA == 0 || paramB == 0) ?
                (paramA + paramB)  * MM_TO_M :
                2 * (paramA + paramB) * MM_TO_M;
    }

    private void collectOpData(OpLevelingSealer opData){
        opData.setCompA(tfCompA.getText().isEmpty() ? 0.0 : Double.parseDouble(tfCompA.getText().replace(",", ".")));
        opData.setCompB(tfCompB.getText().isEmpty() ? 0.0 : Double.parseDouble(tfCompB.getText().replace(",", ".")));

        opData.setSealersWidth(cmbxSealerWidth.getValue());
        opData.setParamA(paramA);
        opData.setParamB(paramB);

        opData.setAssmTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLevelingSealer opData = (OpLevelingSealer)data;

        cmbxSealerWidth.setValue(opData.getSealersWidth());

        paramA = opData.getParamA();
        tfA.setText(String.valueOf(paramA));

        paramB = opData.getParamB();
        tfB.setText(String.valueOf(paramB));

    }

    @Override
    public String helpText() {
        return String.format("W пр - ширина наливного профиля, мм.\n" +
                        "A и B стороны для замкнутого прямоугольного контура, указываются в мм. \n" +
                        "\tСуммарная длина контура расчитывается автоматически. \n" +
                        "\tЕсли контур не замкнут или вообще нестандартный, то общую длину контура можно \n" +
                        "\tуказать для одного из параметров, второй параметр должен быть равен 0\n" +
                        "\n" +
                        "Норма времени нанесения уплотнителя вычисляется по формуле:\n" +
                        "\n" +
                        "T упл = P * V упл + T пз, \n" +
                        "где\n" +
                        "\tP - суммарная длина наносимого контура, м;\n" +
                        "\tV упл = %s - скорость нанесения уплотнителя, мин/м;\n" +
                        "\tT пз = %s - ПЗ время на каждые 6 метров нанесенного уплотнителя. \n" +
                        "\n" +
                        "Расход компонента полиэфирного А вычисляется по формуле:\n" +
                        "\tКомп А = P * A расх, кг. \n" +
                        "\n" +
                        "Расход компонента  B вычисляется по формуле:\n" +
                        "\tКомп B = P * B расх, кг. \n",

                LEVELING_SPEED, LEVELING_PREPARED_TIME);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
