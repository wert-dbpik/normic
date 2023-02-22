package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.OpChopOff;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpMountOnPallet;
import ru.wert.normic.enums.EMeasure;

import java.util.NoSuchElementException;

/**
 * КРЕПЛЕНИЕ К ПОДДОНУ
 */
public class PlateMountOnPalletController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfHeight;

    @FXML
    private TextField tfNormTime;

    private Double roofWrapL, sideWrapL, stretchWrapL, polyWrapL, bubbleWrapL, ductTapeL;
    private double width = 0.8; //габарит квадратного поддона

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpMountOnPallet opData = (OpMountOnPallet) data;

//        stretchWrapL =

        countInitialValues();

        currentNormTime = 14.0;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

    }

    private void collectOpData(OpMountOnPallet opData){
        opData.setCartoon(roofWrapL + sideWrapL);
        opData.setStretchWrap(stretchWrapL);
        opData.setBubbleWrap(bubbleWrapL);
        opData.setPolyWrap(polyWrapL);
        opData.setDuctTape(ductTapeL);

        opData.setPackTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){

        //НИКАКИХ ДЕЙСТВИЙ
    }

}
