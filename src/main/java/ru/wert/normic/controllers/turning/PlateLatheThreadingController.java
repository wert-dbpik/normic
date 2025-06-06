package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opTurning.OpLatheThreading;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

/**
 * НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateLatheThreadingController extends AbstractOpPlate {

    @FXML
    private TextField tfDiameter;

    @FXML
    private TextField tfLength;

    @FXML
    private TextField tfNormTime;


    private OpLatheThreading opData;

    private String initStyle;
    private double diameter; //Диаметр прутка
    private int turningDiameter; //Диаметр обработки
    private int paramA; //Длина заготовки
    private int length; //Глубина  точения

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        initStyle = tfDiameter.getStyle(); //Сохраняем исходный стиль

        new TFIntegerColored(tfDiameter, this);
        new TFIntegerColored(tfLength, this);

        new TFNormTime(tfNormTime, prevFormController);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheThreading) data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = ((FormDetailController) prevFormController).getCmbxMaterial().getValue().getParamS();
        turningDiameter = IntegerParser.getValue(tfDiameter);
        if(turningDiameter > diameter)
            tfDiameter.setStyle("-fx-border-color: #FF5555");
        else
            tfDiameter.setStyle(initStyle);

        paramA = ((FormDetailController) prevFormController).getMatPatchController().getParamA();
        length = IntegerParser.getValue(tfLength);
        if(length > paramA)
            tfLength.setStyle("-fx-border-color: #FF5555");
        else
            tfLength.setStyle(initStyle);

        collectOpData();
    }

    private void collectOpData(){
        opData.setDiameter(turningDiameter);
        opData.setLength(length);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLatheThreading opData = (OpLatheThreading)data;

        turningDiameter = opData.getDiameter();
        tfDiameter.setText(String.valueOf(turningDiameter));

        length = opData.getLength();
        tfLength.setText(String.valueOf(length));

    }

    @Override
    public String helpText() {
        return "Норма времени на нарезание наружной резьбы берется из таблиц стандартных норм \n" +
                "в зависимости от диаметра обработки D (мм) и длины резания L(мм), мин.";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
