package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import ru.wert.normic.components.BXSawType;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.opLocksmith.OpCutOffOnTheSaw;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EMeasure;
import ru.wert.normic.enums.ESawType;

import java.util.NoSuchElementException;

import static ru.wert.normic.settings.NormConstants.*;

/**
 * ОТРЕЗАНИЕ ЗАГОТОВКИ НА ПИЛЕ
 */
public class PlateCutOffOnTheSawController extends AbstractOpPlate {

    @FXML
    private ComboBox<ESawType> cmbxSaw;

    private ESawType sawType;
    private int length;


    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new BXSawType().create(cmbxSaw, ESawType.SMALL_SAW, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpCutOffOnTheSaw opData = (OpCutOffOnTheSaw) data;

        countInitialValues();

        currentNormTime = findTime() + sawType.getSpeed();
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){
        EMeasure lastMeasure = EMeasure.values()[EMeasure.values().length-1];
        if(length > lastMeasure.getLength())
            return lastMeasure.getTime();

        int prevL = 0;
        for (EMeasure d : EMeasure.values()) {
            if (length >= prevL && length <= d.getLength())
                return d.getTime();
            prevL = d.getLength();
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        length = ((FormDetailController) formController).getMatPatchController().getParamA();
        sawType = cmbxSaw.getValue();
    }


    private void collectOpData(OpCutOffOnTheSaw opData){
        opData.setSaw(cmbxSaw.getValue());

        opData.setMechTime(currentNormTime);
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
