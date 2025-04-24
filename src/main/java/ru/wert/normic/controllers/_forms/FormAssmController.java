package ru.wert.normic.controllers._forms;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.BtnDone;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.components.TFInteger;
import ru.wert.normic.controllers._forms.main.FormMenus;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.controllers._forms.main.FormMenuManager;

import static ru.wert.normic.AppStatics.*;

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

    public FormAssmController() {
    }

    @Override
    public void init(TextField tfName, TextField tfQuantity, OpData opData, ImgDouble imgDone) {
        this.opData = (OpAssm) opData;

        ((IOpWithOperations)opData).setFormController(this);

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
                new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
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

        menu.addEmptyPlate();
        MAIN_CONTROLLER.recountMainOpData();

    }

    private void initViews() {

    }

    @Override
    public FormMenuManager createMenu() {
        FormMenus formMenus = new FormMenus(this);
        menu = formMenus.create(USE_ELECTRICAL_MENUS ?
                FormMenus.EMenuType.ELECTRICAL_TYPE :
                FormMenus.EMenuType.ASSM_TYPE);

        linkMenuToButton();

        return menu;

    }


    @Override //AbstractFormController
    public void fillOpData(){
        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.addListOfOperations();
    }

}
