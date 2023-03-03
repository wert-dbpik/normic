package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackOnPallet;

/**
 * КРЕПЛЕНИЕ К ПОДДОНУ
 */
public class PlatePackOnPalletController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    private Double stretchMachineWrapL, polyWrapL;
    private int height; //габарит квадратного поддона
    private double palletDepth = 0.800; //габарит квадратного поддона
    private double palletWidth = 1.200; //габарит квадратного поддона
    private int layers = 2; //Количество слоев при наматывании пленки

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpPackOnPallet opData = (OpPackOnPallet) data;

        countInitialValues();

        double countHeight = height * MM_TO_M;

        stretchMachineWrapL = Math.ceil((palletDepth + palletWidth) * 2 * countHeight / 0.3 * layers);
        polyWrapL = Math.ceil((countHeight * 1.15 * 4.0) + (2.0 * palletDepth));

        currentNormTime = 14.0;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        height = ((FormPackController)formController).getHeight();
    }

    private void collectOpData(OpPackOnPallet opData){
        opData.setStretchMachineWrap(stretchMachineWrapL);
        opData.setPolyWrap(polyWrapL);
        opData.setPallet(1.0);

        opData.setPackTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){

        //НИКАКИХ ДЕЙСТВИЙ
    }

}