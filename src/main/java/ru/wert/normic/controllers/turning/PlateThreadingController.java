package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers.turning.counters.OpLatheDrillingCounter;
import ru.wert.normic.controllers.turning.counters.OpLatheThreadingCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opTurning.OpLatheDrilling;
import ru.wert.normic.entities.ops.opTurning.OpLatheThreading;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;

/**
 * НАРЕЗАНИЕ РЕЗЬБЫ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateThreadingController extends AbstractOpPlate {

    @FXML
    private TextField tfDiameter;

    @FXML
    private TextField tfLength;

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

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheThreading) data;

        countInitialValues();

        currentNormTime = OpLatheThreadingCounter.count((OpLatheThreading) data).getMechTime();//результат в минутах

        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = ((FormDetailController) formController).getCmbxMaterial().getValue().getParamS();
        turningDiameter = IntegerParser.getValue(tfDiameter);
        if(turningDiameter > diameter)
            tfDiameter.setStyle("-fx-border-color: #FF5555");
        else
            tfDiameter.setStyle(initStyle);

        paramA = ((FormDetailController) formController).getMatPatchController().getParamA();
        length = IntegerParser.getValue(tfLength);
        if(length > paramA)
            tfLength.setStyle("-fx-border-color: #FF5555");
        else
            tfLength.setStyle(initStyle);

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
