package ru.wert.normic.controllers.forms;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.AppStatics;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuCalculator;
import ru.wert.normic.components.BXMaterial;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.Material;

import java.util.ArrayList;

import static ru.wert.normic.ChogoriServices.CH_QUICK_MATERIALS;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

public class FormDetailController extends AbstractFormController {

    @FXML @Getter
    private TextField tfDetailName;

    @FXML @Getter
    private ComboBox<Material> cmbxMaterial;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private ImageView ivErase;

    @FXML @Getter
    private TextField tfA, tfB;

    @FXML
    private TextField tfWasteRatio, tfWeight;

    @FXML
    private TextField tfCoat;


    @FXML @Getter
    private TextField tfMechanicalTime, tfPaintingTime, tfTotalTime;

    @FXML
    private Label lblTimeMeasure;

    private double ro; //Плотность
    private double t; //Толщина
    private double wasteRatio; //Коэффициент, учитывающий отход материала
    private int paramA; //параметр А
    private int paramB; //параметр B

    private AbstractFormController controller;

    @Override //AbstractFormController
    public void init(AbstractFormController controller, TextField tfName, OpData opData) {
        this.opData = (OpDetail) opData;
        this.controller = controller;

        //Создаем меню
        createMenu();

        initViews();

        setDragAndDropCellFactory();

        //Инициализируем наименование
        if(tfName != null) {
            ((OpDetail)this.opData).setName(tfName.getText());
            tfDetailName.setText(tfName.getText());
            tfName.textProperty().bindBidirectional(tfDetailName.textProperty());
        }

        //Инициализируем комбобоксы
        new BXMaterial().create(cmbxMaterial, ((OpDetail) opData).getMaterial());


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
                nc.countNorm(nc.getOpData());
            }
        });

        tfA.textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

        tfB.textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

        tfWasteRatio.textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

        ivErase.setOnMouseClicked(e->{
            ((IOpWithOperations)opData).getOperations().clear();
            addedPlates.clear();
            addedOperations.clear();
            listViewTechOperations.getItems().clear();
            countSumNormTimeByShops();
        });
    }

    @Override
    public void createMenu(){
        menu = new MenuCalculator(this, listViewTechOperations, (IOpWithOperations) opData);

        menu.getItems().addAll(menu.createItemAddCutting(), menu.createItemAddBending(), menu.createItemAddLocksmith());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddPainting());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddWeldLongSeam(), menu.createItemAddWeldingDotted());

        tyeMenuToButton();
    }

    private void countWeightAndArea() {
        try {
            ro = cmbxMaterial.getValue().getParamX();
            t = cmbxMaterial.getValue().getParamS();
            paramA = Integer.parseInt(tfA.getText().trim());
            paramB = Integer.parseInt(tfB.getText().trim());
            wasteRatio = Double.parseDouble(tfWasteRatio.getText().trim());
            if(paramA <= 0 || paramB <= 0 || paramA > 2500 || paramB > 2500 || wasteRatio < 1.0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            tfWeight.setText("");
            tfCoat.setText("");
            return;
        }

        double weight = t * paramA * paramB * ro * MM2_TO_M2 * wasteRatio;
        double area = 2 * paramA * paramB * MM2_TO_M2;

        tfWeight.setText(String.format(DOUBLE_FORMAT, weight));
        tfCoat.setText(String.format(DOUBLE_FORMAT, area));

        ((OpDetail)opData).setWeight(weight);
        ((OpDetail)opData).setArea(area);

        controller.calculateAreaByDetails();
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    public void countSumNormTimeByShops(){
        String measure = MIN.getName();

        double mechanicalTime = 0.0;
        double paintingTime = 0.0;

        for(OpData cn: addedOperations){
            mechanicalTime += cn.getMechTime() * cn.getQuantity();
            paintingTime += cn.getPaintTime() * cn.getQuantity();
        }

        opData.setMechTime(mechanicalTime);
        opData.setPaintTime(paintingTime);

        controller.countSumNormTimeByShops();

        if(AppStatics.MEASURE.getValue().equals(SEC)){
            mechanicalTime = mechanicalTime * MIN_TO_SEC;
            paintingTime = paintingTime * MIN_TO_SEC;

            measure = SEC.getName();
        }

        String format = DOUBLE_FORMAT;
        if(AppStatics.MEASURE.getValue().equals(SEC)) format = INTEGER_FORMAT;

        tfMechanicalTime.setText(String.format(format, mechanicalTime));
        tfPaintingTime.setText(String.format(format, paintingTime));

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime ));

        lblTimeMeasure.setText(measure);

    }


    @Override //AbstractFormController
    public void fillOpData(){

        if(((OpDetail)opData).getMaterial() != null)
            cmbxMaterial.setValue(((OpDetail)opData).getMaterial());

        paramA = ((OpDetail)opData).getParamA();
        tfA.setText(String.valueOf(paramA));

        paramB = ((OpDetail)opData).getParamB();
        tfB.setText(String.valueOf(paramB));

        wasteRatio = ((OpDetail)opData).getWasteRatio();
        tfWasteRatio.setText(String.valueOf(wasteRatio));

        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.deployData();
    }



}
