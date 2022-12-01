package ru.wert.normik.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normik.AbstractOpPlate;
import ru.wert.normik.components.BXBendingTool;
import ru.wert.normik.components.CmBx;
import ru.wert.normik.components.TFColoredInteger;
import ru.wert.normik.components.TFNormTime;
import ru.wert.normik.entities.OpBending;
import ru.wert.normik.entities.OpData;
import ru.wert.normik.enums.EBendingTool;
import ru.wert.normik.enums.ETimeMeasurement;
import ru.wert.normik.interfaces.IFormController;
import ru.wert.normik.utils.IntegerParser;

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
    private ETimeMeasurement measure;

    public void init(IFormController controller, OpBending opData){
        this.controller = controller;
        this.opData = opData;

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

        controller.getAddedPlates().add(this);
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
        setTimeMeasurement(measure);
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {

        bends = IntegerParser.getValue(tfBends);
        men = IntegerParser.getValue(tfMen);
        toolRatio = cmbxBendingTool.getValue().getToolRatio();
        measure = controller.getCmbxTimeMeasurement().getValue();
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
