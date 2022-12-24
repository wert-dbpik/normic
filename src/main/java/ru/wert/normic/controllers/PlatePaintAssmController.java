package ru.wert.normic.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.*;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.entities.OpPaintAssm;
import ru.wert.normic.enums.EAssemblingType;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

import java.util.List;

public class PlatePaintAssmController extends AbstractOpPlate {

    @FXML
    private ImageView ivContextMenu;

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ComboBox<EColor> cmbxColor;

    @FXML
    private TextField tfDyeWeight;

    @FXML
    private CheckBox chbxTwoSides;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private ComboBox<EAssemblingType> cmbxAssemblingType;

    @FXML
    private CheckBox chbxCalculatedArea;

    @FXML
    private TextField tfCalculatedArea;

    @FXML
    private TextField tfManualArea;

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
    private double area; //Площадь покрытия введенная вручную
    private double pantingSpeed;// Скорость нанесения покрытия
    private boolean twoSides; //Красить с двух сторон

    private double kArea; //k = 1, если chbxTwoSides isSelected (окрашивание с двух сторон), иначе k = 0.5

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpPaintAssm opData = (OpPaintAssm)data;
        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        countCalculatedArea();

        tfCalculatedArea.disableProperty().bind(chbxCalculatedArea.selectedProperty().not());
        tfManualArea.disableProperty().bind(chbxCalculatedArea.selectedProperty());
        formController.getFormAreaProperty().addListener((observable, oldValue, newValue) -> {
            if(opData.isCalculatedArea()) {
                area = newValue.doubleValue();
                double calcArea = area * kArea;
                tfCalculatedArea.setText(String.format(DOUBLE_FORMAT, calcArea));
            }
        });

        tfCalculatedArea.textProperty().addListener((observable) -> {
            countNorm(opData);
        });

        chbxTwoSides.selectedProperty().addListener((observable, oldValue, newValue) -> {
            opData.setTwoSides(newValue);
            kArea = newValue ? 1.0 : 0.5;
            countCalculatedArea();
            countNorm(opData);
        });

        new BXColor().create(cmbxColor, opData.getColor(), this);
        new BXAssemblingType().create(cmbxAssemblingType, opData.getAssmType(), this);
        new TFNormTime(tfNormTime, formController);
        new TFColoredDouble(tfManualArea, this);
        new TFColoredInteger(tfAlong, this);
        new TFColoredInteger(tfAcross, this);
        new ChBox(chbxCalculatedArea, this);
        new CmBx(cmbxAssemblingType, this);

    }

    private void countCalculatedArea() {
        kArea = chbxTwoSides.isSelected() ? 1.0 : 0.5;
        if(chbxCalculatedArea.isSelected()){
            area = formController.calculateAreaByDetails();//Суммировать площадь входящих деталей
            tfCalculatedArea.setText(String.format(DOUBLE_FORMAT, area * kArea));
        }
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpPaintAssm opData = (OpPaintAssm)data;

        countInitialValues();

        double paintWeight = EColor.getConsumption(cmbxColor.getValue()) * area;
        tfDyeWeight.setText(String.format(DOUBLE_FORMAT, paintWeight));

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
    public void countInitialValues() {
        twoSides = chbxTwoSides.isSelected();
        kArea = twoSides ? 1.0 : 0.5;

        if(!chbxCalculatedArea.isSelected()){
            area = DoubleParser.getValue(tfManualArea);//Использовать введенную пользователем площадь
        } else {
            formController.calculateAreaByDetails();//Суммировать площадь входящих деталей
            area = formController.getFormAreaProperty().get() * kArea;
        }
        along = IntegerParser.getValue(tfAlong);
        across = IntegerParser.getValue(tfAcross);
        pantingSpeed = cmbxAssemblingType.getValue().getSpeed();


    }

    private void collectOpData(OpPaintAssm opData){

        opData.setColor(cmbxColor.getValue());
        opData.setTwoSides(twoSides);
        opData.setCalculatedArea(chbxCalculatedArea.isSelected());
        opData.setArea(area);
        opData.setAlong(along);
        opData.setAcross(across);
        opData.setAssmType(cmbxAssemblingType.getValue());

        opData.setPaintTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPaintAssm opData = (OpPaintAssm)data;

        cmbxColor.setValue(opData.getColor());

        twoSides = opData.isTwoSides();
        chbxTwoSides.setSelected(twoSides);

        chbxCalculatedArea.setSelected(opData.isCalculatedArea());

        area = opData.getArea();
        if(opData.isCalculatedArea()){
            tfCalculatedArea.setText(String.format(DOUBLE_FORMAT, area));
            tfManualArea.setText(String.format(DOUBLE_FORMAT, 0.0));
        } else {
            tfCalculatedArea.setText(String.format(DOUBLE_FORMAT, 0.0));
            tfManualArea.setText(String.format(DOUBLE_FORMAT, area));
        }

        along = opData.getAlong();
        tfAlong.setText(String.valueOf(along));

        across = opData.getAcross();
        tfAcross.setText(String.valueOf(across));

        pantingSpeed = opData.getAssmType().getSpeed();
        cmbxAssemblingType.setValue(opData.getAssmType());
    }

}
