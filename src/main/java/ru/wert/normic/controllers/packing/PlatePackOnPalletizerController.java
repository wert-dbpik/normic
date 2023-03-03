package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInMachineStretchWrap;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * УПАКОВКА ВЫСОКОГО ШКАФА
 */
public class PlatePackOnPalletizerController extends AbstractOpPlate {

    @FXML
    private TextField tfPartMin;

    @FXML
    private TextField tfMachineStretchWrap;

    @FXML
    private TextField tfCartoon;

    @FXML
    private TextField tfDuctTape;

    @FXML
    private Label lblOperationName;

    private int width, depth, height;
    private int partMin; //Минимальная партия коробок
    private Double cartoon;
    private Double stretchWrap;
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
        OpPackInMachineStretchWrap opData = (OpPackInMachineStretchWrap) data;

        countInitialValues();

        cartoon = Math.ceil(2*((width * MM_TO_M + 0.1) * (depth * MM_TO_M + 0.1) * 1.2) + //Крышки верх и низ
                            height * MM_TO_M * 1.1 * 4); //4 уголка на всю высоту

        tfCartoon.setText(DECIMAL_FORMAT.format(cartoon));

        stretchWrap = Math.ceil((width * MM_TO_M + depth * MM_TO_M) * 2 * height * MM_TO_M / 0.3 * 2); //м
        tfMachineStretchWrap.setText(DECIMAL_FORMAT.format(stretchWrap));

        int perimeter = (width + depth) * 2;
        ductTape = Math.ceil(perimeter * MM_TO_M * 4) / DUCT_TAPE_LENGTH;  //Вокруг изделия 4 раза
        tfDuctTape.setText(DECIMAL_FORMAT.format(ductTape));

        double time = (CARTOON_AND_STRETCH_PREPARED_TIME + CARTOON_BOX_PREPARED_TIME / partMin) * 1.07 + //Время изготовления 2х крышек
                stretchWrap * STRETCH_MACHINE_WINDING * height * MM_TO_M; //Время упаковки изделия в коробку

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

    private void collectOpData(OpPackInMachineStretchWrap opData){

        opData.setPolyWrap(partMin);
        opData.setCartoon(cartoon);
        opData.setStretchMachineWrap(stretchWrap);
        opData.setDuctTape(ductTape);

        opData.setPackTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPackInMachineStretchWrap opData = (OpPackInMachineStretchWrap)data;

        partMin = opData.getPartMin();
        tfPartMin.setText(String.valueOf(partMin));

    }

}
