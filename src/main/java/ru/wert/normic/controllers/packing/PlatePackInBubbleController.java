package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import ru.wert.normic.components.RadBtn;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInBubbleWrap;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * УПАКОВКА ВЫСОКОГО ШКАФА
 */
public class PlatePackInBubbleController extends AbstractOpPlate {

    @FXML
    private RadioButton rbByHeight;

    @FXML
    private RadioButton rbByDepth;

    @FXML
    private RadioButton rbByWidth;

    @FXML
    private TextField tfBubbleWrap;

    @FXML
    private TextField tfDuctTape;

    @FXML
    private Label lblOperationName;

    private int width, depth, height;
    private ToggleGroup windBy; //Накручивание по
    private RadioButton selectedRadioButton; //выделенный
    private Double bubbleWrap;
    private Double ductTape;

    @FXML
    void initialize(){
        windBy = new ToggleGroup();
        rbByHeight.setToggleGroup(windBy);
        rbByDepth.setToggleGroup(windBy);
        rbByWidth.setToggleGroup(windBy);
    }


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new RadBtn(rbByHeight, this);
        new RadBtn(rbByDepth, this);
        new RadBtn(rbByWidth, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpPackInBubbleWrap opData = (OpPackInBubbleWrap) data;

        countInitialValues();

        double countHeight;
        double countDepth;
        double countWidth;

        if(selectedRadioButton.equals(rbByHeight)){
            countHeight = height * MM_TO_M;
            countDepth = depth * MM_TO_M;
            countWidth = width * MM_TO_M;
        } else if(selectedRadioButton.equals(rbByDepth)){
            countHeight = depth * MM_TO_M;
            countDepth = height * MM_TO_M;
            countWidth = width * MM_TO_M;
        } else {
            countHeight = width * MM_TO_M;
            countDepth = depth * MM_TO_M;
            countWidth = height * MM_TO_M;
        }

        bubbleWrap = Math.ceil((countWidth + countDepth) * 2.0 * countHeight  * 1.1); //м2
        tfBubbleWrap.setText(DECIMAL_FORMAT.format(bubbleWrap));

        ductTape = (countHeight * 2) / DUCT_TAPE_LENGTH;  //Две высоты
        tfDuctTape.setText(DECIMAL_FORMAT.format(ductTape));

        double time = BUBBLE_CUT_AND_DUCT + bubbleWrap * BUBBLE_HAND_WINDING;

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        selectedRadioButton = (RadioButton) windBy.getSelectedToggle();

        width = ((FormPackController)formController).getWidth();
        depth = ((FormPackController)formController).getDepth();
        height = ((FormPackController)formController).getHeight();

    }


    private void collectOpData(OpPackInBubbleWrap opData){

        opData.setSelectedRadioButton(selectedRadioButton);
        opData.setBubbleWrap(bubbleWrap);
        opData.setDuctTape(ductTape);

        opData.setPackTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPackInBubbleWrap opData = (OpPackInBubbleWrap)data;

        selectedRadioButton = opData.getSelectedRadioButton();
        if(selectedRadioButton == null) selectedRadioButton = rbByHeight;
        selectedRadioButton.setSelected(true);

    }

    @Override
    public String helpText() {
        return null;
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
