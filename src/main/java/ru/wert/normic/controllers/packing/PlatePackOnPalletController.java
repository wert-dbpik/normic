package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackOnPallet;
import ru.wert.normic.enums.EOpType;

/**
 * УСТАНОВКА НА ПОДДОН
 */
public class PlatePackOnPalletController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    private int height; //габарит квадратного поддона
    private double palletDepth = 0.800; //габарит квадратного поддона
    private double palletWidth = 1.200; //габарит квадратного поддона
    private double polyWrapL; //полипропиленовая лента

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        ivOperation.setImage(EOpType.PACK_ON_PALLET.getLogo());

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
        opData.setPolyWrap(polyWrapL);
        opData.setPallet(1.0);

        opData.setPackTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){

        //НИКАКИХ ДЕЙСТВИЙ
    }

    @Override
    public String helpText() {
        return "Установленное на поддон изделие крепится полипропиленовой лентой:\n" +
                "Расход полипропиленовой ленты вычисляется по формуле:\n\n" +
                "\t\t\tL ленты = H x 1.15 x 4 + 2 x D, м,\n" +
                "где\n" +
                "\tH, D - высота и глубина изделия, м.";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
