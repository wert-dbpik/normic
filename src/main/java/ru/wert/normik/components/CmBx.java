package ru.wert.normik.components;

import javafx.scene.control.ComboBox;
import ru.wert.normik.AbstractOpPlate;

public class CmBx {


    public CmBx(ComboBox<?> cmbx, AbstractOpPlate counter) {

        cmbx.valueProperty().addListener((observable, oldValue, newValue) -> {
            counter.countNorm();
        });

    }
}
