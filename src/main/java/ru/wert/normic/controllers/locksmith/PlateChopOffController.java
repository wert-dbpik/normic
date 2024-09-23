package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opLocksmith.OpChopOff;
import ru.wert.normic.enums.EOpType;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.CHOP_SPEED;

/**
 * ОТРУБАНИЕ НА ГЕКЕ
 */
public class PlateChopOffController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    private OpChopOff opData;

    private int length;

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFNormTime(tfNormTime, prevFormController);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpChopOff) data;
        ivOperation.setImage(EOpType.CHOP_OFF.getLogo());

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
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
