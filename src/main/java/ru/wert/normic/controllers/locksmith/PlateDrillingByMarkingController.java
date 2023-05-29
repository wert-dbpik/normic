package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import lombok.Getter;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opLocksmith.OpDrillingByMarking;
import ru.wert.normic.enums.EMeasure;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;

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

    private int diameter; //диаметр отверстия
    private int depth; //глубина отверстия
    private int holes; //Количество отверстий
    private int length; //Максимальная длина или шаг измерения

    int[] depths = new int[]{                           5,    27,   10,   12,   15,   20,   25,   30,   40};
    private final double delta = 0.01;
    //https://sudact.ru/law/obshchemashinostroitelnye-normativy-vremeni-na-slesarno-instrumentalnye-raboty-vypolniaemye/normativnaia-chast/razdel-ii/karta-37/list-1_35/
    enum EDrillingOnRadial {
        DRILLING_ON_RADIAL_D3 (3,   new double[]{0.32, 0.38, 0.47, 0.52, 0.58, 0.68, 0.77, 0.86, 1.00}),
        DRILLING_ON_RADIAL_D4 (4,   new double[]{0.28, 0.34, 0.41, 0.46, 0.52, 0.61, 0.69, 0.76, 0.89}),
        DRILLING_ON_RADIAL_D5 (5,   new double[]{0.29, 0.35, 0.43, 0.48, 0.54, 0.63, 0.71, 0.79, 0.92}),
        DRILLING_ON_RADIAL_D6 (6,   new double[]{0.32, 0.38, 0.46, 0.51, 0.58, 0.68, 0.77, 0.85, 1.00}),
        DRILLING_ON_RADIAL_D8 (8,   new double[]{0.36, 0.43, 0.52, 0.58, 0.65, 0.77, 0.87, 0.96, 1.10}),
        DRILLING_ON_RADIAL_D10(10,  new double[]{0.39, 0.47, 0.57, 0.63, 0.72, 0.84, 0.95, 1.05, 1.25}),
        DRILLING_ON_RADIAL_D12(12,  new double[]{0.42, 0.54, 0.62, 0.68, 0.77, 0.90, 1.00, 1.15, 1.30}),
        DRILLING_ON_RADIAL_D16(16,  new double[]{0.47, 0.57, 0.69, 0.77, 0.87, 1.00, 1.15, 1.25, 1.50}),
        DRILLING_ON_RADIAL_D20(20,  new double[]{0.52, 0.63, 0.76, 0.84, 0.95, 1.10, 1.25, 1.40, 1.65}),
        DRILLING_ON_RADIAL_D25(25,  new double[]{0.57, 0.69, 0.83, 0.92, 1.05, 1.20, 1.40, 1.55, 1.80});


        @Getter int diameter;
        @Getter double[] times;
        EDrillingOnRadial(int diameter, double[] times){
            this.diameter = diameter;
            this.times = times;}
    }


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        initStyle = tfLength.getStyle(); //Сохраняем исходный стиль

        new TFIntegerColored(tfDiameter, this);
        new TFIntegerColored(tfDepth, this);
        new TFIntegerColored(tfHoles, this);
        new TFIntegerColored(tfLength, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpDrillingByMarking opData = (OpDrillingByMarking) data;

        countInitialValues();

        currentNormTime =
                findTimeForMarking() * (holes + 1) +    //Время разметки
                holes * findTimeForDrilling();          //Время сверления
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Время сверления
     */
    private Double findTimeForDrilling() {
        int prevDepth = 0;
        for (int i = 0; i < depths.length; i++) {
            if (depth >= prevDepth && depth <= depths[i]) {
                int prevDiam = 0;
                for (EDrillingOnRadial eDiam : EDrillingOnRadial.values()) {
                    if (diameter >= prevDiam && diameter <= eDiam.getDiameter()) {
                        if (i < eDiam.getTimes().length)
                            return eDiam.getTimes()[i];
                        else
                            //Возвращаем самое последнее значение в массиве
                            return eDiam.getTimes()[eDiam.getTimes().length - 1];
                    }
                    prevDiam = eDiam.getDiameter();
                }
            }
            prevDepth = depths[i];
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Время разметки
     */
    private Double findTimeForMarking(){
        EMeasure lastMeasure = EMeasure.values()[EMeasure.values().length-1];
        if(length > lastMeasure.getLength())
            return lastMeasure.getTime();

        int prevL = 0;
        for (EMeasure d : EMeasure.values()) {
            if (length >= prevL && length <= d.getLength())
                return d.getTime();

            prevL = d.getLength();
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = IntegerParser.getValue(tfDiameter);
        if(diameter > EDrillingOnRadial.values()[EDrillingOnRadial.values().length-1].diameter) {
            tfDiameter.setStyle("-fx-border-color: #FF5555");
            diameter = 25;
        } else
            tfDiameter.setStyle(initStyle);

        depth = IntegerParser.getValue(tfDepth);
        holes = IntegerParser.getValue(tfHoles);
        length = IntegerParser.getValue(tfLength);

    }


    private void collectOpData(OpDrillingByMarking opData){
        opData.setDiameter(diameter);
        opData.setDepth(depth);
        opData.setHoles(holes);
        opData.setLength(length);

        opData.setMechTime(currentNormTime);
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
