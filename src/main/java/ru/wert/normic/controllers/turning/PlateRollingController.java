package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opTurning.OpLatheRolling;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;

/**
 * АКАТЫВАНИЕ ПРОФИЛЯ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateRollingController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private TextField tfDiameter;

    @FXML
    private TextField tfLength;

    private String initStyle;
    private double diameter; //Диаметр прутка
    private int turningDiameter; //Диаметр обработки
    private int paramA; //Длина заготовки
    private int length; //Глубина  точения

    int[] lengths = new int[]{                          10,   20,   30,   40,   50,   75};

    enum ERolling { //page 174 (Р6М5)
        ROLLING_D20(20,0.012, new double[]{0.70, 1.90, 2.40, 2.60, 2.70, 3.00}),
        ROLLING_D30(30,0.016, new double[]{0.80, 2.00, 2.60, 2.90, 3.10, 3.50}),
        ROLLING_D40(40,0.024, new double[]{0.90, 2.10, 2.90, 3.20, 3.50, 4.10}),
        ROLLING_D50(50,0.052, new double[]{2.00, 2.40, 3.30, 3.70, 4.20, 5.50}),
        ROLLING_D75(75,0.08,  new double[]{2.10, 2.70, 3.70, 4.30, 5.00, 7.00}),
        ROLLING_D100(100,0.08,new double[]{2.60, 3.00, 4.20, 5.00, 6.00, 8.00}),
        ROLLING_D125(125,0.1, new double[]{2.70, 3.30, 4.70, 5.50, 6.50, 9.00}),
        ROLLING_D150(150,0.12,new double[]{2.80, 3.70, 5.50, 6.50, 8.00, 11.0});


        @Getter int diameter;
        @Getter double delta;
        @Getter double[] times;
        ERolling(int diameter, double delta, double[] times){
            this.diameter = diameter;
            this.delta = delta;
            this.times = times;}
    }

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpLatheRolling opData = (OpLatheRolling) data;
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
        OpLatheRolling opData = (OpLatheRolling) data;

        countInitialValues();

        currentNormTime = findTime();
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){
        int maxLength = lengths[lengths.length-1];
        if(length > maxLength){
            int prevD = 0;
            for (ERolling eRolling : ERolling.values()) {
                if (diameter >= prevD && diameter <= eRolling.getDiameter()) {
                    return eRolling.getTimes()[eRolling.getTimes().length-1] + eRolling.getDelta() * (length - maxLength);
                }
                prevD = eRolling.getDiameter();
            }
        } else {
            int prevL = 0;
            for (int i = 0; i < lengths.length; i++) {
                if (length >= prevL && length <= lengths[i]) {
                    int prevD = 0;
                    for (ERolling eDiam : ERolling.values()) {
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


    private void collectOpData(OpLatheRolling opData){
        opData.setDiameter(turningDiameter);
        opData.setLength(length);

        opData.setMechTime(currentNormTime);
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
