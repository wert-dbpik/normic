package ru.wert.normik.components;

import javafx.scene.control.TextField;
import ru.wert.normik.interfaces.IFormController;

public class TFNormTime {


    public TFNormTime(TextField tf, IFormController controller) {

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.countSumNormTimeByShops();
        });

    }
}
