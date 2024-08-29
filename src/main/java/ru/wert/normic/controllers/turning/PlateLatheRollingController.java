package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opTurning.OpLatheRolling;
import ru.wert.normic.utils.IntegerParser;

/**
 * АКАТЫВАНИЕ ПРОФИЛЯ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateLatheRollingController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private TextField tfDiameter;

    @FXML
    private TextField tfLength;

    @FXML
    private TextField tfNormTime;

    private OpLatheRolling opData;

    private String initStyle;
    private double diameter; //Диаметр прутка
    private int turningDiameter; //Диаметр обработки
    private int paramA; //Длина заготовки
    private int length; //Глубина  точения



    @Override //AbstractOpPlate
    public void initViews(OpData data){
        initStyle = tfDiameter.getStyle(); //Сохраняем исходный стиль

        new TFNormTime(tfNormTime, prevFormController);
        new TFIntegerColored(tfDiameter, this);
        new TFIntegerColored(tfLength, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheRolling) data;

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
        turningDiameter = IntegerParser.getValue(tfDiameter);
        if(turningDiameter > diameter)
            tfDiameter.setStyle("-fx-border-color: #FF5555");
        else
            tfDiameter.setStyle(initStyle);

        paramA = ((FormDetailController) prevFormController).getMatPatchController().getParamA();
        length = IntegerParser.getValue(tfLength);
        if(length > paramA)
            tfLength.setStyle("-fx-border-color: #FF5555");
        else
            tfLength.setStyle(initStyle);

        collectOpData();
    }

    private void collectOpData(){
        opData.setDiameter(turningDiameter);
        opData.setLength(length);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLatheRolling opData = (OpLatheRolling)data;

        turningDiameter = opData.getDiameter();
        tfDiameter.setText(String.valueOf(turningDiameter));

        length = opData.getLength();
        tfLength.setText(String.valueOf(length));

    }

    @Override
    public String helpText() {
        return "Норма времени на накатывание рифления берется из таблиц стандартных норм \n" +
                "в зависимости от диаметра обработки D (мм) и длины накатывания L(мм), мин.";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
