package ru.wert.normic.controllers._forms;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.AppStatics;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.materials.matlPatches.AbstractMatPatchController;
import ru.wert.normic.menus.MenuForm;
import ru.wert.normic.entities.db_connection.material.Material;

import java.io.IOException;
import java.util.*;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.NormicServices.QUICK_MATERIALS;

/**
 * ДЕТАЛЬ - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ ДЕТАЛИ
 */
public class FormDetailController extends AbstractFormController {

    @FXML @Getter
    private TextField tfDetailName;

    @FXML @Getter
    private TextField tfDetailQuantity;

    @FXML @Getter
    private ComboBox<Material> cmbxMaterial;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private Button btnAddMaterial;

    @FXML @Getter
    private Button btnDone;

    @FXML
    private StackPane spDetailParams;

    @FXML @Getter
    private TextField tfMechanicalTime, tfPaintingTime, tfTotalTime;

    @FXML
    private Label lblTimeMeasure;

    @Getter private AbstractMatPatchController matPatchController;

    private AbstractFormController prevAssmController;



    @Getter private BtnDone done;


    @Override //AbstractFormController
    public void init(AbstractFormController assmController, TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone) {
        this.opData = opData;
        this.prevAssmController = assmController;

        ((IOpWithOperations)opData).setFormController(this);

        initCommon();
        initConnectedFields(tfName, tfQuantity, imgDone);

    }

    /**
     * Вызывается из табличного представления, где деталь не связана с предыдущей формой
     */
    public void init(OpDetail opData){
        this.opData = opData;

        initCommon();
        initConnectedFieldsSeparately();
    }

    private void initCommon() {
        //Инициализируем комбобоксы
        new BXMaterial().create(cmbxMaterial, false, QUICK_MATERIALS.findByName("лист 1"));
        if(((OpDetail)opData).getMaterial() != null)
            cmbxMaterial.setValue(((OpDetail)opData).getMaterial());
        cmbxMaterial.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) return;

            mountMatPatch(oldValue, newValue);
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

        mountMatPatch(null, cmbxMaterial.getValue());

        //Заполняем поля формы
        fillOpData();
        matPatchController.countWeightAndArea();
        MAIN_CONTROLLER.recountMainOpData();

