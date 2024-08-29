package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.controllers._forms.AbstractFormController;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;

public class TFNormTime {


    public TFNormTime(TextField tf, AbstractFormController controller) {

        tf.textProperty().addListener((observable) -> {
            controller.countSumNormTimeByShops();
        });

    }
}
