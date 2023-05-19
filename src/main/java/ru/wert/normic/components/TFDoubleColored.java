package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.utils.DoubleParser;

/**
 * Класс перехватывает ошибочно введенные значения,
 * а так же ограничивает введение чисел только цифрами от 0 до 9 и знаками , и .
 * запятую меняет на точку
 */
public class TFDoubleColored {

    public TFDoubleColored(TextField tf, AbstractOpPlate counter) {
        String style = tf.getStyle();

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) {
                tf.setStyle("-fx-border-color: #FF5555");
                return;
            }

            try {
                DoubleParser.getValue(tf);
                tf.setStyle(style);
                counter.countNorm(counter.getOpData());
            } catch (Exception e) {
                tf.setStyle("-fx-border-color: #FF5555");
//                return;
            }
        });

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
