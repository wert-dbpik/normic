package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.*;
import ru.wert.normic.components.TFColoredInteger;
import ru.wert.normic.controllers.forms.FormDetailController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IFormController;
import ru.wert.normic.interfaces.IOpPlate;
import ru.wert.normic.utils.IntegerParser;


import java.io.IOException;
import java.util.ArrayList;

public class PlateDetailController extends AbstractOpPlate implements IOpPlate {

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfN;

    @FXML
    private ImageView ivEdit;

    @FXML
    private ImageView ivCopy;

    @FXML
    private ImageView ivDeleteOperation;
    
    @FXML
    private Label lblOperationName;

    @FXML
    private Label lblQuantity;

    //Переменные
    private int quantity;
    @Setter@Getter
    private double currentMechanicalNormTime;
    @Setter@Getter
    private double currentPaintNormTime;

    //Переменные для ИМЕНИ
    private static int nameIndex = 0;
    private String detailName;


    private IFormController prevController;
    private FormDetailController detailController;

    private OpDetail opData;
    public void setOpData(OpDetail opData){
        this.opData = opData;
    }

    @Override //IOpData
    public OpData getOpData(){
        return opData;
    }

    public void init(IFormController prevController, OpDetail opData){
        this.prevController = prevController;
        this.opData = opData;

        new TFColoredInteger(tfN, null);

        ivDeleteOperation.setOnMouseClicked(e->{
            prevController.getAddedPlates().remove(this);

            VBox box = prevController.getListViewTechOperations().getSelectionModel().getSelectedItem();
            prevController.getListViewTechOperations().getItems().remove(box);

            prevController.getAddedOperations().remove(this.opData);
            prevController.countSumNormTimeByShops();
        });

        fillOpData();

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");
        lblQuantity.setStyle("-fx-text-fill: #8b4513");

        if(this.opData.getName() == null &&
                tfName.getText() == null || tfName.getText().equals("")) {
            detailName = String.format("Деталь #%s", ++nameIndex);
            tfName.setText(detailName);
        }

        ivEdit.setOnMouseClicked(e->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calculatorDetail.fxml"));
                Parent parent = loader.load();
                detailController = loader.getController();
                detailController.init(prevController, tfName, this.opData);
                Decoration windowDecoration = new Decoration(
                        "ДЕТАЛЬ",
                        parent,
                        false,
                        (Stage) lblOperationName.getScene().getWindow(),
                        "decoration-detail",
                        100);
                ImageView closer = windowDecoration.getImgCloseWindow();
                closer.setOnMousePressed(ev -> collectOpData());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        tfN.textProperty().addListener((observable, oldValue, newValue) -> {
            this.opData.setQuantity(IntegerParser.getValue(tfN));
            prevController.countSumNormTimeByShops();
        });

        prevController.getAddedPlates().add(this);
//        setNormTime();
    }

    @Override//AbstractOpPlate
    public void countNorm(){

        countInitialValues();

        double mechanicalTime = 0;
        double paintTime = 0;

        for(OpData op : opData.getOperations()){
            mechanicalTime += op.getMechTime();
            paintTime += op.getPaintTime();
        }

        currentMechanicalNormTime = mechanicalTime * quantity;
        currentPaintNormTime = paintTime * quantity;
        collectOpData();
        if (detailController != null)
            setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {
        quantity = IntegerParser.getValue(tfN);
    }

    private void collectOpData() {
        if(detailController != null){
            opData.setName(detailController.getTfDetailName().getText());
            opData.setMaterial(detailController.getCmbxMaterial().getValue());
            opData.setParamA(IntegerParser.getValue(detailController.getTfA()));
            opData.setParamB(IntegerParser.getValue(detailController.getTfB()));
            opData.setOperations(new ArrayList<>(detailController.getAddedOperations()));
        }
        opData.setQuantity(IntegerParser.getValue(tfN));
    }

    private void fillOpData(){
        tfName.setText(opData.getName());
        tfN.setText(String.valueOf(opData.getQuantity()));
    }


}
