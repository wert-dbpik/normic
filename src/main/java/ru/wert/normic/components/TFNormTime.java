package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.controllers._forms.AbstractFormController;

public class TFNormTime {


    public TFNormTime(TextField tf, AbstractFormController controller) {

        tf.textProperty().addListener((observable) -> {
            controller.countSumNormTimeByShops();
        });

    }
}
