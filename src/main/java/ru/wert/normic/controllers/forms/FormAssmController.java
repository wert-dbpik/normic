package ru.wert.normic.controllers.forms;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.AppStatics;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuCalculator;
import ru.wert.normic.entities.*;
import ru.wert.normic.enums.ETimeMeasurement;


import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

public class FormAssmController extends AbstractFormController {

    @FXML @Getter
    private TextField tfAssmName;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML
    private Button btnAddOperation;

    @FXML
    private ImageView ivErase;

    @FXML
    private ImageView ivHelpOnTechnologicalProcessing;

    @FXML
    private TextField tfMechanicalTime;

    @FXML
    private TextField tfPaintingTime;

    @FXML
    private TextField tfAssemblingTime;

    @FXML
    private TextField tfPackingTime;

    @FXML
    private Label lblTimeMeasure;

    @FXML @Getter
    private TextField tfTotalTime;

    private AbstractFormController controller;

    @Override
    public void init(AbstractFormController controller, TextField tfName, OpData opData) {
        this.opData = (OpAssm) opData;
        this.controller = controller;

        //Инициализируем список операционных плашек
        addedPlates = FXCollections.observableArrayList();
        addedOperations = new ArrayList<>();

        //Создаем меню
        createMenu();

        initViews();

        //Инициализируем наименование
        if(tfName != null) {
            ((OpAssm)this.opData).setName(tfName.getText());
            tfAssmName.setText(tfName.getText());
            tfAssmName.textProperty().bindBidirectional(tfName.textProperty());
        }

        //Заполняем поля формы
        fillOpData();
        countSumNormTimeByShops();

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

    private void createMenu() {

        menu = new MenuCalculator(this, listViewTechOperations, addedOperations);

        menu.getItems().add(menu.createItemAddDetail());
        menu.getItems().add(menu.createItemAddAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddWeldLongSeam(), menu.createItemAddWeldingDotted());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().addAll(menu.createItemAddAssmNuts(), menu.createItemAddAssmCuttings(), menu.createItemAddAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddLevelingSealer());

        tyeMenuToButton();
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    @Override //AbstractFormController
    public void countSumNormTimeByShops(){
        String measure = MIN.getTimeName();

        double mechanicalTime = 0.0;
        double paintingTime = 0.0;
        double assemblingTime = 0.0;
        double packingTime = 0.0;

        for(OpData cn: addedOperations){
            mechanicalTime += cn.getMechTime() * cn.getQuantity();
            paintingTime += cn.getPaintTime() * cn.getQuantity();
            assemblingTime += cn.getAssmTime() * cn.getQuantity();
            packingTime += cn.getPackTime() * cn.getQuantity();
        }

        opData.setMechTime(mechanicalTime);
        opData.setPaintTime(paintingTime);
        opData.setAssmTime(assemblingTime);
        opData.setPackTime(packingTime);

        controller.countSumNormTimeByShops();

        if(AppStatics.MEASURE.getValue().equals(ETimeMeasurement.SEC)){
            mechanicalTime = mechanicalTime * MIN_TO_SEC;
            paintingTime = paintingTime * MIN_TO_SEC;
            assemblingTime = assemblingTime * MIN_TO_SEC;
            packingTime = packingTime * MIN_TO_SEC;

            measure = SEC.getTimeName();
        }

        String format = DOUBLE_FORMAT;
        if(AppStatics.MEASURE.getValue().equals(ETimeMeasurement.SEC)) format = INTEGER_FORMAT;

        tfMechanicalTime.setText(String.format(format, mechanicalTime));
        tfPaintingTime.setText(String.format(format, paintingTime));
        tfAssemblingTime.setText(String.format(format, assemblingTime));
        tfPackingTime.setText(String.format(format, packingTime));

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assemblingTime + packingTime));

        lblTimeMeasure.setText(measure);

    }

    private void deployData(OpData opData) {
        List<OpData> operations = ((IOpWithOperations)opData).getOperations();
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

    @Override //AbstractFormController
    public void fillOpData(){
        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            deployData(opData);
    }

}
