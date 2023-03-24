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
import ru.wert.normic.enums.EOpType;
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
    private TextField tfCartoonAngle;

    @FXML
    private TextField tfDuctTape;

    @FXML
    private Label lblOperationName;

    private int width, depth, height;
    private int partMin; //Минимальная партия коробок
    private Double cartoonTop;
    private Double cartoonAngle;
    private Double stretchWrap;
    private Double ductTape;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        ivOperation.setImage(EOpType.PACK_IN_MACHINE_STRETCH_WRAP.getLogo());

        lblOperationName.setText(EOpType.PACK_IN_MACHINE_STRETCH_WRAP.getOpName().toUpperCase());
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

        cartoonTop = Math.ceil((countWidth + 0.1) * (countDepth + 0.1) * 1.2 * 2); //Крышки верх и низ
        cartoonAngle = Math.ceil(countHeight * 1.1 * 4); //4 уголка на всю высоту

        tfCartoon.setText(DECIMAL_FORMAT.format(cartoonTop));
        tfCartoonAngle.setText(DECIMAL_FORMAT.format(cartoonAngle));

        stretchWrap = Math.ceil((countWidth + countDepth) * 2 * countHeight / 0.3 * 2); //м
        tfMachineStretchWrap.setText(DECIMAL_FORMAT.format(stretchWrap));

        double perimeter = (countWidth + countDepth) * 2;
        ductTape = Math.ceil(perimeter * 4) / DUCT_TAPE_LENGTH;  //Вокруг изделия 4 раза
        tfDuctTape.setText(DECIMAL_FORMAT.format(ductTape));

        double time = CARTOON_BOX_AND_ANGLES_SPEED + CARTOON_BOX_PREPARED_TIME / partMin * 1.07 + //Время изготовления 2х крышек
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
        opData.setCartoon(cartoonTop);
        opData.setCartoonAngle(cartoonAngle);
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
        return String.format("Минимальная партия - партия изготовления картонных крышек одного размера.\n" +
                        "Расход картона на две крышки и четыре уголка рассчитывается по формулам:\n\n" +
                        "\t\t\tS крышки = (W + 0.1) x (D + 0.1) x 1.2 x 2, м.кв.,;\n" +
                        "\t\t\tS уголки = H x 1.1 x 4, м,;\n" +
                        "где\n" +
                        "\tW, D, H - ширина, глубина и высота изделия, м;\n\n" +
                        "Расход машинной стрейч-пленки рассчитывается по формуле:\n\n" +
                        "\t\t\tL м.стрейч = P x H/0.3 x 2, м,\n" +
                        "где\n" +
                        "\tP - периметр наматываемой поверхности, м;\n" +
                        "\tH (высота) - размер, перпиндикулярный плосткости периметра, м;\n" +
                        "\t0.3 - нахлест, м;\n\n" +
                        "Для крепления пленки и картона используется скотч, расход:\n\n" +
                        "\t\t\tL скотч = 4 x P / L рулон (4 периметра), шт,\n" +
                        "где\n" +
                        "\tL рулон = %s - длина скотча в рулоне, м\n\n" +
                        "Норма времени упаковки рассчитывается по формуле:\n\n" +
                        "\t\t\tT упак = T изг + T пз.короб/N min x 1.07  + \n" +
                        "\t\t\t\t\tV м.упак x H, мин\n" +
                        "где\n" +
                        "\tT изг = %s - время изготовления крышек и уголков, мин;\n" +
                        "\tT пз.короб = %s - ПЗ время изготовления картонных элементов, мин;\n" +
                        "\tN min = минимальная партия каотонных крышек, шт;\n" +
                        "\tV м.упак = %s - скорость оборачивания пленки, мин/м.",
                DUCT_TAPE_LENGTH, CARTOON_BOX_AND_ANGLES_SPEED, CARTOON_BOX_PREPARED_TIME, STRETCH_MACHINE_WINDING);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
