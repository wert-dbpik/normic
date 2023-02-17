package ru.wert.normic.controllers.turning_plates;


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
import ru.wert.normic.entities.OpTurning;
import ru.wert.normic.entities.OpWeldContinuous;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;


public class PlateTurningController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfTurningLength;

    @FXML
    private TextField tfNumOfPassings;

    @FXML
    private TextField tfNormTime;

    private String initStyle;
    private int paramA; //Длина заготовки
    private int length; //Длина точения
    private double diameter; //Диаметр заготовки
    private int passages; //Число токарных проходов

    int[] lengths = new int[]{      25,   50,   75,   100,  125,  150,  200, 250, 300, 350, 400, 450, 500, 550, 600};

    enum EDiameters {
        D20(20,   new double[]{0.66, 0.70, 0.75, 0.81, 0.88, 0.96, 1.05}),
        D30(30,   new double[]{0.72, 0.77, 0.83, 0.90, 0.98, 1.07, 1.17 }),
        D50(50,   new double[]{0.73, 0.80, 0.87, 0.95, 1.35, 1.40, 1.52}),
        D75(75,   new double[]{0.80, 0.90, 1.00, 1.15, 1.60, 1.70, 1.90, 2.15}),
        D100(100, new double[]{0.83, 0.96, 1.10, 1.25, 1.80, 1.90, 2.15, 2.45, 2.75}),
        D125(125, new double[]{1.25, 1.40, 1.60, 1.80, 2.30, 2.40, 2.75, 3.10, 3.50, 3.90, 4.30}),
        D155(155, new double[]{1.30, 1.50, 1.70, 1.95, 2.50, 2.60, 3.05, 3.50, 3.90, 4.40, 4.90, 5.50}),
        D200(200, new double[]{1.35, 1.60, 1.85, 2.20, 2.80, 2.90, 3.50, 4.00, 4.70, 5.00, 5.50, 6.50, 7.00, 8.00, 8.50});

        @Getter int diam;
        @Getter double[] times;
        EDiameters(int diam,  double[] times){
            this.diam = diam;
            this.times = times;}
    }

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpTurning opData = (OpTurning) data;
        initStyle = tfTurningLength.getStyle(); //Сохраняем исходный стиль

        new TFIntegerColored(tfTurningLength, this);
        new TFIntegerColored(tfNumOfPassings, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpTurning opData = (OpTurning) data;

        countInitialValues();

        currentNormTime = findTime() * passages;
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){
        double countD = Math.min(diameter, 200.0);
        int prevL = 0;
        for (int i = 0; i < lengths.length; i++) {
            if (length > prevL && length <= lengths[i]) {
                int prevD = 0;
                for (EDiameters eDiam : EDiameters.values()) {
                    if (countD > prevD && countD <= eDiam.getDiam()) {
                        if(i < eDiam.getTimes().length)
                            return eDiam.getTimes()[i];
                        else
                            //Возвращаем самое последнее значение в массиве
                            return eDiam.getTimes()[eDiam.getTimes().length - 1];
                    }
                    prevD = eDiam.getDiam();
                }
            }
            prevL = lengths[i];
        }
        throw new NoSuchElementException("Ошибка при определении значения нормы времени в таблице");
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        diameter = ((FormDetailController)formController).getCmbxMaterial().getValue().getParamS();

        paramA = ((FormDetailController) formController).getMatPatchController().getParamA();
        length = IntegerParser.getValue(tfTurningLength);
        if(length > paramA)
            tfTurningLength.setStyle("-fx-border-color: #FF5555");
        else
            tfTurningLength.setStyle(initStyle);

        passages = IntegerParser.getValue(tfNumOfPassings);

    }

    private void collectOpData(OpTurning opData){
        opData.setLength(length);
        opData.setPassages(passages);

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpTurning opData = (OpTurning)data;

        length = opData.getLength() == 0 ?
                Integer.parseInt(((FormDetailController)formController).getMatPatchController().getTfA().getText()) :
                opData.getLength();
        tfTurningLength.setText(String.valueOf(length));

        passages = opData.getPassages();
        tfNumOfPassings.setText(String.valueOf(passages));

    }

}
