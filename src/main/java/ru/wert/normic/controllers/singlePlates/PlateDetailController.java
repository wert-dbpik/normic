package ru.wert.normic.controllers.singlePlates;


import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.wert.normic.components.ImgDone;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.utils.IntegerParser;


import java.io.IOException;
import java.util.ArrayList;

/**
 * ДОБАВЛЕНИЕ ДЕТАЛИ
 */
public class PlateDetailController extends AbstractOpPlate {

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfN;

    @FXML
    private ImageView ivDone;

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
    private OpDetail opData;
    private ImgDouble imgDone;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        opData = (OpDetail)data;

        imgDone = new ImgDone(ivDone, 24);
        imgDone.getStateProperty().bindBidirectional(opData.getDoneProperty());
        imgDone.getStateProperty().setValue(opData.isDone());

        new TFIntegerColored(tfN, null);

        lblOperationName.setStyle("-fx-text-fill: darkblue");
        lblQuantity.setStyle("-fx-text-fill: darkblue");

        if(opData.getName() == null &&
                tfName.getText() == null || tfName.getText().equals("")) {
            detailName = String.format("Деталь #%s", ++nameIndex);
            tfName.setText(detailName);
        }

        ivDone.setOnMouseClicked(e->{
            openFormEditor(opData);
        });

        vbOperation.setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2)
                openFormEditor(opData);
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

    /**
     * Открыть форму редактирования детали
     */
    private void openFormEditor(OpDetail opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formDetail.fxml"));
            Parent parent = loader.load();
            formDetailController = loader.getController();
            formDetailController.init(formController, tfName, tfN, this.opData, imgDone);
            Decoration windowDecoration = new Decoration(
                    "ДЕТАЛЬ",
                    parent,
                    true,
                    (Stage) vbOperation.getScene().getWindow(),
                    "decoration-detail",
                    true,
                    false);
            ImageView closer = windowDecoration.getImgCloseWindow();
            closer.setOnMousePressed(ev -> collectOpData(opData, formDetailController, tfName, tfN, imgDone));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

        collectOpData(opData, formDetailController, tfName, tfN, imgDone);
        if (formDetailController != null)
            setTimeMeasurement();
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        quantity = IntegerParser.getValue(tfN);
    }


    public static void collectOpData(OpDetail opData, AbstractFormController formDetailController, TextField tfName, TextField tfN, ImgDouble imgDone) {
        opData.setDone(imgDone.getStateProperty().getValue());
        opData.setName(tfName.getText());
        opData.setQuantity(IntegerParser.getValue(tfN));
        if(formDetailController != null){
            opData.setMaterial(((FormDetailController)formDetailController).getCmbxMaterial().getValue());
            opData.setParamA(IntegerParser.getValue(((FormDetailController)formDetailController).getMatPatchController().getTfA()));
            opData.setParamB(IntegerParser.getValue(((FormDetailController)formDetailController).getMatPatchController().getTfB()));
            //Сохраняем операции
            opData.setOperations(new ArrayList<>(formDetailController.getAddedOperations()));
        }
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpDetail opData = (OpDetail)data;

        if (imgDone != null)imgDone.getStateProperty().setValue(opData.isDone());
        tfName.setText(opData.getName());
        tfN.setText(String.valueOf(opData.getQuantity()));
    }

    @Override
    public String helpText() {
        return null;
    }

    @Override
    public Image helpImage() {
        return null;
    }

}
