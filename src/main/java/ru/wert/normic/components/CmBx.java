package ru.wert.normic.components;

import javafx.scene.control.ComboBox;
import ru.wert.normic.controllers.AbstractOpPlate;

public class CmBx {


    public CmBx(ComboBox<?> cmbx, AbstractOpPlate counter) {

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm(counter.getOpData());
        });

    }
}
