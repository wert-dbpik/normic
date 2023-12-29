package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import lombok.Getter;
import ru.wert.normic.components.TFDoubleColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers.list.counters.OpBendingCounter;
import ru.wert.normic.controllers.turning.counters.OpCutGrooveCounter;
import ru.wert.normic.entities.ops.opList.OpBending;
import ru.wert.normic.entities.ops.opTurning.OpLatheCutGroove;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;

/**
 * НАРЕЗАНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateCutGrooveController extends AbstractOpPlate {

    @FXML
    private TextField tfDepth;

    private OpLatheCutGroove opData;

    private String initStyle;
    private double diameter; //Диаметр заготовки
    private double depth; //Глубина  точения

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        new TFDoubleColored(tfDepth, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheCutGroove) data;

        countInitialValues();

        currentNormTime = OpCutGrooveCounter.count((OpLatheCutGroove) data).getMechTime();//результат в минутах

        setTimeMeasurement();

    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = ((FormDetailController) formController).getCmbxMaterial().getValue().getParamS();
        depth = DoubleParser.getValue(tfDepth);
        if(depth >= (diameter / 2))
            tfDepth.setStyle("-fx-border-color: #FF5555");
        else
            tfDepth.setStyle(initStyle);

        opData.setDepth(depth);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLatheCutGroove opData = (OpLatheCutGroove)data;

        depth = opData.getDepth();
        tfDepth.setText(String.valueOf(depth));

    }

    @Override
    public String helpText() {
        return "Норма времени на резание паза берется из таблиц стандартных норм \n" +
                "в зависимости от диаметра заготовки (материала) и глубины паза T (мм), мин.";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
