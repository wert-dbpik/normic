package ru.wert.normic.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.*;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpPaintAssm;
import ru.wert.normic.enums.EAssemblingType;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

public class PlatePaintAssmController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private ComboBox<EAssemblingType> cmbxAssemblingType;

    @FXML
    private TextField tfArea;

    @FXML
    private TextField tfAlong;

    @FXML
    private TextField tfAcross;

    @FXML
    private ImageView ivHelpOnPainting;

    @FXML
    private TextField tfNormTime;

    private int along; //Параметр А вдоль штанги
    private int across; //Параметр B поперек штанги
    private double area; //Площадь развертки
    private double pantingSpeed;// Скорость нанесения покрытия

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpPaintAssm opData = (OpPaintAssm)data;

        new BXAssemblingType().create(cmbxAssemblingType);
        new TFNormTime(tfNormTime, formController);
        new TFColoredDouble(tfArea, this);
        new TFColoredInteger(tfAlong, this);
        new TFColoredInteger(tfAcross, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new CmBx(cmbxAssemblingType, this);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpPaintAssm opData = (OpPaintAssm)data;

        countInitialValues();

        final int DELTA = 300; //расстояние между сборками

        final double HANGING_TIME = 0.34; //ремя навески и снятия после полимеризации
        final double WINDING_MOVING_SPEED = 1.4; //продувка после промывки и перемещение изделя на штанге, мин/1 м.кв.

        final int alongSize = along + DELTA;
        final int acrossSize = across + DELTA;

        int partsOnBar = 2500/alongSize;

        //Количество штанг в печи
        int bakeBars;
        if(acrossSize < 49) bakeBars = 6;
        else if(acrossSize >= 50 && acrossSize <= 99) bakeBars = 5;
        else if(acrossSize >= 100 && acrossSize <= 199) bakeBars = 4;
        else if(acrossSize >= 200 && acrossSize <= 299) bakeBars = 3;
        else if(acrossSize >= 300 && acrossSize <= 399) bakeBars = 2;
        else bakeBars = 1;

        double time;
        time = HANGING_TIME//Время навешивания
                + area * WINDING_MOVING_SPEED //Время подготовки к окрашиванию
                + area * pantingSpeed //Время нанесения покрытия
                + 40.0/bakeBars/partsOnBar;  //Время полимеризации
        if(area == 0.0) time = 0.0;

        currentNormTime = time;//результат в минутах
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {
        area = DoubleParser.getValue(tfArea);
        along = IntegerParser.getValue(tfAlong);
        across = IntegerParser.getValue(tfAcross);
        pantingSpeed = cmbxAssemblingType.getValue().getSpeed();

    }

    private void collectOpData(OpPaintAssm opData){
        opData.setArea(area);
        opData.setAlong(along);
        opData.setAcross(across);
        opData.setAssmType(cmbxAssemblingType.getValue());

        opData.setPaintTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPaintAssm opData = (OpPaintAssm)data;

        area = opData.getArea();
        tfArea.setText(String.valueOf(area));

        along = opData.getAlong();
        tfAlong.setText(String.valueOf(along));

        across = opData.getAcross();
        tfAcross.setText(String.valueOf(across));

        pantingSpeed = opData.getAssmType().getSpeed();
        cmbxAssemblingType.setValue(opData.getAssmType());
    }

}
