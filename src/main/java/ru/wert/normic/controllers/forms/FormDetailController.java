package ru.wert.normic.controllers.forms;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.AppStatics;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.materials.matlPatches.AbstractMatPatchController;
import ru.wert.normic.menus.MenuCalculator;
import ru.wert.normic.components.BXMaterial;
import ru.wert.normic.entities.*;
import ru.wert.normic.entities.db_connection.material.Material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @FXML
    private StackPane spDetailParams;

    @FXML @Getter
    private TextField tfMechanicalTime, tfPaintingTime, tfTotalTime;

    @FXML
    private Label lblTimeMeasure;

    @Getter private AbstractMatPatchController matPatchController;

    private AbstractFormController controller;

    @Override //AbstractFormController
    public void init(AbstractFormController controller, TextField tfName, OpData opData) {
        this.opData = (OpDetail) opData;
        this.controller = controller;

        //Инициализируем комбобоксы
        new BXMaterial().create(cmbxMaterial);
        cmbxMaterial.valueProperty().addListener((observable, oldValue, newValue) -> {
            mountMatPatch(newValue);
            createMenu();
            matPatchController.countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

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


        mountMatPatch(cmbxMaterial.getValue());

        //Заполняем поля формы
        fillOpData();
        matPatchController.countWeightAndArea();
        countSumNormTimeByShops();

    }

    private void mountMatPatch(Material material) {
        EMatType matType = EMatType.getTypeByName(material.getMatType().getName());
        try {
            FXMLLoader loader = null;
            switch (matType){
                case LIST:
                    loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialPatches/listPatch.fxml")); break;
                case ROUND:
                    loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialPatches/roundPatch.fxml")); break;
                case PROFILE:
                    loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialPatches/profilePatch.fxml"));break;
            }
            assert loader != null;
            Parent parent = loader.load();
            matPatchController = loader.getController();
            matPatchController.init((OpDetail) getOpData(), this, getAddedPlates());
            spDetailParams.getChildren().clear();
            spDetailParams.getChildren().add(parent);

//            fillOpData();
            matPatchController.fillPatchOpData();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {

        tfTotalTime.textProperty().addListener((observable, oldValue, newValue) -> {
            countSumNormTimeByShops();
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
        EMatType type = EMatType.getTypeByName(cmbxMaterial.getValue().getMatType().getName());

        if(type.equals(EMatType.LIST)){ //ЛИСТЫ
            menu.getItems().addAll(menu.createItemAddCutting(), menu.createItemAddBending(), menu.createItemAddLocksmith());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().addAll(menu.createItemAddPainting());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().addAll(menu.createItemAddWeldLongSeam(), menu.createItemAddWeldingDotted());

            deleteImproperOperations(AppStatics.LIST_OPERATIONS);

        } else if (type.equals(EMatType.ROUND)){ //КРУГИ
            menu.getItems().addAll(menu.createItemAddMountDismount());
            menu.getItems().addAll(menu.createItemAddTurning());
            menu.getItems().addAll(menu.createItemAddCutGroove());
            menu.getItems().addAll(menu.createItemAddThreading());
            menu.getItems().addAll(menu.createItemAddDrilling());
            menu.getItems().addAll(menu.createItemAddRolling());
            menu.getItems().addAll(menu.createItemAddCutOff());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().addAll(menu.createItemAddPainting());

            deleteImproperOperations(AppStatics.ROUND_OPERATIONS);

        } else { //ПРОФИЛИ
            menu.getItems().addAll(menu.createItemAddPainting());

            deleteImproperOperations(AppStatics.PROFILE_OPERATIONS);
        }

        linkMenuToButton();
    }

    /**
     * Метод удаляет операции не подходящие под операцию
     */
    private void deleteImproperOperations(List<EOpType> properOperations) {
        //Корректируем список операции, удаляем несовместимые
        List<OpData> operations = new ArrayList<>(getAddedOperations());
        if(getListViewTechOperations() == null || getListViewTechOperations().getItems().isEmpty()
        ) return;

        for (OpData op : operations) {
            if (!properOperations.contains(op.getOpType())) {
                int index = getAddedOperations().indexOf(op);

                getListViewTechOperations().getItems().remove(index);
                addedPlates.remove(index);
                getAddedOperations().remove(index);
            }
        }

        countSumNormTimeByShops();
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

//        matPatchController.fillPatchOpData();

        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.deployData();
    }



}
