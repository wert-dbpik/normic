package ru.wert.normic.controllers.assembling;


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
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormAssmController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.opAssembling.OpAssm;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opAssembling.OpDetail;
import ru.wert.normic.interfaces.IOpPlate;
import ru.wert.normic.utils.IntegerParser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * ДОБАВЛЕНИЕ СБОРКИ
 */
public class PlateAssmController extends AbstractOpPlate implements IOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfAssmName;

    @FXML
    private TextField tfN;

    @FXML
    private ImageView ivEdit;

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
    
    private FormAssmController formAssmController;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpAssm opData = (OpAssm)data;

        new TFIntegerColored(tfN, null);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");
        lblQuantity.setStyle("-fx-text-fill: #8b4513");

        if(opData.getName() == null &&
                tfAssmName.getText() == null || tfAssmName.getText().equals("")) {
            assmName = String.format("Сборка #%s", ++nameIndex);
            tfAssmName.setText(assmName);
        }

        ivEdit.setOnMouseClicked(e->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formAssm.fxml"));
                Parent parent = loader.load();
                formAssmController = loader.getController();
                formAssmController.init(formController, tfAssmName, this.opData);
                Decoration windowDecoration = new Decoration(
                        "СБОРКА",
                        parent,
                        false,
                        (Stage) lblOperationName.getScene().getWindow(),
                        "decoration-assm",
                        true,
                        false);
                ImageView closer = windowDecoration.getImgCloseWindow();
                closer.setOnMousePressed(ev -> collectOpData(opData));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //Сохраняем имя при изменении
        tfAssmName.textProperty().addListener((observable, oldValue, newValue) -> {
            ((OpDetail)this.opData).setName(tfAssmName.getText());
        });

        //Сохраняем количество и пересчитываем при изменении
        tfN.textProperty().addListener((observable, oldValue, newValue) -> {
            this.opData.setQuantity(IntegerParser.getValue(tfN));
            formController.countSumNormTimeByShops();
        });
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpAssm opData = (OpAssm)data;

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

        collectOpData(opData);
        if (formAssmController != null)
            setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        quantity = IntegerParser.getValue(tfN);
    }

    private void collectOpData(OpAssm opData) {
        if(formAssmController != null){
            opData.setName(formAssmController.getTfAssmName().getText());
            //Сохраняем операции
            opData.setOperations(new ArrayList<>(formAssmController.getAddedOperations()));
        }
        opData.setQuantity(IntegerParser.getValue(tfN));
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpAssm opData = (OpAssm)data;

        tfAssmName.setText(opData.getName());
        tfN.setText(String.valueOf(opData.getQuantity()));
    }


}
