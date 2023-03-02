package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInCartoonBox;
import ru.wert.normic.entities.ops.opPack.OpPackTallCabinet;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * УПАКОВКА ВЫСОКОГО ШКАФА
 */
public class PlatePackInCartoonBoxController extends AbstractOpPlate {


    @FXML
    private TextField tfPartMin;

    @FXML
    private TextField tfCartoon;

    @FXML
    private TextField tfDuctTape;

    @FXML
    private Label lblOperationName;

    private int width, depth, height;
    private int partMin; //Минимальная партия коробок
    private Double cartoon;
    private Double ductTape;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new TFIntegerColored(tfPartMin, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpPackInCartoonBox opData = (OpPackInCartoonBox) data;

        countInitialValues();

        cartoon = Math.ceil((width * MM_TO_M + 0.1) * (depth * MM_TO_M + 0.1) * 2 +
                (width * MM_TO_M + 0.1) * (height * MM_TO_M + 0.1) * 2 +
                (depth * MM_TO_M + 0.1) * (height * MM_TO_M + 0.1) * 2);

        tfCartoon.setText(String.valueOf(cartoon));

        ductTape = Math.ceil((width * MM_TO_M + depth * MM_TO_M) * 4.0 + depth * MM_TO_M * 4.0);
        tfDuctTape.setText(String.valueOf(ductTape));

        double time = (CARTOON_BOX_SPEED + CARTOON_BOX_PREPARED_TIME / partMin) * 1.07 + //Время изготовления коробки
                PACK_IN_CARTOON_BOX_SPEED; //Время упаковки изделия в коробку

        currentNormTime = time;
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

        partMin = IntegerParser.getValue(tfPartMin);
    }

    private void collectOpData(OpPackInCartoonBox opData){

        opData.setPolyWrap(partMin);
        opData.setCartoon(cartoon);
        opData.setDuctTape(ductTape);

        opData.setPackTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPackInCartoonBox opData = (OpPackInCartoonBox)data;

        partMin = opData.getPartMin();
        tfPartMin.setText(String.valueOf(partMin));

    }

}
