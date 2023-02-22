package ru.wert.normic.controllers._forms;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.AppStatics;
import ru.wert.normic.components.BXMaterial;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpPack;
import ru.wert.normic.entities.OpPack;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.enums.EMatType;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.materials.matlPatches.AbstractMatPatchController;
import ru.wert.normic.menus.MenuOps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

/**
 * ДЕТАЛЬ - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ ДЕТАЛИ
 */
public class FormPackController extends AbstractFormController {

    @FXML @Getter
    private TextField tfDetailName;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private ImageView ivErase;

    @FXML @Getter
    private TextField tfTotalTime;

    @FXML @Getter
    private TextField tfWidth, tfDepth, tfHeight;

    @FXML
    private Label lblTimeMeasure;

    @Getter private int width, depth, height;

    private AbstractFormController controller;

    @Override //AbstractFormController
    public void init(AbstractFormController controller, TextField tfName, OpData opData) {
        this.opData = (OpPack) opData;
        this.controller = controller;

        new TFIntegerColored(tfWidth, null);
        new TFIntegerColored(tfDepth, null);
        new TFIntegerColored(tfHeight, null);

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

    @Override
    public void createMenu(){
        menu = new MenuOps(this, listViewTechOperations, (IOpWithOperations) opData);

            menu.getItems().add(menu.createItemPackTallCabinet());
            menu.getItems().add(menu.createItemMountOnPallet());


        linkMenuToButton();
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    public void countSumNormTimeByShops(){
        String measure = MIN.getName();

        double packTime = 0.0;

        for(OpData cn: addedOperations){
            packTime += cn.getPackTime();
        }

        opData.setPackTime(packTime);

        controller.countSumNormTimeByShops();

        if(AppStatics.MEASURE.getValue().equals(SEC)){
            packTime = packTime * MIN_TO_SEC;

            measure = SEC.getName();
        }

        String format = DOUBLE_FORMAT;
        if(AppStatics.MEASURE.getValue().equals(SEC)) format = INTEGER_FORMAT;

        tfTotalTime.setText(String.format(format,packTime ));

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
            menu.deployData();
    }



}
