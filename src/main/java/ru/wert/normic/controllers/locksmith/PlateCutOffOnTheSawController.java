package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import ru.wert.normic.components.BXSawType;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.opLocksmith.OpCutOffOnTheSaw;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.ESawType;

import static ru.wert.normic.settings.NormConstants.*;

/**
 * ОТРЕЗАНИЕ ЗАГОТОВКИ НА ПИЛЕ
 */
public class PlateCutOffOnTheSawController extends AbstractOpPlate {

    @FXML
    private ComboBox<ESawType> cmbxSaw;

    private OpCutOffOnTheSaw opData;

    private ESawType sawType;
    private int length;


    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new BXSawType().create(cmbxSaw, ESawType.SMALL_SAW, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            prevFormController.countSumNormTimeByShops();
        });

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpCutOffOnTheSaw) data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

        setTimeMeasurement();
    }



    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        length = ((FormDetailController) prevFormController).getMatPatchController().getParamA();
        sawType = cmbxSaw.getValue();

        collectOpData();
    }


    private void collectOpData(){
        opData.setLength(length);
        opData.setSaw(cmbxSaw.getValue());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutOffOnTheSaw opData = (OpCutOffOnTheSaw)data;

        sawType = opData.getSaw();
        cmbxSaw.setValue(sawType);

    }

    @Override
    public String helpText() {
        return String.format("Норма времени на резку заготовки на пиле вычисляется по формуле:\n\n" +
                        "\t\t\tT рез = T изм + V рез,\n" +
                        "где\n" +
                        "\tT изм - время на проведение измерений; \n" +
                        "\tV рез = %s - скорость резания на малой пиле и\n" +
                        "\t\t%s на большой пиле. \n",
                SMALL_SAWING_SPEED, BIG_SAWING_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
