package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.dataBaseEntities.ops.OpData;
import ru.wert.normic.dataBaseEntities.ops.opTurning.OpLatheDrilling;
import ru.wert.normic.utils.IntegerParser;

/**
 * СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateLatheDrillingController extends AbstractOpPlate {

    @FXML
    private TextField tfDiameter;

    @FXML
    private TextField tfLength;

    private OpLatheDrilling opData;

    private String initStyle;
    private double diameter; //Диаметр прутка
    private int turningDiameter; //Диаметр обработки
    private int paramA; //Длина заготовки
    private int length; //Глубина  точения



    @Override //AbstractOpPlate
    public void initViews(OpData data){

        initStyle = tfDiameter.getStyle();

        new TFIntegerColored(tfDiameter, this);
        new TFIntegerColored(tfLength, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            prevFormController.countSumNormTimeByShops();
        });

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheDrilling) data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     * При превышении возможных табличных размеров диаметра и длин принимается максимальное значение
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = ((FormDetailController) prevFormController).getCmbxMaterial().getValue().getParamS();
        turningDiameter = IntegerParser.getValue(tfDiameter);

        if(turningDiameter >= diameter)
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
        OpLatheDrilling opData = (OpLatheDrilling)data;

        turningDiameter = opData.getDiameter();
        tfDiameter.setText(String.valueOf(turningDiameter));

        length = opData.getLength();
        tfLength.setText(String.valueOf(length));

    }

    @Override
    public String helpText() {
        return "Осевое сверление заготовки на токарном станке на определенную глубину\n" +
                "или на проход:\n" +
                "\tD диаметр - диаметр сверла, мм; (max = 30 мм)\n" +
                "\tL длина - глубина сверления, мм. (max = 100 мм)\n\n" +
                "Норма времени на точение T сверл. берется из таблиц стандартных норм \n" +
                "\tв зависимости от D и L, мин.";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
