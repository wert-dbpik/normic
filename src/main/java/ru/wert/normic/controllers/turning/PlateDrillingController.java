package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDrilling;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;

/**
 * СВЕРЛЕНИЕ ОТВЕРСТИЯ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateDrillingController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfDiameter;

    @FXML
    private TextField tfLength;

    @FXML
    private TextField tfNormTime;

    private String initStyle;
    private double diameter; //Диаметр прутка
    private int turningDiameter; //Диаметр обработки
    private int paramA; //Длина заготовки
    private int length; //Глубина  точения

    int[] lengths = new int[]{                 10,   20,   40,   60,   80,   100};
    private double delta = 0.01;

    enum EDrilling { //page 174 (Р6М5)
        DRILLING_D6(6,   new double[]{0.30, 0.40, 0.70, 1.10, 1.40, 1.60}),
        DRILLING_D8(8,   new double[]{0.40, 0.40, 0.70, 1.20, 1.50, 1.70}),
        DRILLING_D10(10, new double[]{0.40, 0.50, 0.80, 1.20, 1.60, 1.70}),
        DRILLING_D12(12, new double[]{0.40, 0.50, 0.90, 1.30, 1.80, 1.90}),
        DRILLING_D16(16, new double[]{0.40, 0.50, 0.90, 1.30, 1.80, 1.90}),
        DRILLING_D20(20, new double[]{0.50, 0.70, 1.00, 1.50, 2.00, 2.40}),
        DRILLING_D25(5,  new double[]{0.60, 0.90, 1.20, 1.70, 2.20, 2.70}),
        DRILLING_D30(30, new double[]{0.80, 1.20, 1.60, 2.30, 3.00, 3.60});


        @Getter int diameter;
        @Getter double[] times;
        EDrilling(int diameter, double[] times){
            this.diameter = diameter;
            this.times = times;}
    }

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpDrilling opData = (OpDrilling) data;
        initStyle = tfDiameter.getStyle(); //Сохраняем исходный стиль

        new TFIntegerColored(tfDiameter, this);
        new TFIntegerColored(tfLength, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpDrilling opData = (OpDrilling) data;

        countInitialValues();

        currentNormTime = findTime();
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){
        int maxLength = lengths[lengths.length-1];
        if(length > maxLength){
            int prevD = 0;
            for (EDrilling eDrilling : EDrilling.values()) {
                if (diameter >= prevD && diameter <= eDrilling.getDiameter()) {
                    return eDrilling.getTimes()[eDrilling.getTimes().length-1] + delta * (length - maxLength);
                }
                prevD = eDrilling.getDiameter();
            }
        } else {
            int prevL = 0;
            for (int i = 0; i < lengths.length; i++) {
                if (length >= prevL && length <= lengths[i]) {
                    int prevD = 0;
                    for (EDrilling eDiam : EDrilling.values()) {
                        if (diameter >= prevD && diameter <= eDiam.getDiameter()) {
                            if (i < eDiam.getTimes().length)
                                return eDiam.getTimes()[i];
                            else
                                //Возвращаем самое последнее значение в массиве
                                return eDiam.getTimes()[eDiam.getTimes().length - 1];
                        }
                        prevD = eDiam.getDiameter();
                    }
                }
                prevL = lengths[i];
            }
        }

        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = ((FormDetailController) formController).getCmbxMaterial().getValue().getParamS();
        turningDiameter = IntegerParser.getValue(tfDiameter);
        if(turningDiameter > diameter)
            tfDiameter.setStyle("-fx-border-color: #FF5555");
        else
            tfDiameter.setStyle(initStyle);

        paramA = ((FormDetailController) formController).getMatPatchController().getParamA();
        length = IntegerParser.getValue(tfLength);
        if(length > paramA)
            tfLength.setStyle("-fx-border-color: #FF5555");
        else
            tfLength.setStyle(initStyle);
    }

    private void collectOpData(OpDrilling opData){
        opData.setDiameter(turningDiameter);
        opData.setLength(length);

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpDrilling opData = (OpDrilling)data;

        turningDiameter = opData.getDiameter();
        tfDiameter.setText(String.valueOf(turningDiameter));

        length = opData.getLength();
        tfLength.setText(String.valueOf(length));

    }

}
