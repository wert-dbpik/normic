package ru.wert.normic.materials.matlPatches;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;

public class ProfileMatPatchController extends AbstractMatPatchController {

    @FXML@Getter
    private TextField tfA;

    @FXML@Getter
    private TextField tfB;

    @FXML@Getter
    private TextField tfWasteRatio;

    @FXML@Getter
    private TextField tfWeight;

    @FXML@Getter
    private TextField tfCoat;

    @Override
    public void fillPatchOpData() {

        paramA = opData.getParamA();
        getTfA().setText(String.valueOf(paramA));

        paramB = 5;
        getTfB().setText(String.valueOf(paramB));

        wasteRatio = opData.getWasteRatio();
        getTfWasteRatio().setText(String.valueOf(wasteRatio));

        tfWeight.setText(String.valueOf(opData.getWeight()));
        tfCoat.setText(String.valueOf(opData.getArea()));
    }


    @Override
    public void countWeightAndArea() {

    }
}
