package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opTurning.OpLatheMountDismount;
import ru.wert.normic.enums.EOpType;

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

    private double weight; //Масса заготовки

    private ToggleGroup toggleGroup = new ToggleGroup();
    private double[] weights =       new double[]{0.3,  1.0,  3.0,  5.0,  10.0,  20.0};
    private ELatheHolders holder;


    enum ELatheHolders {
        CENTERS             (new double[]{0.17, 0.25, 0.35, 0.40, 0.50, 0.61}),
        HOLDER              (new double[]{0.58, 0.81, 1.05, 1.25, 1.50, 1.80}),
        HOLDER_AND_CENTER   (new double[]{0.33, 0.45, 0.61, 0.70, 0.84, 1.05});

        @Getter double[] times;

        ELatheHolders(double[] times){
            this.times = times;}
    }

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpLatheMountDismount opData = (OpLatheMountDismount) data;
        ivOperation.setImage(EOpType.LATHE_MOUNT_DISMOUNT.getLogo());

        rbCenters.setToggleGroup(toggleGroup);
        rbHolder.setToggleGroup(toggleGroup);
        rbHolderWithCenter.setToggleGroup(toggleGroup);


        toggleGroup.selectedToggleProperty().addListener((ob, o, n) -> {
            if(rbCenters.isSelected()) holder = ELatheHolders.CENTERS;
            else if(rbHolder.isSelected()) holder = ELatheHolders.HOLDER;
            else holder = ELatheHolders.HOLDER_AND_CENTER;

            countNorm(opData);
            formController.countSumNormTimeByShops();
        });

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpLatheMountDismount opData = (OpLatheMountDismount) data;

        countInitialValues();

        currentNormTime = findTime();
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){
        double countW = Math.min(weight, 20.0);
        double prevW = 0;
        for (int i = 0; i < weights.length; i++) {
            if (countW >= prevW && countW <= weights[i]) {
                return holder.getTimes()[i];
            }
            prevW = weights[i];
        }
        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        String text = (((FormDetailController)formController).getMatPatchController()).getTfWeight().getText();
        weight = text == null || text.isEmpty() ? 0.0 : Double.parseDouble(text.replace(",", "."));

    }


    private void collectOpData(OpLatheMountDismount opData){
        opData.setHolder(holder.ordinal());

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLatheMountDismount opData = (OpLatheMountDismount)data;

        holder = ELatheHolders.values()[opData.getHolder()];
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
