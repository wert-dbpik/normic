package ru.wert.normic.controllers._forms;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.AppStatics;
import ru.wert.normic.components.BtnDone;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.components.TFInteger;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuForm;

import static ru.wert.normic.AppStatics.CURRENT_MEASURE;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.*;

/**
 * СБОРКА - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ СБОРКИ
 */
public class FormAssmController extends AbstractFormController {

    @FXML @Getter
    private TextField tfAssmName;

    @FXML @Getter
    private TextField tfAssmQuantity;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private Button btnDone;

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

    public FormAssmController() {
    }

    @Override
    public void init(AbstractFormController controller, TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone) {
        this.opData = (OpAssm) opData;
        this.controller = controller;

        BtnDone done = new BtnDone(btnDone);
        done.getStateProperty().bindBidirectional(imgDone.getStateProperty());

        //Создаем меню
        createMenu();

        initViews();

        setDragAndDropCellFactory();

        //Инициализируем наименование
        if(tfName != null) {
            ((OpAssm)this.opData).setName(tfName.getText());
            tfAssmName.setText(tfName.getText());
            tfAssmName.textProperty().bindBidirectional(tfName.textProperty());
        }


        new TFInteger(tfAssmQuantity);

        //Инициализируем количество
        if(tfQuantity != null) {
            ((OpAssm)this.opData).setOpQuantity(Integer.parseInt(tfQuantity.getText()));
            this.opData.setQuantity(opData.getOpQuantity() * controller.getOpData().getQuantity());
            tfAssmQuantity.setText(tfQuantity.getText());
            tfQuantity.textProperty().bindBidirectional(tfAssmQuantity.textProperty());
        }

        //Заполняем поля формы
        fillOpData();
        countSumNormTimeByShops();

        menu.addEmptyPlate();

    }

    private void initViews() {

        tfTotalTime.textProperty().addListener((observable, oldValue, newValue) -> {
            countSumNormTimeByShops();
        });

    }

    @Override
    public  MenuForm createMenu() {

        menu = new MenuForm(this, listViewTechOperations, (IOpWithOperations) opData);

        menu.getItems().add(menu.createItemDetail());
        menu.getItems().add(menu.createItemAssm());
        menu.getItems().add(menu.createItemPack());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemPaintAssm());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemWeldLongSeam());
        menu.getItems().add(menu.createItemWeldingDotted());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAssmNuts());
        menu.getItems().add(menu.createItemAssmNutsMK());
        menu.getItems().add(menu.createItemAssmCuttings());
        menu.getItems().add(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemLevelingSealer());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddFilePallet());


        linkMenuToButton();

        return menu;
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    @Override //AbstractFormController
    public void countSumNormTimeByShops(){
        String measure = MIN.getMeasure();

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

        if(CURRENT_MEASURE.equals(SEC)){
            mechanicalTime = mechanicalTime * MIN_TO_SEC;
            paintingTime = paintingTime * MIN_TO_SEC;
            assemblingTime = assemblingTime * MIN_TO_SEC;
            packingTime = packingTime * MIN_TO_SEC;

            measure = SEC.getMeasure();
        }
        if(CURRENT_MEASURE.equals(HOUR)){
            mechanicalTime = mechanicalTime * MIN_TO_HOUR;
            paintingTime = paintingTime * MIN_TO_HOUR;
            assemblingTime = assemblingTime * MIN_TO_HOUR;
            packingTime = packingTime * MIN_TO_HOUR;

            measure = HOUR.getMeasure();
        }


        String format = DOUBLE_FORMAT;
        if(AppStatics.MEASURE.getSelectedToggle().getUserData().equals(SEC.name())) format = INTEGER_FORMAT;

        tfMechanicalTime.setText(String.format(format, mechanicalTime).trim());
        tfPaintingTime.setText(String.format(format, paintingTime).trim());
        tfAssemblingTime.setText(String.format(format, assemblingTime).trim());
        tfPackingTime.setText(String.format(format, packingTime).trim());

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assemblingTime + packingTime).trim());

        lblTimeMeasure.setText(measure);

    }


    @Override //AbstractFormController
    public void fillOpData(){
        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.deployData();
    }

}
