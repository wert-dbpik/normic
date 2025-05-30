package ru.wert.normic.controllers._forms;


import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.AppStatics;
import ru.wert.normic.components.BtnDone;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.components.TFInteger;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.controllers._forms.main.FormMenuManager;
import ru.wert.normic.utils.IntegerParser;

import java.util.Collections;

import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.*;

/**
 * ДЕТАЛЬ - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ ДЕТАЛИ
 */
public class FormPackController extends AbstractFormController {

    @FXML @Getter
    private TextField tfDetailName;

    @FXML @Getter
    private TextField tfPackQuantity;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private Button btnDone;

    @FXML @Getter
    private TextField tfTotalTime;

    @FXML @Getter
    private TextField tfWidth, tfDepth, tfHeight;

    @FXML
    private Label lblTimeMeasure;

    @Getter private int width, depth, height;

    @Override //AbstractFormController
    public void init(TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone) {
        this.opData = (OpPack) opData;

        ((IOpWithOperations)opData).setFormController(this);

        BtnDone done = new BtnDone(btnDone);
        done.getStateProperty().bindBidirectional(imgDone.getStateProperty());

        //Создаем меню
        createMenu();

        initViews();

        setDragAndDropCellFactory();

        //Инициализируем наименование
        if(tfName != null) {
            ((OpPack)this.opData).setName(tfName.getText());
            tfDetailName.setText(tfName.getText());
            tfName.textProperty().bindBidirectional(tfDetailName.textProperty());
        }

        new TFInteger(tfPackQuantity);

        //Инициализируем количество
        if(tfQuantity != null) {
            ((OpPack)this.opData).setQuantity(Integer.parseInt(tfQuantity.getText()));
            tfPackQuantity.setText(tfQuantity.getText());
            tfQuantity.textProperty().bindBidirectional(tfPackQuantity.textProperty());
        }

        //Заполняем поля формы
        fillOpData();
        countSumNormTimeByShops();

        menu.addEmptyPlate();

    }


    private void initViews() {

        addedOperations.addListener((ListChangeListener<OpData>) c -> {
            ((OpPack)opData).setOperations(addedOperations);
        });

        tfWidth.textProperty().addListener((observable, oldValue, newValue) -> {
            width = IntegerParser.getValue(tfWidth);
            ((OpPack)opData).setWidth(width);
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

        tfDepth.textProperty().addListener((observable, oldValue, newValue) -> {
            depth = IntegerParser.getValue(tfDepth);
            ((OpPack)opData).setDepth(depth);
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

        tfHeight.textProperty().addListener((observable, oldValue, newValue) -> {
            height = IntegerParser.getValue(tfHeight);
            ((OpPack)opData).setHeight(height);
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

        tfTotalTime.textProperty().addListener((observable, oldValue, newValue) -> {
            countSumNormTimeByShops();
        });

//        ivErase.setOnMouseClicked(e->{
//            ((IOpWithOperations)opData).getOperations().clear();
//            addedPlates.clear();
//            addedOperations.clear();
//            listViewTechOperations.getItems().clear();
//            countSumNormTimeByShops();
//        });

    }

    @Override
    public FormMenuManager createMenu(){
        menu = new FormMenuManager(this, listViewTechOperations, (IOpWithOperations) opData);

        menu.getItems().add(menu.createItemPackInCartoonBox());
        menu.getItems().add(menu.createItemPackOnPalletizer());
        menu.getItems().add(menu.createItemPackInHandStretchWrap());
        menu.getItems().add(menu.createItemPackInBubbleWrap());
        menu.getItems().add(menu.createItemPackOnPallet());

        Menu simpleOperationsMenu = menu.createAllSimpleOperations(Collections.singletonList(ENormType.NORM_PACKING));
        if(simpleOperationsMenu != null) {
            menu.getItems().add(new SeparatorMenuItem());
            menu.getItems().add(simpleOperationsMenu);
        }


        linkMenuToButton();

        return menu;
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    public void countSumNormTimeByShops(){
        String measure = MIN.getMeasure();

        double packTime = 0.0;

        for(OpData cn: addedOperations){
            packTime += cn.getPackTime();
        }

        opData.setPackTime(roundTo001(packTime));

//        controller.countSumNormTimeByShops();
        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);

        packTime = packTime * CURRENT_MEASURE.getRate();
        measure = CURRENT_MEASURE.getMeasure();

        String format = DOUBLE_FORMAT;
        if(AppStatics.MEASURE.getSelectedToggle().getUserData().equals(SEC.name())) format = INTEGER_FORMAT;

        tfTotalTime.setText(String.format(format, packTime ).trim());

        lblTimeMeasure.setText(measure);

    }


    @Override //AbstractFormController
    public void fillOpData(){

        width = ((OpPack)opData).getWidth();
        tfWidth.setText(String.valueOf(width));

        depth = ((OpPack)opData).getDepth();
        tfDepth.setText(String.valueOf(depth));

        height = ((OpPack)opData).getHeight();
        tfHeight.setText(String.valueOf(height));

        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.addListOfOperations();
    }



}
