package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.controllers._plates.AbstractOpPlate;

/**
 * Поле сохраняется при изменении
 */
public class TfString {

    public TfString(TextField tf, AbstractOpPlate counter) {
        tf.textProperty().addListener((observable) -> {
            counter.countNorm(counter.getOpData());
        });
    }
}
