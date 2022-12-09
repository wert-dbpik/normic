package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.AbstractOpPlate;

public class TFColoredInteger{


    public TFColoredInteger(TextField tf, AbstractOpPlate counter) {
        String style = tf.getStyle();
        String normStyle = counter == null ? "" : counter.getTfNormTime().getStyle();

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("")) {
                tf.setStyle("-fx-border-color: #FF5555");
                if (counter != null) {
                    counter.getTfNormTime().setStyle("-fx-border-color: #FF5555");
                }
                return;
            }
            tf.setStyle(style);
            if (counter != null) {
                counter.getTfNormTime().setStyle(normStyle);
                counter.countNorm(counter.getOpData());
            }
        });

        tf.setOnKeyTyped(e->{
            if(tf.isFocused() && !e.getCharacter().matches("[0-9]"))
                e.consume();
        });
    }
}
