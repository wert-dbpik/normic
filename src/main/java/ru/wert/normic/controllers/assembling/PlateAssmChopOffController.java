package ru.wert.normic.controllers.assembling;


import javafx.scene.image.Image;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.dataBaseEntities.ops.opAssembling.OpAssmChopOff;
import ru.wert.normic.enums.EOpType;

import static ru.wert.normic.settings.NormConstants.CHOP_SPEED;

/**
 * ОТРУБАНИЕ НА СБОРОЧНОМ УЧАСТКЕ
 */
public class PlateAssmChopOffController extends AbstractOpPlate {

    private OpAssmChopOff opData;

    private int length;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            prevFormController.countSumNormTimeByShops();
        });
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpAssmChopOff) data;
        ivOperation.setImage(EOpType.CHOP_OFF.getLogo());

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getAssmTime();//результат в минутах

        setTimeMeasurement();
    }



    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        length = ((FormDetailController) prevFormController).getMatPatchController().getParamA();

        collectOpData();
    }


    private void collectOpData(){

        opData.setLength(length);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpAssmChopOff opData = (OpAssmChopOff)data;

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
