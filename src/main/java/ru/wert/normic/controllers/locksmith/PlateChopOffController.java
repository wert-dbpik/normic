package ru.wert.normic.controllers.locksmith;


import javafx.scene.image.Image;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opLocksmith.OpChopOff;
import ru.wert.normic.enums.EMeasure;
import ru.wert.normic.enums.EOpType;

import java.util.NoSuchElementException;

import static ru.wert.normic.entities.settings.NormConstants.CHOP_SPEED;

/**
 * ОТРУБАНИЕ НА ГЕКЕ
 */
public class PlateChopOffController extends AbstractOpPlate {

    private int length;
    private double chopTime = 0.05;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpChopOff opData = (OpChopOff) data;
        ivOperation.setImage(EOpType.CHOP_OFF.getLogo());

        countInitialValues();

        currentNormTime = findTime() + chopTime;
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime() {
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

    }


    private void collectOpData(OpChopOff opData){

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpChopOff opData = (OpChopOff)data;

        //НИКАКИХ ДЕЙСТВИЙ
    }

    @Override
    public String helpText() {
        return String.format("Рубка в размер производится на Геке\n\n" +
                "Норма времени на рубку вычисляется по формуле:\n\n" +
                "\t\t\tT руб = T изм + V руб,\n" +
                "где\n" +
                "\tT изм - время на проведение измерений; \n" +
                "\tV руб = %s - скорость рубки; \n",
                CHOP_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
