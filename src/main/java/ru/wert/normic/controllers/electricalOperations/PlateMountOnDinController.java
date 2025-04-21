package ru.wert.normic.controllers.electricalOperations;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.electrical.OpMountOnDin;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * УСТАНОВКА НА ДИНРЕЙКУ
 */
public class PlateMountOnDinController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfAvtomats;

    @FXML
    private TextField tfHeaters;

    private OpMountOnDin opData;

    private int avtomats; //Количество автоматов
    private int heaters; //Количество нагревателей

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfAvtomats, this);
        new TFIntegerColored(tfHeaters, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpMountOnDin) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {
        heaters = IntegerParser.getValue(tfHeaters);
        avtomats = IntegerParser.getValue(tfAvtomats);

        collectOpData();
    }

    private void collectOpData(){
        opData.setHeaters(heaters);
        opData.setAvtomats(avtomats);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpMountOnDin opData = (OpMountOnDin)data;

        avtomats = opData.getAvtomats();
        tfAvtomats.setText(String.valueOf(avtomats));

        heaters = opData.getHeaters();
        tfHeaters.setText(String.valueOf(heaters));
    }

    @Override
    public String helpText() {
        return String.format("Установка на динрейку автоматов, термостатов, УЗО,\n" +
                        "\tкоммутаторов, зажимов и т.п устанавливается за %s мин/элемент.\n" +
                        "\n" +
                        "Установка на динрейку нагревателей, счетчиков.\n" +
                        "\tустанавливается за %s мин/элемент.\n",

                MOUNT_ON_DIN_AUTOMATS, MOUNT_ON_DIN_HEATERS);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
