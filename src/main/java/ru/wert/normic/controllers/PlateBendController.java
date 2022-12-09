package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.AbstractOpPlate;
import ru.wert.normic.components.BXBendingTool;
import ru.wert.normic.components.CmBx;
import ru.wert.normic.components.TFColoredInteger;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.entities.OpBending;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IFormController;
import ru.wert.normic.utils.IntegerParser;

public class PlateBendController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfBends;

    @FXML
    private TextField tfMen;

    @FXML
    private ComboBox<EBendingTool> cmbxBendingTool;

    @FXML
    private TextField tfNormTime;

    private IFormController controller;
    private OpBending opData;

    public OpData getOpData(){
        return opData;
    }

    private int bends;
    private int men;
    private double toolRatio;

    public void init(IFormController controller, OpBending opData){
        this.controller = controller;
        this.opData = opData;

        controller.getAddedPlates().add(this);
        controller.getAddedOperations().add(opData);

        new BXBendingTool().create(cmbxBendingTool);

        fillOpData(); //Должен стоять до навешивагия слушателей на TextField

        new TFNormTime(tfNormTime, controller);
        new TFColoredInteger(tfBends, this);
        new TFColoredInteger(tfMen, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new CmBx(cmbxBendingTool, this);

        ivDeleteOperation.setOnMouseClicked(e->{
            controller.getAddedPlates().remove(this);
            VBox box = controller.getListViewTechOperations().getSelectionModel().getSelectedItem();
            controller.getListViewTechOperations().getItems().remove(box);
            currentNormTime = 0.0;
            controller.countSumNormTimeByShops();
        });

        countNorm();
    }


    @Override//AbstractOpPlate
    public void countNorm(){

        countInitialValues();

        final double BENDING_SERVICE_RATIO = 1.25; //коэфффициент, учитывающий 25% времени на обслуживание при гибке
        final double BENDING_SPEED = 0.15; //корость гибки, мин/гиб
        double time;
        time =  bends * BENDING_SPEED * toolRatio * men  //мин
                * BENDING_SERVICE_RATIO;

        currentNormTime = time;
        collectOpData();
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {

        bends = IntegerParser.getValue(tfBends);
        men = IntegerParser.getValue(tfMen);
        toolRatio = cmbxBendingTool.getValue().getToolRatio();
    }

    private void collectOpData(){
        opData.setBends(bends);
        opData.setMen(men);
        opData.setTool(cmbxBendingTool.getValue());

        opData.setMechTime(currentNormTime);
    }

    private void fillOpData(){
        bends = opData.getBends();
        tfBends.setText(String.valueOf(bends));

        men = opData.getMen();
        tfMen.setText(String.valueOf(men));

        if(opData.getTool() != null){
            toolRatio = opData.getTool().getToolRatio();
            cmbxBendingTool.setValue(opData.getTool());
        }

    }


}
