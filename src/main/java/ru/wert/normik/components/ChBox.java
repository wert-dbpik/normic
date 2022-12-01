package ru.wert.normik.components;

import javafx.scene.control.CheckBox;
import ru.wert.normik.AbstractOpPlate;

public class ChBox {


    public ChBox(CheckBox chbx, AbstractOpPlate counter) {

        chbx.selectedProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm();
        });

    }
}
