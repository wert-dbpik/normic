package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers.turning.counters.OpLatheMountDissmountCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opTurning.OpLatheMountDismount;

import java.util.NoSuchElementException;

/**
 * УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
 */
public class PlateMountDismountController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private RadioButton rbHolder;

    @FXML
    private RadioButton rbHolderWithCenter;

    @FXML
    private RadioButton rbCenters;

    private OpLatheMountDismount opData;

    private double weight; //Масса заготовки

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private OpLatheMountDissmountCounter.ELatheHolders holder;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpLatheMountDismount opData = (OpLatheMountDismount) data;

        rbCenters.setToggleGroup(toggleGroup);
        rbHolder.setToggleGroup(toggleGroup);
        rbHolderWithCenter.setToggleGroup(toggleGroup);


        toggleGroup.selectedToggleProperty().addListener((ob, o, n) -> {
            if(rbCenters.isSelected()) holder = OpLatheMountDissmountCounter.ELatheHolders.CENTERS;
            else if(rbHolder.isSelected()) holder = OpLatheMountDissmountCounter.ELatheHolders.HOLDER;
            else holder = OpLatheMountDissmountCounter.ELatheHolders.HOLDER_AND_CENTER;

            countNorm(opData);
            formController.countSumNormTimeByShops();
        });

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheMountDismount) data;

        countInitialValues();

        int length = (((FormDetailController) formController).getMatPatchController()).getParamA();

        currentNormTime = ((length == 0) ? currentNormTime = 0.0 :
                OpLatheMountDissmountCounter.count((OpLatheMountDismount) data).getMechTime());//результат в минутах

        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        String text = (((FormDetailController)formController).getMatPatchController()).getTfWeight().getText();
        weight = text == null || text.isEmpty() ? 0.0 : Double.parseDouble(text.replace(",", "."));

        collectOpData();
    }

    private void collectOpData(){
        opData.setWeight(weight);
        opData.setHolder(holder.ordinal());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLatheMountDismount opData = (OpLatheMountDismount)data;

        holder = OpLatheMountDissmountCounter.ELatheHolders.values()[opData.getHolder()];
        switch (holder.ordinal()){
            case 0 : rbCenters.setSelected(true); break;
            case 1 : rbHolder.setSelected(true); break;
            case 2 : rbHolderWithCenter.setSelected(true); break;
        }

    }

    @Override
    public String helpText() {
        return "Норма времени на установку берется из таблиц стандартных норм \n" +
                "в зависимости от расчетной массы заготовки, мин.";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
