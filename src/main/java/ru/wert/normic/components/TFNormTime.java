package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.interfaces.IFormController;

public class TFNormTime {


    public TFNormTime(TextField tf, IFormController controller) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.countSumNormTimeByShops();
        });

    }
}
