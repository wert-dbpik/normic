package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import ru.wert.normic.components.RadBtn;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInBubbleWrap;
import ru.wert.normic.entities.ops.opPack.OpPackInHandStretchWrap;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * УПАКОВКА РУЧНОЙ СТРЕЙЧ-ПЛЕНКОЙ
 */
public class PlatePackInHandStretchWrapController extends AbstractOpPlate {

    @FXML
    private RadioButton rbByHeight;

    @FXML
    private RadioButton rbByDepth;

    @FXML
    private RadioButton rbByWidth;

    @FXML
    private TextField tfStretchWrap;

    @FXML
    private TextField tfDuctTape;

    @FXML
    private Label lblOperationName;

    private int width, depth, height;
    private ToggleGroup windBy; //Накручивание по
    private RadioButton selectedRadioButton; //выделенный
    private Double handStretchWrap;
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
        OpPackInHandStretchWrap opData = (OpPackInHandStretchWrap) data;

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

        handStretchWrap = Math.ceil((countWidth + countDepth) * 2.0 * countHeight/0.3  * 2); //м
        tfStretchWrap.setText(DECIMAL_FORMAT.format(handStretchWrap));

        ductTape = countHeight / DUCT_TAPE_LENGTH;  //1 высота
        tfDuctTape.setText(DECIMAL_FORMAT.format(ductTape));

        double time = handStretchWrap * STRETCH_HAND_WINDING;

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

    private void collectOpData(OpPackInHandStretchWrap opData){

        opData.setSelectedRadioButton(selectedRadioButton);
        opData.setBubbleWrap(handStretchWrap);
        opData.setDuctTape(ductTape);

        opData.setPackTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPackInHandStretchWrap opData = (OpPackInHandStretchWrap)data;

        selectedRadioButton = opData.getSelectedRadioButton();
        if(selectedRadioButton == null) selectedRadioButton = rbByHeight;
        selectedRadioButton.setSelected(true);

    }

}