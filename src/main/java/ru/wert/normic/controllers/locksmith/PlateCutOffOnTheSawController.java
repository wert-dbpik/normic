package ru.wert.normic.controllers.locksmith;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.BXSawType;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.opLocksmith.OpCutOffOnTheSaw;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.EMeasure;
import ru.wert.normic.enums.ESawType;

import java.util.NoSuchElementException;

/**
 * ОТРЕЗАНИЕ ЗАГОТОВКИ НА ПИЛЕ
 */
public class PlateCutOffOnTheSawController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private ComboBox<ESawType> cmbxSaw;

    @FXML
    private TextField tfNormTime;

    private ESawType sawType;
    private int length;

    enum ECutSolidDiameters { //page 123 (Р6М5)
        CUT_SOLID_D10(10, 0.9),
        CUT_SOLID_D20(20, 1.0),
        CUT_SOLID_D30(30, 1.5),
        CUT_SOLID_D40(40, 1.8),
        CUT_SOLID_D60(60, 2.7),
        CUT_SOLID_D80(80, 4.3),
        CUT_SOLID_D90(90, 5.0),
        CUT_SOLID_D100(100, 6.4);

        @Getter int diam;
        @Getter double time;
        ECutSolidDiameters(int diam,  double time){
            this.diam = diam;
            this.time = time;}
    }



    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpCutOffOnTheSaw opData = (OpCutOffOnTheSaw) data;

        new BXSawType().create(cmbxSaw, ESawType.SMALL_SAW, this);


        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpCutOffOnTheSaw opData = (OpCutOffOnTheSaw) data;

        countInitialValues();

        currentNormTime = findTime() + sawType.getSpeed();
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){
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
        sawType = cmbxSaw.getValue();
    }

    private void collectOpData(OpCutOffOnTheSaw opData){
        opData.setSaw(cmbxSaw.getValue());

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutOffOnTheSaw opData = (OpCutOffOnTheSaw)data;

        sawType = opData.getSaw();
        cmbxSaw.setValue(sawType);

    }

}
