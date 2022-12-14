package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import ru.wert.normic.components.TFColoredInteger;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpWeldDotted;
import ru.wert.normic.utils.IntegerParser;

public class PlateWeldDottedController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

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

    private int parts; //Количество элементов
    private int dots; //Количество точек
    private int drops; //Количество прихваток

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpWeldDotted opData = (OpWeldDotted)data;

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new TFNormTime(tfNormTime, formController);
        new TFColoredInteger(tfParts, this);
        new TFColoredInteger(tfDots, this);
        new TFColoredInteger(tfDrops, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpWeldDotted opData = (OpWeldDotted)data;

        countInitialValues();

        final double WELDING_PARTS_SPEED = 0.13; //скорость онденсаторной сварки точкой, мин/элемент
        final double WELDING_DOTTED_SPEED = 0.3; //скорость контактной сварки, мин/точку
        final double WELDING_DROP_SPEED = 0.07; //скорость сварки прихватками, мин/прихватку

        double time;
        time =  parts * WELDING_PARTS_SPEED + dots * WELDING_DOTTED_SPEED + drops * WELDING_DROP_SPEED;   //мин

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {

        parts = IntegerParser.getValue(tfParts);
        dots = IntegerParser.getValue(tfDots);
        drops = IntegerParser.getValue(tfDrops);
    }

    private void collectOpData(OpWeldDotted opData){
        opData.setParts(parts);
        opData.setDots(dots);
        opData.setDrops(drops);

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpWeldDotted opData = (OpWeldDotted)data;

        parts = opData.getParts();
        tfParts.setText(String.valueOf(parts));

        dots = opData.getDots();
        tfDots.setText(String.valueOf(dots));

        drops = opData.getDrops();
        tfDrops.setText(String.valueOf(drops));
    }


}
