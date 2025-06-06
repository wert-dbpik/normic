package ru.wert.normic.controllers.assembling;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpLevelingSealer;
import ru.wert.normic.enums.ESealersWidth;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.LEVELING_PREPARED_TIME;
import static ru.wert.normic.settings.NormConstants.LEVELING_SPEED;

/**
 * НАЛИВКА УПЛОТНИТЕЛЯ
 */
public class PlateLevelingSealerController extends AbstractOpPlate {

    @FXML
    private TextField tfName;

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

    private OpLevelingSealer opData;

    private String name; //Наименование
    private int paramA; //Размер А
    private int paramB;//Размер Б

    @Override //AbstractOpPlate
    public void initViews(OpData opData){

        new TfString(tfName, this);
        new BXSealersWidth().create(cmbxSealerWidth, ((OpLevelingSealer)opData).getSealersWidth(), this);
        new TFNormTime(tfNormTime, prevFormController);
        new TFIntegerColored(tfA, this);
        new TFIntegerColored(tfB, this);
        new CmBx(cmbxSealerWidth, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLevelingSealer)data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);

        tfCompA.setText(String.format(DOUBLE_FORMAT, opData.getCompA()));
        tfCompB.setText(String.format(DOUBLE_FORMAT, opData.getCompB()));
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        name = tfName.getText().trim();
        paramA = IntegerParser.getValue(tfA);
        paramB = IntegerParser.getValue(tfB);

        collectOpData();
    }

    private void collectOpData(){
        opData.setName(name);
        opData.setCompA(tfCompA.getText().isEmpty() ? 0.0 : Double.parseDouble(tfCompA.getText().replace(",", ".")));
        opData.setCompB(tfCompB.getText().isEmpty() ? 0.0 : Double.parseDouble(tfCompB.getText().replace(",", ".")));
        opData.setSealersWidth(cmbxSealerWidth.getValue());
        opData.setParamA(paramA);
        opData.setParamB(paramB);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLevelingSealer opData = (OpLevelingSealer)data;

        name = opData.getName();
        tfName.setText(name);

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
                        "\tСуммарная длина контура рассчитывается автоматически. \n" +
                        "\tЕсли контур не замкнут или вообще нестандартный, то общую длину контура можно \n" +
                        "\tуказать для одного из параметров, второй параметр должен быть равен 0\n\n" +
                        "Норма времени нанесения уплотнителя вычисляется по формуле:\n\n" +
                        "\t\t\tT упл = P * V упл + T пз, мин \n" +
                        "где\n" +
                        "\tP - суммарная длина наносимого контура, м;\n" +
                        "\tV упл = %s - скорость нанесения уплотнителя, мин/м;\n" +
                        "\tT пз = %s - ПЗ время на каждые 6 метров нанесенного уплотнителя. \n\n" +
                        "Расход компонента полиэфирного А вычисляется по формуле:\n\n" +
                        "\t\t\tКомп А = P * A расх, кг. \n\n" +
                        "Расход компонента  B вычисляется по формуле:\n\n" +
                        "\t\t\tКомп B = P * B расх, кг. \n",

                LEVELING_SPEED, LEVELING_PREPARED_TIME);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
