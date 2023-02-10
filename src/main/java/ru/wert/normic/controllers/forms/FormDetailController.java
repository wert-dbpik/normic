package ru.wert.normic.controllers.forms;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.AppStatics;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.materials.detailPatches.AbstractMatPatchController;
import ru.wert.normic.menus.MenuCalculator;
import ru.wert.normic.components.BXMaterial;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.material.Material;

import java.io.IOException;

import static ru.wert.normic.NormicServices.QUICK_MATERIALS;
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

    @FXML
    private StackPane spDetailParams;

    @FXML @Getter
    private TextField tfMechanicalTime, tfPaintingTime, tfTotalTime;

    @FXML
    private Label lblTimeMeasure;

    private AbstractMatPatchController abstractMatPatchController;

    private AbstractMatPatchController matPatchController;

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
        new BXMaterial().create(cmbxMaterial);
        cmbxMaterial.valueProperty().addListener((observable, oldValue, newValue) -> {
            EMatType matType = EMatType.getTypeByName(newValue.getMatType().getName());
            changeMatPatch(matType);
            matPatchController.countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });
        Material initMaterial = QUICK_MATERIALS.findByName("лист 1");
        if(initMaterial == null) cmbxMaterial.getSelectionModel().select(0);
        else  cmbxMaterial.setValue(initMaterial);

        //Заполняем поля формы
        fillOpData();
        matPatchController.countWeightAndArea();
        countSumNormTimeByShops();
    }

    private void changeMatPatch(EMatType newValue) {
        try {
            FXMLLoader loader = null;
            switch (newValue){
                case LIST:
                    loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialPatches/listPatch.fxml")); break;
                case ROUND:
                    loader = new FXMLLoader(getClass().getResource("fxml/materials/materialPatches/roundPatch.fxml")); break;
                case PROFILE:
                    loader = new FXMLLoader(getClass().getResource("fxml/materials/materialPatches/profilePatch.fxml"));break;
            }
            assert loader != null;
            Parent parent = loader.load();
            matPatchController = loader.getController();
            spDetailParams.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {

        tfTotalTime.textProperty().addListener((observable, oldValue, newValue) -> {
            countSumNormTimeByShops();
        });

//        cmbxMaterial.valueProperty().addListener((observable, oldValue, newValue) -> {
//            matPatchController.countWeightAndArea();
//            for(AbstractOpPlate nc : addedPlates){
//                nc.countNorm(nc.getOpData());
//            }
//        });


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

        linkMenuToButton();
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

        matPatchController.setParamA(((OpDetail)opData).getParamA());
        tfA.setText(String.valueOf(matPatchController.getParamA()));

        matPatchController.setParamB(((OpDetail)opData).getParamB());
        tfB.setText(String.valueOf(matPatchController.getParamB()));

        matPatchController.setWasteRatio(((OpDetail)opData).getWasteRatio());
        tfWasteRatio.setText(String.valueOf(matPatchController.getWasteRatio()));

        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.deployData();
    }



}
