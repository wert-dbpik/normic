package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackOnPallet;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

/**
 * УСТАНОВКА НА ПОДДОН
 */
public class PlatePackOnPalletController extends AbstractOpPlate {

    @FXML
    private TextField tfNormTime;

    private OpPackOnPallet opData;

    private int height; //габарит квадратного поддона

    private double polyWrapL; //полипропиленовая лента

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFNormTime(tfNormTime, prevFormController);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpPackOnPallet) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        height = ((FormPackController) prevFormController).getHeight();

        collectOpData();
    }


    private void collectOpData(){
        opData.setHeight(height);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){

        //НИКАКИХ ДЕЙСТВИЙ
    }

    @Override
    public String helpText() {
        return "Установленное на поддон изделие крепится полипропиленовой лентой:\n" +
                "Расход полипропиленовой ленты вычисляется по формуле:\n\n" +
                "\t\t\tL ленты = H x 1.15 x 4 + 2 x D, м,\n" +
                "где\n" +
                "\tH, D - высота и глубина изделия, м.";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
