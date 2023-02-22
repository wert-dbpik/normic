package ru.wert.normic.controllers._forms;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.AppStatics;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuOps;
import ru.wert.normic.entities.*;
import ru.wert.normic.enums.ETimeMeasurement;


import static ru.wert.normic.controllers.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;

/**
 * СБОРКА - ФОРМА ДОБАВЛЕНИЯ ОПЕРАЦИЙ ДЛЯ СБОРКИ
 */
public class FormAssmController extends AbstractFormController {

    @FXML @Getter
    private TextField tfAssmName;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML @Getter
    private Button btnAddOperation;

    @FXML
    private ImageView ivErase;

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
    public void init(AbstractFormController controller, TextField tfName, OpData opData) {
        this.opData = (OpAssm) opData;
        this.controller = controller;

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
    public  void createMenu() {

        menu = new MenuOps(this, listViewTechOperations, (IOpWithOperations) opData);

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
        menu.getItems().add(menu.createItemAssmCuttings());
        menu.getItems().add(menu.createItemAssmNodes());
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(menu.createItemLevelingSealer());

        linkMenuToButton();
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    @Override //AbstractFormController
    public void countSumNormTimeByShops(){
        String measure = MIN.getName();

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

            measure = SEC.getName();
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


    @Override //AbstractFormController
    public void fillOpData(){
        if(!((IOpWithOperations)opData).getOperations().isEmpty())
            menu.deployData();
    }

}
