package ru.wert.normic;


import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.BXTimeMeasurement;
import ru.wert.normic.entities.*;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IFormController;


import java.util.ArrayList;
import java.util.List;

import static ru.wert.normic.AbstractOpPlate.*;
import static ru.wert.normic.enums.ETimeMeasurement.MIN;
import static ru.wert.normic.enums.ETimeMeasurement.SEC;


public class MainController implements IFormController {

    @FXML
    private ImageView ivSave;


    @FXML @Getter
    private ComboBox<ETimeMeasurement> cmbxTimeMeasurement;

    @FXML @Getter
    private ListView<VBox> listViewTechOperations;

    @FXML
    private ImageView ivAddOperation;

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

    private MenuCalculator menu;

    @FXML @Getter
    private TextField tfTotalTime;

    @Getter private ObservableList<AbstractOpPlate> addedPlates;
    @Getter private List<OpData> addedOperations;

    @Getter //IFormController
    private OpAssm opData;

    @FXML
    void initialize(){
        AppStatics.MEASURE = cmbxTimeMeasurement;
        opData = new OpAssm();
        //Инициализируем список операционных плашек
        addedPlates = FXCollections.observableArrayList();
        addedOperations = new ArrayList<>();

        //Создаем меню
        createMenu();

        initViews();

        //Инициализируем комбобоксы
        new BXTimeMeasurement().create(cmbxTimeMeasurement);

        //Заполняем поля формы
        fillOpData();

    }


    private void fillOpData(){
        if(!opData.getOperations().isEmpty())
            deployData(opData);
    }

    private void initViews() {

        ivSave.setOnMouseClicked(this::save);

        cmbxTimeMeasurement.valueProperty().addListener((observable, oldValue, newValue) -> {
            lblTimeMeasure.setText(newValue.getTimeName());
            countSumNormTimeByShops();
        });

    }

    private void save(MouseEvent e){
        opData.setOperations(new ArrayList<>(addedOperations));

        Gson gson = new Gson();
        String json = gson.toJson(opData);
        System.out.println("saved :" + json);
    }

    private void createMenu() {

        MenuCalculator menu = new MenuCalculator(this, listViewTechOperations, addedOperations);

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

        ivAddOperation.setOnMouseClicked(e->{
            menu.show(ivAddOperation, Side.LEFT, -15.0, 30.0);
        });
    }


    /**
     * Метод расчитывает суммарное время по участкам
     */
    @Override //IFormController
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

        if(cmbxTimeMeasurement.getValue().equals(ETimeMeasurement.SEC)){
            mechanicalTime = mechanicalTime * MIN_TO_SEC;
            paintingTime = paintingTime * MIN_TO_SEC;
            assemblingTime = assemblingTime * MIN_TO_SEC;
            packingTime = packingTime * MIN_TO_SEC;

            measure = SEC.getTimeName();
        }

        String format = doubleFormat;
        if(cmbxTimeMeasurement.getValue().equals(ETimeMeasurement.SEC)) format = integerFormat;

        tfMechanicalTime.setText(String.format(format, mechanicalTime));
        tfPaintingTime.setText(String.format(format, paintingTime));
        tfAssemblingTime.setText(String.format(format, assemblingTime));
        tfPackingTime.setText(String.format(format, packingTime));

        tfTotalTime.setText(String.format(format, mechanicalTime + paintingTime + assemblingTime + packingTime));

        lblTimeMeasure.setText(measure);

    }

    private void deployData(OpAssm opData) {
        List<OpData> operations = opData.getOperations();
        for (OpData op : operations) {
            switch (op.getOpType()) {
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

    @Override
    public void init(IFormController controller, TextField tfName, OpData opData) {
        //НЕ ИСПОЛЬЗУЕТСЯ
    }

}