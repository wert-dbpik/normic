package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.OpErrorData;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * СЛЕСАРНЫЕ ОПЕРАЦИИ
 */
public class PlateErrorController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;
    
    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private Label lbErrorOpData;

    @FXML
    private Label lblOperationName;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpErrorData opData = (OpErrorData)data;
        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        lbErrorOpData.setText(opData.getErrorOpData());

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


}
