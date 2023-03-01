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
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.opAssm.OpDetail;
import ru.wert.normic.interfaces.IOpPlate;
import ru.wert.normic.utils.IntegerParser;


import java.io.IOException;
import java.util.ArrayList;

/**
 * ДОБАВЛЕНИЕ ДЕТАЛИ
 */
public class PlateDetailController extends AbstractOpPlate implements IOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfName;

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

    //Переменные для ИМЕНИ
    public static int nameIndex = 0;
    private String detailName;

    private FormDetailController formDetailController;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpDetail opData = (OpDetail)data;

        new TFIntegerColored(tfN, null);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");
        lblQuantity.setStyle("-fx-text-fill: #8b4513");

        if(opData.getName() == null &&
                tfName.getText() == null || tfName.getText().equals("")) {
            detailName = String.format("Деталь #%s", ++nameIndex);
            tfName.setText(detailName);
        }

        ivEdit.setOnMouseClicked(e->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formDetail.fxml"));
                Parent parent = loader.load();
                formDetailController = loader.getController();
                formDetailController.init(formController, tfName, this.opData);
                Decoration windowDecoration = new Decoration(
                        "ДЕТАЛЬ",
                        parent,
                        false,
                        (Stage) lblOperationName.getScene().getWindow(),
                        "decoration-detail",
                        true,
                        false);
                ImageView closer = windowDecoration.getImgCloseWindow();
                closer.setOnMousePressed(ev -> collectOpData(opData));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //Сохраняем имя при изменении
        tfName.textProperty().addListener((observable, oldValue, newValue) -> {
            ((OpDetail)this.opData).setName(tfName.getText());
        });

        //Сохраняем количество и пересчитываем при изменении
        tfN.textProperty().addListener((observable, oldValue, newValue) -> {
            this.opData.setQuantity(IntegerParser.getValue(tfN));
            formController.countSumNormTimeByShops();
            formController.calculateAreaByDetails();
        });

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpDetail opData = (OpDetail)data;

        countInitialValues();

        double mechanicalTime = 0;
        double paintTime = 0;

        for(OpData op : opData.getOperations()){
            mechanicalTime += op.getMechTime();
            paintTime += op.getPaintTime();
        }

        currentMechanicalNormTime = mechanicalTime * quantity;
        currentPaintNormTime = paintTime * quantity;

        collectOpData(opData);
        if (formDetailController != null)
            setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        quantity = IntegerParser.getValue(tfN);
    }

    private void collectOpData(OpDetail opData) {
        opData.setName(tfName.getText());
        opData.setQuantity(IntegerParser.getValue(tfN));
        if(formDetailController != null){
            opData.setMaterial(formDetailController.getCmbxMaterial().getValue());
            opData.setParamA(IntegerParser.getValue(formDetailController.getMatPatchController().getTfA()));
            opData.setParamB(IntegerParser.getValue(formDetailController.getMatPatchController().getTfB()));
            //Сохраняем операции
            opData.setOperations(new ArrayList<>(formDetailController.getAddedOperations()));
        }
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpDetail opData = (OpDetail)data;

        tfName.setText(opData.getName());
        tfN.setText(String.valueOf(opData.getQuantity()));
    }


}
