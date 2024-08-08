package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers.turning.counters.OpLatheMountDismountCounter;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.dataBaseEntities.ops.opTurning.OpLatheMountDismount;

/**
 * УСТАНОВКА И СНЯТИЕ ДЕТАЛИ С ТОКАРНОГО СТАНКА
 */
public class PlateLatheMountDismountController extends AbstractOpPlate {

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

    private OpLatheMountDismountCounter.ELatheHolders holder;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpLatheMountDismount opData = (OpLatheMountDismount) data;

        rbCenters.setToggleGroup(toggleGroup);
        rbHolder.setToggleGroup(toggleGroup);
        rbHolderWithCenter.setToggleGroup(toggleGroup);


        toggleGroup.selectedToggleProperty().addListener((ob, o, n) -> {
            if(rbCenters.isSelected()) holder = OpLatheMountDismountCounter.ELatheHolders.CENTERS;
            else if(rbHolder.isSelected()) holder = OpLatheMountDismountCounter.ELatheHolders.HOLDER;
            else holder = OpLatheMountDismountCounter.ELatheHolders.HOLDER_AND_CENTER;

            countNorm(opData);
            prevFormController.countSumNormTimeByShops();
        });

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            prevFormController.countSumNormTimeByShops();
        });
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheMountDismount) data;

        countInitialValues();

        int length = (((FormDetailController) prevFormController).getMatPatchController()).getParamA();

        currentNormTime = ((length == 0) ? currentNormTime = 0.0 :
                opData.getOpType().getNormCounter().count(data).getMechTime());//результат в минутах

        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        String text = (((FormDetailController) prevFormController).getMatPatchController()).getTfWeight().getText();
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

        holder = OpLatheMountDismountCounter.ELatheHolders.values()[opData.getHolder()];
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
