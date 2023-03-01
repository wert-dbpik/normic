package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpMountOnPallet;

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

    private Double stretchMachineWrapL, polyWrapL;
    private int width, depth, height; //габарит квадратного поддона
    private int palletWidth1 = 800; //габарит квадратного поддона
    private int palletWidth2 = 1200; //габарит квадратного поддона
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
        OpMountOnPallet opData = (OpMountOnPallet) data;

        countInitialValues();

        stretchMachineWrapL = ((palletWidth1 * MM_TO_M + palletWidth2 * MM_TO_M) * 2 * height * MM_TO_M / 0.3 * layers);
        polyWrapL = ((height * MM_TO_M * 1.15 * 4.0) + (2.0 * palletWidth1 * MM_TO_M));

        currentNormTime = 14.0;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        width = ((FormPackController)formController).getWidth();
        depth = ((FormPackController)formController).getDepth();
        height = ((FormPackController)formController).getHeight();
    }

    private void collectOpData(OpMountOnPallet opData){
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
