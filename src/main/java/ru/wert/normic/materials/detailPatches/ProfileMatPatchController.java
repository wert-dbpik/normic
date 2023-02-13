package ru.wert.normic.materials.detailPatches;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import ru.wert.normic.controllers.forms.FormDetailController;

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
    public void fillOpData() {

    }

    @Override
    public void countWeightAndArea() {

    }
}
