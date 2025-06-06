package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInMachineStretchWrap;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

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
    private TextField tfNormTime;

    private OpPackInMachineStretchWrap opData;

    private int width, depth, height;
    private int partMin; //Минимальная партия коробок


    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFNormTime(tfNormTime, prevFormController);
        new TFIntegerColored(tfPartMin, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpPackInMachineStretchWrap) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);

        tfCartoon.setText(DECIMAL_FORMAT.format(opData.getCartoon()));
        tfCartoonAngle.setText(DECIMAL_FORMAT.format(opData.getCartoonAngle()));
        tfMachineStretchWrap.setText(DECIMAL_FORMAT.format(opData.getStretchMachineWrap()));
        tfDuctTape.setText(DECIMAL_FORMAT.format(opData.getDuctTape()));
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        width = ((FormPackController) prevFormController).getWidth();
        depth = ((FormPackController) prevFormController).getDepth();
        height = ((FormPackController) prevFormController).getHeight();

        partMin = IntegerParser.getValue(tfPartMin);

        collectOpData();
    }


    private void collectOpData(){
        opData.setPartMin(partMin);

        opData.setHeight(height);
        opData.setWidth(width);
        opData.setDepth(depth);
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
