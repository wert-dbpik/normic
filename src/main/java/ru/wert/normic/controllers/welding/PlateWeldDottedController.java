package ru.wert.normic.controllers.welding;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.list.counters.OpBendingCounter;
import ru.wert.normic.controllers.welding.counters.OpWeldDottedCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opWelding.OpWeldDotted;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.settings.NormConstants.*;

/**
 * СВАРКА ТОЧЕЧНАЯ И ПРИХВАТКАМИ
 */
public class PlateWeldDottedController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private TextField tfParts;

    @FXML
    private TextField tfDots;

    @FXML
    private TextField tfDrops;

    @FXML
    private TextField tfNormTime;

    private OpWeldDotted opData;

    private int parts; //Количество элементов
    private int dots; //Количество точек
    private int drops; //Количество прихваток

    @Override //AbstractOpPlate
    public void initViews(OpData opData){
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfParts, this);
        new TFIntegerColored(tfDots, this);
        new TFIntegerColored(tfDrops, this);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpWeldDotted)data;

        countInitialValues();

        currentNormTime = OpWeldDottedCounter.count((OpWeldDotted) data).getMechTime();//результат в минутах

        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        parts = IntegerParser.getValue(tfParts);
        dots = IntegerParser.getValue(tfDots);
        drops = IntegerParser.getValue(tfDrops);

        collectOpData();
    }

    private void collectOpData(){
        opData.setParts(parts);
        opData.setDots(dots);
        opData.setDrops(drops);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpWeldDotted opData = (OpWeldDotted)data;

        parts = opData.getParts();
        tfParts.setText(String.valueOf(parts));

        dots = opData.getDots();
        tfDots.setText(String.valueOf(dots));

        drops = opData.getDrops();
        tfDrops.setText(String.valueOf(drops));
    }

    @Override
    public String helpText() {
        return String.format("КОНДЕНСАТОРНАЯ - установка шпилек, гвоздей, лепестков заземления.\n" +
                        "\tСварка одного элемента за %s мин.\n" +
                        "\n" +
                        "СВАРКА КОНТАКТНАЯ - приварка элементов конструкции на точку.\n" +
                        "\tОдна точка выполняется за %s мин.\n" +
                        "\n" +
                        "СВАРКА НА ПРИХВАТКИ - приварка элементов конструкции на прихватки.\n" +
                        "\tОдна прихватка выполняется за %s мин.\n",

                WELDING_PARTS_SPEED, WELDING_DOTTED_SPEED, WELDING_DROP_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
