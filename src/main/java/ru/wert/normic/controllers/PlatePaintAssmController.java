package ru.wert.normic.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.AbstractOpPlate;
import ru.wert.normic.components.*;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpPaintAssm;
import ru.wert.normic.enums.EAssemblingType;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IFormController;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

public class PlatePaintAssmController extends AbstractOpPlate {

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

    private IFormController controller;
    private OpPaintAssm opData;

    public OpData getOpData(){
        return opData;
    }

    private int along; //Параметр А вдоль штанги
    private int across; //Параметр B поперек штанги
    private double area; //Площадь развертки
    private double pantingSpeed;// Скорость нанесения покрытия

    public void init(IFormController controller, OpPaintAssm opData){
        this.controller = controller;
        this.opData = opData;

        controller.getAddedPlates().add(this);
        controller.getAddedOperations().add(opData);

        fillOpData(); //Должен стоять до навешивагия слушателей на TextField

        new BXAssemblingType().create(cmbxAssemblingType);
        new TFNormTime(tfNormTime, controller);
        new TFColoredDouble(tfArea, this);
        new TFColoredInteger(tfAlong, this);
        new TFColoredInteger(tfAcross, this);


        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new CmBx(cmbxAssemblingType, this);

        ivDeleteOperation.setOnMouseClicked(e->{
            controller.getAddedPlates().remove(this);
            VBox box = controller.getListViewTechOperations().getSelectionModel().getSelectedItem();
            controller.getListViewTechOperations().getItems().remove(box);
            currentNormTime = 0.0;
            controller.countSumNormTimeByShops();
        });

        countNorm();

    }

    @Override//AbstractOpPlate
    public void countNorm(){

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
        collectOpData();
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

    private void collectOpData(){
        opData.setArea(area);
        opData.setAlong(along);
        opData.setAcross(across);
        opData.setAssmType(cmbxAssemblingType.getValue());

        opData.setPaintTime(currentNormTime);
    }

    private void fillOpData(){
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
