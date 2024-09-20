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
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuForm;

import java.util.Collections;

import static ru.wert.normic.AppStatics.*;
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

    private AbstractFormController prevAssmController;

    public FormAssmController() {
    }

    @Override
    public void init(AbstractFormController prevAssnController, TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone) {
        this.opData = (OpAssm) opData;
        this.prevAssmController = prevAssnController;

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
        tfAssmQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                int total = Integer.parseInt(newValue) * prevAssmController.getOpData().getTotal();
                TotalCounter.recountNormsWithNewTotal(total, opData,prevAssmController);
            }
        });


        //Инициализируем количество
        if(tfQuantity != null) {
            ((OpAssm)this.opData).setQuantity(Integer.parseInt(tfQuantity.getText()));
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
        menu.getItems().add(menu.createItemWeldDifficulty());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAssmNuts());
        menu.getItems().add(menu.createItemAssmNutsMK());
        menu.getItems().add(menu.createItemAssmCuttings());
        menu.getItems().add(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemLevelingSealer());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemAddFilePallet());
        menu.getItems().add(menu.createItemSearchFilePallet());
        menu.getItems().add(new SeparatorMenuItem());

        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_ASSEMBLING));
        if(simpleOperationsMenu != null)
            menu.getItems().add(simpleOperationsMenu);


        linkMenuToButton();

        return menu;
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    @Override //AbstractFormController
    public void countSumNormTimeByShops(){

        OpData opData = TotalCounter.countSumNormTimeByShops((IOpWithOperations) getOpData(), prevAssmController);

        fillNormsAndMeasurment( opData.getMechTime(), opData.getPaintTime(), opData.getAssmTime(), opData.getPackTime());

    }

    private void fillNormsAndMeasurment(double mechanicalTime, double paintingTime, double assemblingTime, double packingTime) {

        //Пересчитываем нормы согласно единице измерения
        mechanicalTime = mechanicalTime * CURRENT_MEASURE.getRate();
        paintingTime = paintingTime * CURRENT_MEASURE.getRate();
        assemblingTime = assemblingTime * CURRENT_MEASURE.getRate();
        packingTime = packingTime * CURRENT_MEASURE.getRate();

        String format = DOUBLE_FORMAT;
        if(CURRENT_MEASURE.equals(ETimeMeasurement.SEC)) format = INTEGER_FORMAT;

        tfMechanicalTime.setText(String.format(format, mechanicalTime).trim());
        tfPaintingTime.setText(String.format(format, paintingTime).trim());
        tfAssemblingTime.setText(String.format(format, assemblingTime).trim());
        tfPackingTime.setText(String.format(format, packingTime).trim());

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assemblingTime + packingTime).trim());

        lblTimeMeasure.setText(CURRENT_MEASURE.getMeasure());
    }


    @Override //AbstractFormController
    public void fillOpData(){
        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.addListOfOperations();
    }

}
