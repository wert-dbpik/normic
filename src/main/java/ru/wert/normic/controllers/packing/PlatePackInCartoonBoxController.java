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
import ru.wert.normic.entities.ops.opPack.OpPackInCartoonBox;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

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
    private TextField tfNormTime;

    private OpPackInCartoonBox opData;
    private int width, depth, height;
    private int partMin; //Минимальная партия коробок


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        new TFNormTime(tfNormTime, prevFormController);
        new TFIntegerColored(tfPartMin, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpPackInCartoonBox) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);

        tfCartoon.setText(DECIMAL_FORMAT.format(opData.getCartoon()));
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
        opData.setHeight(height);
        opData.setWidth(width);
        opData.setDepth(depth);
        opData.setPartMin(partMin);

    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPackInCartoonBox opData = (OpPackInCartoonBox)data;

        partMin = opData.getPartMin();
        tfPartMin.setText(String.valueOf(partMin));

    }

    @Override
    public String helpText() {
        return String.format("Минимальная партия - партия изготовления картонных коробок одного размера.\n" +
                        "Расход картона рассчитывается по формуле:\n\n" +
                        "\t\t\tS карт = (W+0.1)x(D+0.1)*2 + \n" +
                        "\t\t\t\t\t(W+0.1)x(H+0.1)*2 + \n" +
                        "\t\t\t\t\t(D+0.1)x(H+0.1)*2, м.кв.\n" +
                        "где\n" +
                        "\tW, D, H - ширина, глубина и высота изделия, м;\n\n" +
                        "Для крепления картона используется скотч, расход:\n\n" +
                        "\t\t\tL скотч = ((W+D)x2 x 4 + 4xH)/ L рулон, шт,\n" +
                        "\t\t\t\t\t(4 периметра и 4 высоты), \n" +
                        "где\n" +
                        "\tL рулон = %s - длина скотча в рулоне, м.\n\n" +
                        "Норма времени упаковки рассчитывается по формуле:\n\n" +
                        "\t\t\tT упак = (T изг + T пз.короб/ N min) x 1.07  + \n" +
                        "\t\t\t\t\tT упак, мин,\n" +
                        "где\n" +
                        "\tT изг = %s - время изготовления коробки, мин;\n" +
                        "\tT пз.короб = %s - ПЗ время изготовления коробки, мин;\n" +
                        "\tN min = минимальная партия каотонных крышек, шт;\n" +
                        "\tT упак = %s - время упаковки в коробку, мин.\n\n" +
                        "В норму времени входит наклейка этикеток и укладка \n" +
                        "\t\tсопроводительной документации.",
                DUCT_TAPE_LENGTH, CARTOON_BOX_SPEED, CARTOON_BOX_PREPARED_TIME, PACK_IN_CARTOON_BOX_SPEED
        );
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
