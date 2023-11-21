package ru.wert.normic.controllers.singlePlates;


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
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers._forms.FormAssmController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.utils.IntegerParser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * ДОБАВЛЕНИЕ СБОРКИ
 */
public class PlateAssmController extends AbstractOpPlate{

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfN;

    @FXML
    private ImageView ivEdit;

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

        lblOperationName.setStyle("-fx-text-fill: darkblue");
        lblQuantity.setStyle("-fx-text-fill: darkblue");

        if(opData.getName() == null &&
                tfName.getText() == null || tfName.getText().equals("")) {
            assmName = String.format("Сборка #%s", ++nameIndex);
            tfName.setText(assmName);
        }

        ivEdit.setOnMouseClicked(e->{
            openFormEditor(opData);
        });

        vbOperation.setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2)
                openFormEditor(opData);
        });

        //Сохраняем имя при изменении
        tfName.textProperty().addListener((observable, oldValue, newValue) -> {
            ((OpAssm)this.opData).setName(tfName.getText());
        });

        //Сохраняем количество и пересчитываем при изменении
        tfN.textProperty().addListener((observable, oldValue, newValue) -> {
            this.opData.setQuantity(IntegerParser.getValue(tfN));
            formController.countSumNormTimeByShops();
        });
    }

    /**
     * Открыть форму редактирования сборки
     */
    private void openFormEditor(OpAssm opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formAssm.fxml"));
            Parent parent = loader.load();
            formAssmController = loader.getController();
            formAssmController.init(formController, tfName, tfN, this.opData);
            Decoration windowDecoration = new Decoration(
                    "СБОРКА",
                    parent,
                    true,
                    (Stage) lblOperationName.getScene().getWindow(),
                    "decoration-assm",
                    true,
                    false);
            ImageView closer = windowDecoration.getImgCloseWindow();
            closer.setOnMousePressed(ev -> collectOpData(opData, formAssmController, tfName, tfN));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

        collectOpData(opData, formAssmController, tfName, tfN);
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


    public static void collectOpData(OpAssm opData, AbstractFormController formAssmController, TextField tfName, TextField tfN) {
        opData.setName(tfName.getText());
        opData.setQuantity(IntegerParser.getValue(tfN));
        if(formAssmController != null){
            //Сохраняем операции
            opData.setOperations(new ArrayList<>(formAssmController.getAddedOperations()));
        }
        opData.setQuantity(IntegerParser.getValue(tfN));
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpAssm opData = (OpAssm)data;

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
