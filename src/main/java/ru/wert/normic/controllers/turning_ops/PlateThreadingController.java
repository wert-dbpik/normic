package ru.wert.normic.controllers.turning_ops;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.forms.FormDetailController;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpThreading;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;


public class PlateThreadingController extends AbstractOpPlate {

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

    int[] lengths = new int[]{               10,   15,   20,   25,   30,   40};

    enum EThreading {
        THREADING_D6(6,   new double[]{0.95, 1.05, 1.20, 1.25, 1.35, 1.45}), //Плашкой стр155 (Р6М5)
        THREADING_D8(8,   new double[]{0.95, 1.05, 1.20, 1.30, 1.40, 1.55}), //Плашкой стр155 (Р6М5)
        THREADING_D10(10, new double[]{1.00, 1.15, 1.25, 1.35, 1.45, 1.65}), //Плашкой стр155 (Р6М5)
        THREADING_D12(12, new double[]{1.05, 1.15, 1.30, 1.45, 1.55, 1.76}), //Плашкой стр155 (Р6М5)
        THREADING_D16(16, new double[]{1.05, 1.15, 1.45, 1.55, 1.65, 1.85}), //Плашкой стр155 (Р6М5)

        THREADING_D24(24, new double[]{2.20, 2.40, 2.60, 2.80, 3.00, 3.20}), //Резцом стр143 (Р6М5)
        THREADING_D30(30, new double[]{2.30, 2.50, 2.70, 2.90, 3.10, 3.30}); //Резцом стр143 (Р6М5)


        @Getter int diameter;
        @Getter double[] times;
        EThreading(int diameter,  double[] times){
            this.diameter = diameter;
            this.times = times;}
    }

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpThreading opData = (OpThreading) data;
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
        OpThreading opData = (OpThreading) data;

        countInitialValues();

        currentNormTime = findTime();
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){
        int maxLength = lengths[lengths.length-1];
        if(length > maxLength){
            int prevD = 0;
            for (EThreading eDiam : EThreading.values()) {
                if (diameter >= prevD && diameter <= eDiam.getDiameter()) {
                    return eDiam.getTimes()[eDiam.getTimes().length-1] + 0.02 * (length - maxLength);
                }
                prevD = eDiam.getDiameter();
            }
        } else {
            int prevL = 0;
            for (int i = 0; i < lengths.length; i++) {
                if (length >= prevL && length <= lengths[i]) {
                    int prevD = 0;
                    for (EThreading eDiam : EThreading.values()) {
                        if (diameter >= prevD && diameter <= eDiam.getDiameter()) {
                            return eDiam.getTimes()[i];
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

    private void collectOpData(OpThreading opData){
        opData.setDiameter(turningDiameter);
        opData.setLength(length);

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpThreading opData = (OpThreading)data;

        turningDiameter = opData.getDiameter();
        tfDiameter.setText(String.valueOf(turningDiameter));

        length = opData.getLength();
        tfLength.setText(String.valueOf(length));

    }

}
