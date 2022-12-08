package ru.wert.normic.controllers.forms;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.AbstractOpPlate;
import ru.wert.normic.AppStatics;
import ru.wert.normic.MenuCalculator;
import ru.wert.normic.components.BXMaterial;
import ru.wert.normic.components.BXTimeMeasurement;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.Material;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IFormController;

import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

public class FormDetailController implements IFormController {

    @FXML @Getter
    private TextField tfDetailName;

    @FXML @Getter
    private ComboBox<Material> cmbxMaterial;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML
    private ImageView ivAddOperation;

    @FXML
    private ImageView ivErase;

    @FXML
    private ImageView ivHelpOnPartParameters;

    @FXML @Getter
    private TextField tfA;

    @FXML @Getter
    private TextField tfB;

    @FXML
    private ImageView ivHelpOnWeight;

    @FXML
    private TextField tfWeight;

    @FXML
    private TextField tfCoat;

    @FXML
    private ImageView ivHelpOnTechnologicalProcessing;

    @FXML @Getter
    private TextField tfMechanicalTime;

    @FXML @Getter
    private TextField tfPaintingTime;

    @FXML
    private Label lblTimeMeasure;

    @FXML @Getter
    private TextField tfTotalTime;

    private MenuCalculator menu;

    @Getter //IFormController
    private OpDetail opData;

    private double ro; //Плотность
    private double t; //Толщина
    private int paramA; //параметр А
    private int paramB; //параметр B

    @Getter private ObservableList<AbstractOpPlate> addedPlates;
    @Getter private List<OpData> addedOperations;
    private IFormController controller;

    @Override //IFormController
    public void init(IFormController controller, TextField tfName, OpData opData) {
        this.opData = (OpDetail) opData;
        this.controller = controller;

        //Инициализируем список операционных плашек
        addedPlates = FXCollections.observableArrayList();
        addedOperations = new ArrayList<>();

        //Создаем меню
        createMenu();

        initViews();

        //Инициализируем наименование
        if(tfName != null) {
            this.opData.setName(tfName.getText());
            tfDetailName.setText(tfName.getText());
            tfName.textProperty().bindBidirectional(tfDetailName.textProperty());
        }

        //Инициализируем комбобоксы
        new BXMaterial().create(cmbxMaterial);

        //Заполняем поля формы
        fillOpData();
        countWeightAndArea();
        countSumNormTimeByShops();
    }

    private void initViews() {

        tfTotalTime.textProperty().addListener((observable, oldValue, newValue) -> {
            countSumNormTimeByShops();
        });

        cmbxMaterial.valueProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm();
            }
        });

        tfA.textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm();
            }
        });

        tfB.textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm();
            }
        });
    }


    private void createMenu(){
        menu = new MenuCalculator(this, addedPlates, listViewTechOperations, addedOperations);

        menu.getItems().addAll(menu.createItemAddCutting(), menu.createItemAddBending(), menu.createItemAddLocksmith());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddWeldLongSeam(), menu.createItemAddWeldingDotted());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddPainting());

        ivAddOperation.setOnMouseClicked(e->{
            menu.show(ivAddOperation, Side.LEFT, -15.0, 30.0);
        });
    }

    private void countWeightAndArea() {
        try {
            ro = cmbxMaterial.getValue().getParamX();
            t = cmbxMaterial.getValue().getParamS();
            paramA = Integer.parseInt(tfA.getText().trim());
            paramB = Integer.parseInt(tfB.getText().trim());
            if(paramA <= 0 || paramB <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            tfWeight.setText("");
            tfCoat.setText("");
            return;
        }

        double weight = t * paramA * paramB * ro * MM2_TO_M2 * 1.1;
        double area = 2 * paramA * paramB * MM2_TO_M2;

        tfWeight.setText(String.format(doubleFormat, weight));
        tfCoat.setText(String.format(doubleFormat, area));
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    public void countSumNormTimeByShops(){
        String measure = MIN.getTimeName();

        double mechanicalTime = 0.0;
        double paintingTime = 0.0;

        for(OpData cn: addedOperations){
            mechanicalTime += cn.getMechTime();
            paintingTime += cn.getPaintTime();
        }

        opData.setMechTime(mechanicalTime);
        opData.setPaintTime(paintingTime);

        controller.countSumNormTimeByShops();

        if(AppStatics.MEASURE.getValue().equals(SEC)){
            mechanicalTime = mechanicalTime * MIN_TO_SEC;
            paintingTime = paintingTime * MIN_TO_SEC;

            measure = SEC.getTimeName();
        }

        String format = doubleFormat;
        if(AppStatics.MEASURE.getValue().equals(SEC)) format = integerFormat;

        tfMechanicalTime.setText(String.format(format, mechanicalTime));
        tfPaintingTime.setText(String.format(format, paintingTime));

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime ));

        lblTimeMeasure.setText(measure);

    }

    private void deployData(OpDetail opData) {
        List<OpData> operations = opData.getOperations();
        for (OpData op : operations) {
            switch (op.getOpType()) {
                case DETAIL:
                    menu.addDetailPlate((OpDetail) op);
                    break;
                case ASSM:
                    menu.addAssmPlate((OpAssm) op);
                    break;
                case CUTTING:
                    menu.addCattingPlate((OpCutting) op);
                    break;
                case BENDING:
                    menu.addBendingPlate((OpBending) op);
                    break;
                case LOCKSMITH:
                    menu.addLocksmithPlate((OpLocksmith) op);
                    break;
                case PAINTING:
                    menu.addPaintPlate((OpPaint) op);
                    break;
                case PAINT_ASSM:
                    menu.addPaintAssmPlate((OpPaintAssm) op);
                    break;
                case WELD_CONTINUOUS:
                    menu.addWeldContinuousPlate((OpWeldContinuous) op);
                    break;
                case WELD_DOTTED:
                    menu.addWeldDottedPlate((OpWeldDotted) op);
                    break;
                case ASSM_CUTTINGS:
                    menu.addAssmCuttingsPlate((OpAssmCutting) op);
                    break;
                case ASSM_NUTS:
                    menu.addAssmNutsPlate((OpAssmNut) op);
                    break;
                case ASSM_NODES:
                    menu.addAssmNodesPlate((OpAssmNode) op);
                    break;
                case LEVELING_SEALER:
                    menu.addLevelingSealerPlate((OpLevelingSealer) op);
                    break;
            }
        }
    }

    private void fillOpData(){

        if(opData.getMaterial() != null)
            cmbxMaterial.setValue(opData.getMaterial());

        paramA = opData.getParamA();
        tfA.setText(String.valueOf(paramA));

        paramB = opData.getParamB();
        tfB.setText(String.valueOf(paramB));

        if(!opData.getOperations().isEmpty())
            deployData(opData);
    }



}
