package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.opLocksmith.OpChopOff;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EMeasure;

import java.util.NoSuchElementException;

/**
 * ОТРУБАНИЕ НА ГЕКЕ
 */
public class PlateChopOffController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfNormTime;

    private int length;
    private double chopTime = 0.05;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpChopOff opData = (OpChopOff) data;

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpChopOff opData = (OpChopOff) data;

        countInitialValues();

        currentNormTime = findTime() + chopTime;
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime() {
        EMeasure lastMeasure = EMeasure.values()[EMeasure.values().length-1];
        if(length > lastMeasure.getLength())
            return lastMeasure.getTime();

        int prevL = 0;
        for (EMeasure d : EMeasure.values()) {
            if (length >= prevL && length <= d.getLength())
                return d.getTime();

            prevL = d.getLength();
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        length = ((FormDetailController) formController).getMatPatchController().getParamA();

    }

    private void collectOpData(OpChopOff opData){

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpChopOff opData = (OpChopOff)data;

        //НИКАКИХ ДЕЙСТВИЙ
    }

}