        menu.addEmptyPlate();
    }

    private void initConnectedFieldsSeparately(){
        new TFInteger(tfDetailQuantity);
        tfDetailName.setText(((OpDetail)opData).getName());
        tfDetailQuantity.setText(String.valueOf(opData.getQuantity()));
        done.getStateProperty().setValue(((OpDetail)opData).isDone());
    }


    private void initConnectedFields(TextField tfName, TextField tfQuantity, ImgDouble imgDone){
        //Инициализируем наименование
        if(tfName != null) {
            ((OpDetail)this.opData).setName(tfName.getText());
            tfDetailName.setText(tfName.getText());
            tfName.textProperty().bindBidirectional(tfDetailName.textProperty());
        }
        //Инициализируем количество
        if(tfQuantity != null) {
            ((OpDetail)this.opData).setQuantity(Integer.parseInt(tfQuantity.getText()));
            tfDetailQuantity.setText(tfQuantity.getText());
            tfQuantity.textProperty().bindBidirectional(tfDetailQuantity.textProperty());

        }
        //При изменении значения в поле tfDetailQuantity изменяется общее количество данной детали в изделли (total),
        //что влечет изменение норм времени на некоторые оперции
        tfDetailQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("")) {
                new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
            }

        });

        done.getStateProperty().bindBidirectional(imgDone.getStateProperty());
    }

    /**
     * Устанавливает соответствующую панель для расчета материала
     * @param prevMaterial
     * @param newMaterial
     */
    private void mountMatPatch(Material prevMaterial, Material newMaterial) {
        //Определяем, если материал щтучный - для него нужно менять панель каждый раз

        if(newMaterial.getName().equals(NO_MATERIAL)) {

            cmbxMaterial.setValue(QUICK_MATERIALS.findByName("лист 1"));
            newMaterial = QUICK_MATERIALS.findByName("лист 1");
        }
        boolean isPieceType = EMatType.getTypeByName(newMaterial.getMatType().getName()).equals(EMatType.PIECE);

        EMatType prevMatType = (prevMaterial == null || prevMaterial.getName().equals(NO_MATERIAL)) ?
                null :
                EMatType.getTypeByName(prevMaterial.getMatType().getName());
        EMatType newMatType = EMatType.getTypeByName(newMaterial.getMatType().getName());
        //Нам нужна только смена EMatType и первичная инициализация
        if(isPieceType || !newMatType.equals(prevMatType)) {
            try {
                FXMLLoader loader = null;
                switch (newMatType) {
                    case LIST:
                        loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialPatches/listPatch.fxml"));
                        break;
                    case ROUND:
                        loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialPatches/roundPatch.fxml"));
                        break;
                    case PROFILE:
                        loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialPatches/profilePatch.fxml"));
                        break;
                    case PIECE:
                        loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialPatches/piecePatch.fxml"));
                        break;
                }
                Parent parent = loader.load();
                matPatchController = loader.getController();
                matPatchController.init((OpDetail) getOpData(), this, getAddedPlates());
                spDetailParams.getChildren().clear();
                spDetailParams.getChildren().add(parent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Сохраняем введенные ранее данные
            ((OpDetail) opData).setParamA(matPatchController.getParamA());
            ((OpDetail) opData).setParamB(matPatchController.getParamB());
            ((OpDetail) opData).setParamC(matPatchController.getParamC());
        }

        matPatchController.fillPatchOpData();
    }

    private void initViews() {

        done = new BtnDone(btnDone);

        new BtnAddMaterial(btnAddMaterial, cmbxMaterial);

        MAIN_CONTROLLER.recountMainOpData();
    }

    @Override
    public MenuForm createMenu(){
        menu = new MenuForm(this, listViewTechOperations, (IOpWithOperations) opData);
        EMatType type = EMatType.getTypeByName(cmbxMaterial.getValue().getMatType().getName());

        if(type.equals(EMatType.LIST)){ //ЛИСТЫ
            menu.getItems().add(menu.createItemCutting());
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(menu.createItemDrillingByMarking());
            menu.getItems().add(menu.createItemLocksmith());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPaintDetail());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemWeldLongSeam());
            menu.getItems().add(menu.createItemWeldingDotted());

            Menu simpleOperationsMenu = menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLING));
            if(simpleOperationsMenu != null) {
                menu.getItems().add(new SeparatorMenuItem());
                menu.getItems().add(simpleOperationsMenu);
            }

            deleteImproperOperations(AppStatics.LIST_OPERATIONS);

        } else if (type.equals(EMatType.ROUND)){ //КРУГИ
            menu.getItems().add(menu.createItemMountDismount());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemTurning());
            menu.getItems().add(menu.createItemDrilling());
            menu.getItems().add(menu.createItemCutGroove());
            menu.getItems().add(menu.createItemThreading());
            menu.getItems().add(menu.createItemRolling());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemChopOff());
            menu.getItems().add(menu.createItemCutOffOnTheSaw());
            menu.getItems().add(menu.createItemCutOff());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPaintingOld());

            Menu simpleOperationsMenu = menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLING));
            if(simpleOperationsMenu != null) {
                menu.getItems().add(new SeparatorMenuItem());
                menu.getItems().add(simpleOperationsMenu);
            }

            deleteImproperOperations(AppStatics.ROUND_OPERATIONS);

        } else if (type.equals(EMatType.PROFILE)){ //ПРОФИЛИ
            menu.getItems().add(menu.createItemCutOffOnTheSaw());
            menu.getItems().add(menu.createItemChopOff());
            menu.getItems().add(menu.createItemDrillingByMarking());
            menu.getItems().add(menu.createItemLocksmith());
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPaintingOld());

            Menu simpleOperationsMenu = menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLING));
            if(simpleOperationsMenu != null) {
                menu.getItems().add(new SeparatorMenuItem());
                menu.getItems().add(simpleOperationsMenu);
            }

            deleteImproperOperations(AppStatics.PROFILE_OPERATIONS);
        } else { //ШТУЧНЫЕ
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPaintingOld());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createAllLatheOperations());
            menu.getItems().add(menu.createAllLocksmithOperations());
            menu.getItems().add(menu.createAllWeldingOperations());
            menu.getItems().add(menu.createAllAssmOperations());

            Menu simpleOperationsMenu = menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLING));
            if(simpleOperationsMenu != null) {
                menu.getItems().add(new SeparatorMenuItem());
                menu.getItems().add(simpleOperationsMenu);
            }
        }

        linkMenuToButton();

        return menu;
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

        MAIN_CONTROLLER.recountMainOpData();
    }



    @Override //AbstractFormController
    public void fillOpData(){

        if(((OpDetail)opData).getMaterial() != null)
            cmbxMaterial.setValue(((OpDetail)opData).getMaterial());

        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.addListOfOperations();
    }



}
