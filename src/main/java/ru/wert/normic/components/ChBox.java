package ru.wert.normic.components;

import javafx.scene.control.CheckBox;
import ru.wert.normic.AbstractOpPlate;

public class ChBox {


    public ChBox(CheckBox chbx, AbstractOpPlate counter) {

        chbx.selectedProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm();
        });

    }
}
