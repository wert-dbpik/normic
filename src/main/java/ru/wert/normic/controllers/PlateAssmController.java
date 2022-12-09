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
import ru.wert.normic.AbstractOpPlate;
import ru.wert.normic.components.TFColoredInteger;
import ru.wert.normic.controllers.forms.FormAssmController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.OpAssm;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.interfaces.IFormController;
import ru.wert.normic.interfaces.IOpPlate;
import ru.wert.normic.utils.IntegerParser;

import java.io.IOException;
import java.util.ArrayList;

public class PlateAssmController extends AbstractOpPlate implements IOpPlate {

    @FXML
    private TextField tfAssmName;

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
    @Setter@Getter
    private double currentAssmNormTime;
    @Setter@Getter
    private double currentPackNormTime;

    //Переменные для ИМЕНИ
    public static int nameIndex = 0;
    private String assmName;

    private IFormController prevController;
    private FormAssmController assmController;

    private OpAssm opData;
    public void setOpData(OpAssm opData){
        this.opData = opData;
    }

    @Override //IOpData
    public OpData getOpData(){
        return opData;
    }

    public void init(IFormController prevController, OpAssm opData){
        this.prevController = prevController;
        this.opData = opData;

        prevController.getAddedPlates().add(this);
        prevController.getAddedOperations().add(opData);

        fillOpData();

        new TFColoredInteger(tfN, null);

        ivDeleteOperation.setOnMouseClicked(e->{
            prevController.getAddedPlates().remove(this);

            VBox box = prevController.getListViewTechOperations().getSelectionModel().getSelectedItem();
            prevController.getListViewTechOperations().getItems().remove(box);

            prevController.getAddedOperations().remove(this.opData);
            prevController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");
        lblQuantity.setStyle("-fx-text-fill: #8b4513");

        if(this.opData.getName() == null &&
                tfAssmName.getText() == null || tfAssmName.getText().equals("")) {
            assmName = String.format("Сборка #%s", ++nameIndex);
            tfAssmName.setText(assmName);
        }

        ivEdit.setOnMouseClicked(e->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calculatorAssm.fxml"));
                Parent parent = loader.load();
                assmController = loader.getController();
                assmController.init(prevController, tfAssmName, this.opData);
                Decoration windowDecoration = new Decoration(
                        "СБОРКА",
                        parent,
                        false,
                        (Stage) lblOperationName.getScene().getWindow(),
                        "decoration-assm",
                        true);
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

        countNorm();
    }

    @Override//AbstractOpPlate
    public void countNorm(){

        countInitialValues();

        double mechanicalTime = 0;
        double paintTime = 0;
        double assmTime = 0;
        double packTime = 0;

        for(OpData op : opData.getOperations()){
            mechanicalTime += op.getMechTime();
            paintTime += op.getPaintTime();
            assmTime += op.getAssmTime();
            packTime += op.getPackTime();
        }

        currentMechanicalNormTime = mechanicalTime * quantity;
        currentPaintNormTime = paintTime * quantity;
        currentAssmNormTime = assmTime * quantity;
        currentPackNormTime = packTime * quantity;

        collectOpData();
        if (assmController != null)
            setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    private void countInitialValues() {
        quantity = IntegerParser.getValue(tfN);
    }

    private void collectOpData() {
        if(assmController != null){
            opData.setName(assmController.getTfAssmName().getText());
            //Сохраняем операции
            opData.setOperations(new ArrayList<>(assmController.getAddedOperations()));
        }
        opData.setQuantity(IntegerParser.getValue(tfN));
    }

    private void fillOpData(){
        tfAssmName.setText(opData.getName());
        tfN.setText(String.valueOf(opData.getQuantity()));
    }


}
