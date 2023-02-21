package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.TFDoubleColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.OpCutGroove;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;

/**
 * НАРЕЗАНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateCutGrooveController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfDepth;

    @FXML
    private TextField tfNormTime;

    private String initStyle;
    private double diameter; //Диаметр заготовки
    private double depth; //Глубина  точения

    enum ECutGroove { //page 129 (Р6М5)
        CUT_GROOVE_T2(2, 0.8),
        CUT_SOLID_D20(5, 0.9),
        CUT_SOLID_D30(10, 1.0),
        CUT_SOLID_D40(15, 1.4),
        CUT_SOLID_D60(20, 1.6),
        CUT_SOLID_D80(25, 5.5),
        CUT_SOLID_D90(30, 6.5),
        CUT_SOLID_D100(35, 8.5);

        @Getter int depth;
        @Getter double time;
        ECutGroove(int depth, double time){
            this.depth = depth;
            this.time = time;}
    }

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpCutGroove opData = (OpCutGroove) data;
        initStyle = tfDepth.getStyle(); //Сохраняем исходный стиль

        new TFDoubleColored(tfDepth, this);


        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpCutGroove opData = (OpCutGroove) data;

        countInitialValues();

        currentNormTime = findTime();
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){

            int prevD = 0;
            for(ECutGroove d : ECutGroove.values()){
                if(depth >= prevD && depth <= d.getDepth())
                    return d.getTime();
                prevD = d.getDepth();
            }


        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = ((FormDetailController) formController).getCmbxMaterial().getValue().getParamS();
        depth = IntegerParser.getValue(tfDepth);
        if(depth > (diameter / 2))
            tfDepth.setStyle("-fx-border-color: #FF5555");
        else
            tfDepth.setStyle(initStyle);
    }

    private void collectOpData(OpCutGroove opData){
        opData.setDepth(depth);

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutGroove opData = (OpCutGroove)data;

        depth = opData.getDepth();
        tfDepth.setText(String.valueOf(depth));

    }

}
