package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFDoubleColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.opTurning.OpLatheCutGroove;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.utils.DoubleParser;

/**
 * НАРЕЗАНИЕ КАНАВКИ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateLatheCutGrooveController extends AbstractOpPlate {

    @FXML
    private TextField tfDepth;

    @FXML
    private TextField tfNormTime;

    private OpLatheCutGroove opData;

    private String initStyle;
    private double diameter; //Диаметр заготовки
    private double depth; //Глубина  точения

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        new TFDoubleColored(tfDepth, this);
        new TFNormTime(tfNormTime, prevFormController);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheCutGroove) data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

        setTimeMeasurement();

    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = ((FormDetailController) prevFormController).getCmbxMaterial().getValue().getParamS();
        depth = DoubleParser.getValue(tfDepth);
        if(depth >= (diameter / 2))
            tfDepth.setStyle("-fx-border-color: #FF5555");
        else
            tfDepth.setStyle(initStyle);

        collectOpData();
    }

    private void collectOpData(){
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
