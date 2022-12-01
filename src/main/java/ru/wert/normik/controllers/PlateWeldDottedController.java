package ru.wert.normik.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normik.AbstractOpPlate;
import ru.wert.normik.components.TFColoredInteger;
import ru.wert.normik.components.TFNormTime;
import ru.wert.normik.entities.OpData;
import ru.wert.normik.entities.OpWeldDotted;
import ru.wert.normik.enums.ETimeMeasurement;
import ru.wert.normik.interfaces.IFormController;
import ru.wert.normik.utils.IntegerParser;

public class PlateWeldDottedController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfParts;

    @FXML
    private TextField tfDots;

    @FXML
    private TextField tfDrops;

    @FXML
    private TextField tfNormTime;

    private IFormController controller;
    private OpWeldDotted opData;

    public OpData getOpData(){
        return opData;
    }

    private int parts; //Количество элементов
    private int dots; //Количество точек
    private int drops; //Количество прихваток
    private ETimeMeasurement measure;

    public void init(IFormController controller, OpWeldDotted opData){
        this.controller = controller;
        this.opData = opData;

        fillOpData(); //Должен стоять до навешивагия слушателей на TextField

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new TFNormTime(tfNormTime, controller);
        new TFColoredInteger(tfParts, this);
        new TFColoredInteger(tfDots, this);
        new TFColoredInteger(tfDrops, this);

        ivDeleteOperation.setOnMouseClicked(e->{
            controller.getAddedPlates().remove(this);
            VBox box = controller.getListViewTechOperations().getSelectionModel().getSelectedItem();
            controller.getListViewTechOperations().getItems().remove(box);
            currentNormTime = 0.0;
            controller.countSumNormTimeByShops();
        });

        controller.getAddedPlates().add(this);
        countNorm();
    }

    @Override//AbstractOpPlate
    public void countNorm(){

        countInitialValues();

        final double WELDING_PARTS_SPEED = 0.13; //скорость онденсаторной сварки точкой, мин/элемент
        final double WELDING_DOTTED_SPEED = 0.3; //скорость контактной сварки, мин/точку
        final double WELDING_DROP_SPEED = 0.07; //скорость сварки прихватками, мин/прихватку

        double time;
        time =  parts * WELDING_PARTS_SPEED + dots * WELDING_DOTTED_SPEED + drops * WELDING_DROP_SPEED;   //мин

        currentNormTime = time;
        collectOpData();
        setTimeMeasurement(measure);
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {

        parts = IntegerParser.getValue(tfParts);
        dots = IntegerParser.getValue(tfDots);
        drops = IntegerParser.getValue(tfDrops);

        measure = controller.getCmbxTimeMeasurement().getValue();
        collectOpData();
    }

    private void collectOpData(){
        opData.setParts(parts);
        opData.setDots(dots);
        opData.setDrops(drops);

        opData.setMechTime(currentNormTime);
    }

    private void fillOpData(){
        parts = opData.getParts();
        tfParts.setText(String.valueOf(parts));

        dots = opData.getDots();
        tfDots.setText(String.valueOf(dots));

        drops = opData.getDrops();
        tfDrops.setText(String.valueOf(drops));
    }


}
