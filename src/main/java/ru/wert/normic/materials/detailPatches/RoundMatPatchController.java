package ru.wert.normic.materials.detailPatches;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import ru.wert.normic.controllers.forms.FormDetailController;

import static ru.wert.normic.controllers.AbstractOpPlate.*;

public class RoundMatPatchController extends AbstractMatPatchController {

    @FXML@Getter
    private TextField tfA;

    @FXML@Getter
    private TextField tfB; //Не используется!

    @FXML@Getter
    private TextField tfWasteRatio;

    @FXML@Getter
    private TextField tfWeight;

    @FXML@Getter
    private TextField tfCoat;

    private final double cut = 5.0;  //Ширина реза отрезного инструмента

    @Override
    public void fillPatchOpData() {

        paramA = opData.getParamA();
        getTfA().setText(String.valueOf(paramA));

        paramB = 5;
        getTfB().setText(String.valueOf(paramB));

        wasteRatio = opData.getWasteRatio();
        getTfWasteRatio().setText(String.valueOf(wasteRatio));
    }

    @Override
    public void countWeightAndArea() {
        try {
            t = detailController.getCmbxMaterial().getValue().getParamS(); //Диаметр
            ro = detailController.getCmbxMaterial().getValue().getParamX(); //Масса погонного метра
            paramA = Integer.parseInt(tfA.getText().trim()); //Длина отрезанной заготовки
            paramB = 0;
            wasteRatio = Double.parseDouble(tfWasteRatio.getText().trim());
            if(paramA <= 0 || paramA > 6000 || wasteRatio < 1.0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            tfWeight.setText("");
            tfCoat.setText("");
            return;
        }

        double weight = (paramA + cut) * MM_TO_M * ro * wasteRatio;
        double area = 3.14 * t * (paramA + cut) * MM2_TO_M2;

        tfWeight.setText(String.format(DOUBLE_FORMAT, weight));
        tfCoat.setText(String.format(DOUBLE_FORMAT, area));

        opData.setWeight(weight);
        opData.setArea(area);

        detailController.calculateAreaByDetails();
    }
}
