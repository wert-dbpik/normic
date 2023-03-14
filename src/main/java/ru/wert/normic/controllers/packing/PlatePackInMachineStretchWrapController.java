package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
public class PlatePackInMachineStretchWrapController extends AbstractOpPlate {

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

        double countHeight = height * MM_TO_M;
        double countDepth = depth * MM_TO_M;
        double countWidth = width * MM_TO_M;

        cartoon = Math.ceil(2*((countWidth + 0.1) * (countDepth + 0.1) * 1.2) + //Крышки верх и низ
                countHeight * 1.1 * 4); //4 уголка на всю высоту

        tfCartoon.setText(DECIMAL_FORMAT.format(cartoon));

        stretchWrap = Math.ceil((countWidth + countDepth) * 2 * countHeight / 0.3 * 2); //м
        tfMachineStretchWrap.setText(DECIMAL_FORMAT.format(stretchWrap));

        double perimeter = (countWidth + countDepth) * 2;
        ductTape = Math.ceil(perimeter * 4) / DUCT_TAPE_LENGTH;  //Вокруг изделия 4 раза
        tfDuctTape.setText(DECIMAL_FORMAT.format(ductTape));

        double time = (CARTOON_AND_STRETCH_PREPARED_TIME + CARTOON_BOX_PREPARED_TIME / partMin) * 1.07 + //Время изготовления 2х крышек
                STRETCH_MACHINE_WINDING * countHeight; //Время упаковки изделия в коробку

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

    @Override
    public String helpText() {
        return String.format("Минимальная партия - партия изготовления картонных крышек одного размера\n" +
                        "Расход картона на две крышки и четыре уголка рассчитывается по формуле:\n\n" +
                        "S картон = (W + 0.1) x (D + 0.1) x 2 x 1.2 + H x 1.1 x 4, м.кв.,;\n" +
                        "где\n" +
                        "\tW, D, H - ширина, глубина и высота изделия, м;\n\n" +
                        "Расход машинной стрейч-пленки рассчитывается по формуле:\n\n" +
                        "L м.стрейч = P x H/0.3 x 2, м,\n" +
                        "где\n" +
                        "\tP - периметр наматываемой поверхности, м;\n" +
                        "\tH (высота) - размер, перпиндикулярный плосткости периметра, м;\n" +
                        "\t0.3 - нахлест, м;\n\n" +
                        "Для крепления пленки и картона используется скотч, расход:\n\n" +
                        "L скотч = 4 x P / L рулон (4 периметра), шт,\n" +
                        "где\n" +
                        "\tL рулон = %s - длина скотча в рулоне, м\n\n" +
                        "Норма времени упаковки рассчитывается по формуле:\n\n" +
                        "T упак = (T пз + T пз.короб/ N min) x 1.07  + V м.упак x H, мин\n" +
                        "где\n" +
                        "\tT пз = %s - ПЗ время на упаковку в машинную пленку, мин;\n" +
                        "\tT пз.короб = %s - время изготовления картонных элементов, мин;\n" +
                        "\tN min = минимальная партия каотонных крышек, шт;\n" +
                        "\tV м.упак = %s - скорость оборачивания пленки, мин/м.",
                DUCT_TAPE_LENGTH, CARTOON_AND_STRETCH_PREPARED_TIME, CARTOON_BOX_PREPARED_TIME, STRETCH_MACHINE_WINDING);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
