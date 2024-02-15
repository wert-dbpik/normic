package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.locksmith.counters.OpDrillingByMarkingCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opLocksmith.OpDrillingByMarking;
import ru.wert.normic.utils.IntegerParser;

/**
 * СВЕРЛЕНИЕ ОТВЕРСТИЙ ПО РАЗМЕТКЕ
 */
public class PlateDrillingByMarkingController extends AbstractOpPlate {

    @FXML
    private TextField tfDiameter;

    @FXML
    private TextField tfHoles;

    @FXML
    private TextField tfDepth;

    @FXML
    private TextField tfLength;

    private String initStyle;

    private OpDrillingByMarking opData;

    private int diameter; //диаметр отверстия
    private int depth; //глубина отверстия
    private int holes; //Количество отверстий
    private int length; //Максимальная длина или шаг измерения

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        initStyle = tfLength.getStyle(); //Сохраняем исходный стиль

        new TFIntegerColored(tfDiameter, this);
        new TFIntegerColored(tfDepth, this);
        new TFIntegerColored(tfHoles, this);
        new TFIntegerColored(tfLength, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            prevFormController.countSumNormTimeByShops();
        });
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpDrillingByMarking) data;

        countInitialValues();

        currentNormTime = opData.getNormCounter().count(data).getMechTime();//результат в минутах

        setTimeMeasurement();
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = IntegerParser.getValue(tfDiameter);
        if(diameter > OpDrillingByMarkingCounter.EDrillingOnRadial.values()[OpDrillingByMarkingCounter.EDrillingOnRadial.values().length-1].diameter) {
            tfDiameter.setStyle("-fx-border-color: #FF5555");
            diameter = 25;
        } else
            tfDiameter.setStyle(initStyle);

        depth = IntegerParser.getValue(tfDepth);
        holes = IntegerParser.getValue(tfHoles);
        length = IntegerParser.getValue(tfLength);

        collectOpData();
    }


    private void collectOpData(){
        opData.setDiameter(diameter);
        opData.setDepth(depth);
        opData.setHoles(holes);
        opData.setLength(length);

    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpDrillingByMarking opData = (OpDrillingByMarking)data;

        diameter = opData.getDiameter();
        tfDiameter.setText(String.valueOf(diameter));

        depth = opData.getDepth();
        tfDepth.setText(String.valueOf(depth));

        holes = opData.getHoles();
        tfHoles.setText(String.valueOf(holes));

        length = opData.getLength();
        tfLength.setText(String.valueOf(length));

    }

    @Override
    public String helpText() {
        return "Сверление отверстий по разметке производится ручным инструментом.\n" +
                "D отв - диаметр группы отверстий (максимальный), мм; \n" +
                "T гл - глубина сверления или толщина материала, при сверлении трубы насквозь - \n" +
                "\t\tтолщина двух стенок, мм;\n" +
                "N отв - число отверстий в группе\n" +
                "L max.дл./шаг - максимальная отмеряемая длина или шаг между отверстиями, мм\n\n" +
                "Норма времени на сверление отверстий вычисляется по формуле:\n\n" +
                "\t\t\tT сверл = T изм * (N отв + 1) + N отв * V сверл,\n" +
                "где\n" +
                "\tN отв - число отверстий; \n" +
                "\tT изм - время на проведение измерений при разметке; \n" +
                "\tV сверл - скорость сверления зависит от диаметра и толщины, мин/отв";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
