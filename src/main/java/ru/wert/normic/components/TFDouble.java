package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.controllers.AbstractOpPlate;

/**
 * Поле допускает ввод [0-9.,]
 * нажатие [,] интерпретирует как точку [.]
 */
public class TFDouble {

    public TFDouble(TextField tf) {
        tf.setOnKeyTyped(e->{
            if(tf.isFocused() && !e.getCharacter().matches("[0-9.,]"))
                e.consume();
            else if(tf.isFocused() && e.getCharacter().matches("[.]")) {
                int pos = tf.getCaretPosition();
                tf.setText(tf.getText().concat(","));
                tf.positionCaret(pos+1);
                e.consume();
            }
        });
    }
}
