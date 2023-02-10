package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.controllers.AbstractOpPlate;

/**
 * Поле допускает ввод [0-9]
 */
public class TFInteger {

    public TFInteger(TextField tf) {
        tf.setOnKeyTyped(e->{
            if(tf.isFocused() && !e.getCharacter().matches("[0-9]"))
                e.consume();
        });
    }
}
