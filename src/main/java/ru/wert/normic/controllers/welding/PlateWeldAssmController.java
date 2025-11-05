package ru.wert.normic.controllers.welding;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.BXPartBigness;
import ru.wert.normic.components.BXWeldDifficulty;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opWelding.OpWeldAssm;
import ru.wert.normic.entities.ops.opWelding.OpWeldDifficulty;
import ru.wert.normic.enums.EPartBigness;
import ru.wert.normic.enums.EWeldDifficulty;
import ru.wert.normic.utils.IntegerParser;

import static java.lang.String.format;
import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

/**
 * СБОРКА СВАРОЧНОЙ КОНСТРУКЦИИ
 */
public class PlateWeldAssmController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;
    
    @FXML
    private ComboBox<EPartBigness> cmbxPartBigness;

    @FXML
    private TextField tfNumOfDetails;

    private int numOfDetails; // Число привариваемых деталей

    private OpWeldAssm opData;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        opData = (OpWeldAssm) data;

        new TFIntegerColored(tfNumOfDetails, this);

        new BXPartBigness().create(cmbxPartBigness, opData.getPartBigness(), this);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpWeldAssm) data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        collectOpData();
    }


    private void collectOpData(){
        opData.setNumOfDetails(IntegerParser.getValue(tfNumOfDetails));
        opData.setPartBigness(cmbxPartBigness.getValue());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpWeldAssm opData = (OpWeldAssm)data;

        cmbxPartBigness.setValue(opData.getPartBigness());

        numOfDetails = opData.getNumOfDetails();
        tfNumOfDetails.setText(String.valueOf(numOfDetails));

    }

    @Override
    public String helpText() {
        return
                "Здесь добавляется врямя на сварку сварочной конструкции\n" +
                        "в зависимости от ее габаритов\n" +
                        "\t\t\tT св.непр.= N сб.дет x V сб.св, мин\n" +
                        "где\n\n" +
                        "\tN сб.дет - число свариваемых деталей, шт;\n" +
                        "\tV сб.св - время на сборку одной детали, зависит от габаритов всей конструкции:\n" +
                format("\t\tсборка легкой конструкции %d мин/дет\n", EPartBigness.SMALL.getTimePerPart()) +
                format("\t\tсборка тяжелой конструкции %d мин/дет\n", EPartBigness.BIG.getTimePerPart());

    }

    @Override
    public Image helpImage() {
        return null;
    }
}
