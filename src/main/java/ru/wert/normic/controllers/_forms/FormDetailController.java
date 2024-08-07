package ru.wert.normic.controllers._forms;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.AppStatics;
import ru.wert.normic.decoration.Decoration;
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

import static ru.wert.normic.AppStatics.CURRENT_MEASURE;
import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.NormicServices.QUICK_MATERIALS;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.*;

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

    @FXML
    private Button btnDone;

    @FXML
    private StackPane spDetailParams;

    @FXML @Getter
    private TextField tfMechanicalTime, tfPaintingTime, tfTotalTime;

    @FXML
    private Label lblTimeMeasure;

    @Getter private AbstractMatPatchController matPatchController;

    private AbstractFormController controller;

    @Override //AbstractFormController
    public void init(AbstractFormController controller, TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone) {
        this.opData = (OpDetail) opData;
        this.controller = controller;

        BtnDone done = new BtnDone(btnDone);
        done.getStateProperty().bindBidirectional(imgDone.getStateProperty());

        //Инициализируем комбобоксы
        new BXMaterial().create(cmbxMaterial);
        if(((OpDetail) opData).getMaterial() != null)
            cmbxMaterial.setValue(((OpDetail) opData).getMaterial());
        cmbxMaterial.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) return;
//            if(EMatType.getTypeByName(newValue.getMatType().getName()).equals(EMatType.PIECE))

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

        //Инициализируем наименование
        if(tfName != null) {
            ((OpDetail)this.opData).setName(tfName.getText());
            tfDetailName.setText(tfName.getText());
            tfName.textProperty().bindBidirectional(tfDetailName.textProperty());
        }

        new TFInteger(tfDetailQuantity);

        //Инициализируем количество
        if(tfQuantity != null) {
            ((OpDetail)this.opData).setQuantity(Integer.parseInt(tfQuantity.getText()));
            tfDetailQuantity.setText(tfQuantity.getText());
            tfQuantity.textProperty().bindBidirectional(tfDetailQuantity.textProperty());
        }

        mountMatPatch(null, cmbxMaterial.getValue());

        //Заполняем поля формы
        fillOpData();
        matPatchController.countWeightAndArea();
        countSumNormTimeByShops();

        menu.addEmptyPlate();

    }

    /**
     * Устанавливает соответствующую панель для расчета материала
     * @param prevMaterial
     * @param newMaterial
     */
    private void mountMatPatch(Material prevMaterial, Material newMaterial) {
        //Определяем, если материал щтучный - для него нужно менять панель каждый раз
        boolean isPieceType = EMatType.getTypeByName(newMaterial.getMatType().getName()).equals(EMatType.PIECE);

        EMatType prevMatType = (prevMaterial == null) ? null : EMatType.getTypeByName(prevMaterial.getMatType().getName());
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
        btnAddMaterial.setGraphic(new ImageView(new Image(String.valueOf(getClass().getResource("/pics/btns/materials.png")), 18,18, true, true)));
        btnAddMaterial.setTooltip(new Tooltip("Добавить материал"));
        btnAddMaterial.setOnAction(e->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/materials/materialsTV.fxml"));
                Parent parent = loader.load();
                Decoration decoration = new Decoration(
                        "МАТЕРИАЛЫ",
                        parent,
                        false,
                        (Stage) ((Node)e.getSource()).getScene().getWindow(),
                        "decoration-settings",
                        false,
                        false);

                decoration.getWindow().setOnHiding(r->{
                    Material chosenMaterial = cmbxMaterial.getValue();
                    ObservableList<Material> materials = FXCollections.observableArrayList(QUICK_MATERIALS.findAll());
                    materials.sort(Comparator.comparing(Material::getName));
                    cmbxMaterial.getItems().clear();
                    cmbxMaterial.getItems().addAll(materials);
                    if(!materials.contains(chosenMaterial)) chosenMaterial = materials.get(0);
                    cmbxMaterial.getSelectionModel().select(chosenMaterial);
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        tfTotalTime.textProperty().addListener((observable, oldValue, newValue) -> {
            countSumNormTimeByShops();
        });
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
            menu.getItems().add(menu.createItemPainting());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemWeldLongSeam());
            menu.getItems().add(menu.createItemWeldingDotted());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_MECHANICAL)));

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
            menu.getItems().add(menu.createItemPainting());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_MECHANICAL)));

            deleteImproperOperations(AppStatics.ROUND_OPERATIONS);

        } else if (type.equals(EMatType.PROFILE)){ //ПРОФИЛИ
            menu.getItems().add(menu.createItemCutOffOnTheSaw());
            menu.getItems().add(menu.createItemChopOff());
            menu.getItems().add(menu.createItemDrillingByMarking());
            menu.getItems().add(menu.createItemLocksmith());
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPainting());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_MECHANICAL)));

            deleteImproperOperations(AppStatics.PROFILE_OPERATIONS);
        } else { //ШТУЧНЫЕ
            menu.getItems().add(menu.createItemBending());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createItemPainting());
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(menu.createAllLatheOperations());
            menu.getItems().add(menu.createAllLocksmithOperations());
            menu.getItems().add(menu.createAllWeldingOperations());
            menu.getItems().add(menu.createAllAssmOperations());
            menu.getItems().add(menu.createAllSimpleOperations(Arrays.asList(ENormType.NORM_MECHANICAL, ENormType.NORM_ASSEMBLE)));

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

        countSumNormTimeByShops();
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    public void countSumNormTimeByShops(){
        String measure = MIN.getMeasure();

        double mechanicalTime = 0.0;
        double paintingTime = 0.0;
        double assmTime = 0.0;

        for(OpData cn: addedOperations){
            mechanicalTime += cn.getMechTime() * cn.getQuantity();
            paintingTime += cn.getPaintTime() * cn.getQuantity();
            assmTime += cn.getAssmTime() * cn.getQuantity();
        }

        opData.setMechTime(roundTo001(mechanicalTime));
        opData.setPaintTime(roundTo001(paintingTime));
        opData.setAssmTime(roundTo001(assmTime));

        controller.countSumNormTimeByShops();

        if(CURRENT_MEASURE.equals(SEC)){
            mechanicalTime = mechanicalTime * MIN_TO_SEC;
            paintingTime = paintingTime * MIN_TO_SEC;
            assmTime = assmTime * MIN_TO_SEC;

            measure = SEC.getMeasure();
        }
        if(CURRENT_MEASURE.equals(HOUR)){
            mechanicalTime = mechanicalTime * MIN_TO_HOUR;
            paintingTime = paintingTime * MIN_TO_HOUR;
            assmTime = assmTime * MIN_TO_HOUR;

            measure = HOUR.getMeasure();
        }

        String format = DOUBLE_FORMAT;
        if(AppStatics.MEASURE.getSelectedToggle().getUserData().equals(SEC.name())) format = INTEGER_FORMAT;

        tfMechanicalTime.setText(String.format(format, mechanicalTime).trim());
        tfPaintingTime.setText(String.format(format, paintingTime).trim());
//        tfAssmTime.setText(String.format(format, paintingTime).trim());

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assmTime).trim());

        lblTimeMeasure.setText(measure);

    }


    @Override //AbstractFormController
    public void fillOpData(){

        if(((OpDetail)opData).getMaterial() != null)
            cmbxMaterial.setValue(((OpDetail)opData).getMaterial());

        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.addListOfOperations();
    }



}
