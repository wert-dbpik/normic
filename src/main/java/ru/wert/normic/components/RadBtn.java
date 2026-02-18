package ru.wert.normic.components;

import javafx.scene.control.RadioButton;
import ru.wert.normic.controllers._plates.AbstractOpPlate;

public class RadBtn {


    public RadBtn(RadioButton rb, AbstractOpPlate counter) {

        rb.selectedProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

    }
}
