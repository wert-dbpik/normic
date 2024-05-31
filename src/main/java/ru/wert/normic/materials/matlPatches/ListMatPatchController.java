package ru.wert.normic.materials.matlPatches;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.DOUBLE_FORMAT;
import static ru.wert.normic.controllers.AbstractOpPlate.MM2_TO_M2;

/**
 * Класс описывающий поведение панели листового материала
 */
public class ListMatPatchController extends AbstractMatPatchController {

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

        paramB = opData.getParamB();
        getTfB().setText(String.valueOf(paramB));

        wasteRatio = opData.getWasteRatio();
        getTfWasteRatio().setText(String.valueOf(wasteRatio));

        tfWeight.setText(String.valueOf(opData.getWeight()));
        tfCoat.setText(String.valueOf(opData.getArea()));
    }


    @Override
    public void countWeightAndArea() {
        try {
            ro = detailController.getCmbxMaterial().getValue().getParamX();
            t = detailController.getCmbxMaterial().getValue().getParamS();
            paramA = Integer.parseInt(tfA.getText().trim());
            paramB = Integer.parseInt(tfB.getText().trim());
            wasteRatio = Double.parseDouble(tfWasteRatio.getText().trim());
            if(paramA <= 0 || paramB <= 0 || paramA > 2500 || paramB > 2500 || wasteRatio < 1.0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            tfWeight.setText("");
            tfCoat.setText("");
            return;
        }

        double weight = t * paramA * paramB * ro * MM2_TO_M2 * wasteRatio;
        double area = 2 * paramA * paramB * MM2_TO_M2;

        tfWeight.setText(String.format(DOUBLE_FORMAT, weight));
        tfCoat.setText(String.format(DOUBLE_FORMAT, area));

        opData.setWeight(roundTo001(weight));
        opData.setArea(roundTo001(area));

//        detailController.calculateAreaByDetails();
    }
}
