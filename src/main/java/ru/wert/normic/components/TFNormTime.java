package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.entities.ops.OpData;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;

public class TFNormTime {


    public TFNormTime(TextField tf, AbstractFormController controller) {

        tf.textProperty().addListener((observable) -> {

/*
            double mechanicalTime = 0.0;
            double paintingTime = 0.0;
            double assemblingTime = 0.0;
            double packingTime = 0.0;

            for(OpData cn: MAIN_CONTROLLER.getAddedOperations()){
                mechanicalTime += cn.getMechTime() * cn.getQuantity();
                paintingTime += cn.getPaintTime() * cn.getQuantity();
                assemblingTime += cn.getAssmTime() * cn.getQuantity();
                packingTime += cn.getPackTime() * cn.getQuantity();
            }

            OpData op = new OpData();
            op.setMechTime(mechanicalTime);
            op.setPaintTime(paintingTime);
            op.setAssmTime(assemblingTime);
            op.setPackTime(packingTime);

 */

            controller.countSumNormTimeByShops();

        });

    }
}
