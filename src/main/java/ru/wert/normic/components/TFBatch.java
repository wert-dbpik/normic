package ru.wert.normic.components;

import javafx.scene.control.TextField;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.MainController;

import static ru.wert.normic.AppStatics.CURRENT_BATCH;

public class TFBatch {


    public TFBatch(TextField tf, MainController counter) {
        String style = tf.getStyle();
        String normStyle = counter == null ? "" : counter.getTfBatch().getStyle();

        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("") || Integer.parseInt(newValue) == 0) {
                tf.setStyle("-fx-border-color: #ff5555");
                if (counter != null) {
                    counter.getTfBatch().setStyle("-fx-border-color: #FF5555");
                }
                return;
            }
            tf.setStyle(style);
            if (counter != null) {
                counter.getTfBatch().setStyle(normStyle);
                //Пересчитываем с учетом новой партии
                CURRENT_BATCH = Integer.parseInt(newValue);
                counter.recountMainOpData();
            }
        });

        tf.setOnKeyTyped(e->{
            if(tf.isFocused() && !e.getCharacter().matches("[0-9]"))
                e.consume();
        });
    }
}
