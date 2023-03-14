package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInCartoonBox;
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

        double countHeight = height * MM_TO_M;
        double countDepth = depth * MM_TO_M;
        double countWidth = width * MM_TO_M;

        cartoon = Math.ceil((countWidth + 0.1) * (countDepth + 0.1) * 2 +
                (countWidth + 0.1) * (countHeight + 0.1) * 2 +
                (countDepth + 0.1) * (countHeight + 0.1) * 2);

        tfCartoon.setText(DECIMAL_FORMAT.format(cartoon));

        ductTape = ((countWidth + countDepth) * 4.0 + countHeight * 4.0) / DUCT_TAPE_LENGTH;
        tfDuctTape.setText(DECIMAL_FORMAT.format(ductTape));

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

    @Override
    public String helpText() {
        return String.format("Минимальная партия - партия изготовления картонных коробок одного размера.\n" +
                        "Расход картона рассчитывается по формуле:\n\n" +
                        "S карт = (W+0.1)x(D+0.1)*2 + (W+0.1)x(H+0.1)*2 + (D+0.1)x(H+0.1)*2, м.кв.\n" +
                        "где\n" +
                        "\tW, D, H - ширина, глубина и высота изделия, м;\n\n" +
                        "Для крепления картона используется скотч, расход:\n\n" +
                        "L скотч = ((W+D)x2 x 4 + 4xH)/ L рулон (4 периметра и 4 высоты), шт,\n" +
                        "где\n" +
                        "\tL рулон = %s - длина скотча в рулоне, м.\n\n" +
                        "Норма времени упаковки рассчитывается по формуле:\n\n" +
                        "T упак = (T изг + T пз.короб/ N min) x 1.07  + T упак, мин,\n" +
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
