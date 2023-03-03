package ru.wert.normic.components;

import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import ru.wert.normic.controllers.AbstractOpPlate;

public class RadBtn {


    public RadBtn(RadioButton rb, AbstractOpPlate counter) {

        rb.selectedProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

    }
}
