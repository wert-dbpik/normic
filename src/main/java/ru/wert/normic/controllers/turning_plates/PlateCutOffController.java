package ru.wert.normic.controllers.turning_plates;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.forms.FormDetailController;
import ru.wert.normic.entities.OpCutOff;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpTurning;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;


public class PlateCutOffController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfThickness;

    @FXML
    private CheckBox chbxCutOffSolid;

    @FXML
    private TextField tfNormTime;

    private String initStyle;
    private double diameter; //Диаметр заготовки
    private boolean cutOffSolid; //отрезание детали сплошного сечения
    private double thickness; //Глубина  точения

    enum ECutSolidDiameters { //page 123 (Р6М5)
        CUT_SOLID_D10(10, 0.9),
        CUT_SOLID_D20(20, 1.0),
        CUT_SOLID_D30(30, 1.5),
        CUT_SOLID_D40(40, 1.8),
        CUT_SOLID_D60(60, 2.7),
        CUT_SOLID_D80(80, 4.3),
        CUT_SOLID_D90(90, 5.0),
        CUT_SOLID_D100(100, 6.4);

        @Getter int diam;
        @Getter double time;
        ECutSolidDiameters(int diam,  double time){
            this.diam = diam;
            this.time = time;}
    }

    enum ECutPipeThicknesses { //page 124 (Р6М5)
        CUT_PIPE_T05(5, 0.9),
        CUT_PIPE_T10(10, 1.0),
        CUT_PIPE_T20(20, 1.3),
        CUT_PIPE_T30(30, 2.5),
        CUT_PIPE_T40(40, 3.5),
        CUT_PIPE_T50(50, 4.3),
        CUT_PIPE_T60(60, 5.5),
        CUT_PIPE_T70(70, 8.5),
        CUT_PIPE_T80(80, 9.5),
        CUT_PIPE_T90(90, 13.5);

        @Getter int thickness;
        @Getter double time;
        ECutPipeThicknesses(int thickness, double time){
            this.thickness = thickness;
            this.time = time;}
    }


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpCutOff opData = (OpCutOff) data;
        initStyle = tfThickness.getStyle(); //Сохраняем исходный стиль

        tfThickness.disableProperty().bind(chbxCutOffSolid.selectedProperty());

        chbxCutOffSolid.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                thickness = diameter / 2.0;
                tfThickness.setText(String.valueOf(thickness));
            }

            formController.countSumNormTimeByShops();
        });

        new TFIntegerColored(tfThickness, this);


        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpCutOff opData = (OpCutOff) data;

        countInitialValues();

        currentNormTime = findTime();
        collectOpData(opData);
        setTimeMeasurement();
    }

    private Double findTime(){
        if(chbxCutOffSolid.isSelected()){
            int prevD = 0;
            for(ECutSolidDiameters d : ECutSolidDiameters.values()){
                if(diameter > prevD && diameter <= d.getDiam())
                    return d.getTime();
                prevD = d.getDiam();
            }
        } else {
            int prevT = 0;
            for(ECutPipeThicknesses d : ECutPipeThicknesses.values()){
                if(thickness > prevT && thickness <= d.getThickness())
                    return d.getTime();
                prevT = d.getThickness();
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
        thickness = chbxCutOffSolid.isSelected() ?
                diameter / 2 :
                IntegerParser.getValue(tfThickness);
        if(thickness > (diameter / 2))
            tfThickness.setStyle("-fx-border-color: #FF5555");
        else
            tfThickness.setStyle(initStyle);
    }

    private void collectOpData(OpCutOff opData){
        opData.setCutOffSolid(chbxCutOffSolid.isSelected());
        opData.setThickness(thickness);

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutOff opData = (OpCutOff)data;

        cutOffSolid = opData.getCutOffSolid();
        chbxCutOffSolid.setSelected(cutOffSolid);

        thickness = opData.getThickness() == 0.0 ?
                ((FormDetailController) formController).getCmbxMaterial().getValue().getParamS() / 2 :
                opData.getThickness();
        tfThickness.setText(String.valueOf(thickness));

    }

}
