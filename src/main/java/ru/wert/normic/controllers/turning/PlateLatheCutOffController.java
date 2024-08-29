package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.opTurning.OpLatheCutOff;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.utils.IntegerParser;

/**
 * ОТРЕЗАНИЕ ДЕТАЛИ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateLatheCutOffController extends AbstractOpPlate {

    @FXML
    private TextField tfThickness;

    @FXML
    private CheckBox chbxCutOffSolid;

    @FXML
    private TextField tfNormTime;

    private OpLatheCutOff opData;

    private String initStyle;
    private double diameter; //Диаметр заготовки
    private boolean cutOffSolid; //отрезание детали сплошного сечения
    private double thickness; //Глубина  точения

    @Override //AbstractOpPlate
    public void initViews(OpData data){

        tfThickness.disableProperty().bind(chbxCutOffSolid.selectedProperty());

        chbxCutOffSolid.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                thickness = diameter / 2.0;
                tfThickness.setText(String.valueOf(thickness));
            }

            prevFormController.countSumNormTimeByShops();
        });

        new TFIntegerColored(tfThickness, this);


        new TFNormTime(tfNormTime, prevFormController);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheCutOff) data;

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
        thickness = chbxCutOffSolid.isSelected() ?
                diameter / 2 :
                IntegerParser.getValue(tfThickness);
        if(thickness > (diameter / 2))
            tfThickness.setStyle("-fx-border-color: #FF5555");
        else
            tfThickness.setStyle(initStyle);

        collectOpData();
    }

    private void collectOpData(){
        opData.setMaterial(((FormDetailController) prevFormController).getCmbxMaterial().getValue());
        opData.setCutOffSolid(chbxCutOffSolid.isSelected());
        opData.setThickness(thickness);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLatheCutOff opData = (OpLatheCutOff)data;

        cutOffSolid = opData.getCutOffSolid();
        chbxCutOffSolid.setSelected(cutOffSolid);

        thickness = opData.getThickness() == 0.0 ?
                ((FormDetailController) prevFormController).getCmbxMaterial().getValue().getParamS() / 2 :
                opData.getThickness();
        tfThickness.setText(String.valueOf(thickness));

    }

    @Override
    public String helpText() {
        return "Норма времени на отрезание берется из таблиц стандартных норм \n" +
                "в зависимости от вида заготовки: " +
                "прутка (сплошное сечение) - берется половина диаметра материала\n" +
                "или трубы - берется толщина стенки T в мм, мин.";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
