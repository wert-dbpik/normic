package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.OpErrorData;
import ru.wert.normic.enums.EOpType;

/**
 * СЛЕСАРНЫЕ ОПЕРАЦИИ
 */
public class PlateErrorController extends AbstractOpPlate {

    @FXML
    private Label lbErrorOpData;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpErrorData opData = (OpErrorData)data;

        lblOperationName.setStyle("-fx-text-fill: #ef1515");

        lbErrorOpData.setText(EOpType.findOpTypeByName(opData.getErrorOpData()).getOpName());
        lbErrorOpData.setStyle("-fx-text-fill: #ef1515");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data) {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }



    private void collectOpData(OpErrorData opData) {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data) {
        //НЕ ИСПОЛЬЗУЕТСЯ
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
