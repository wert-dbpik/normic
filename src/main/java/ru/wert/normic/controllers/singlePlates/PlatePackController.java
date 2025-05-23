package ru.wert.normic.controllers.singlePlates;


import javafx.beans.property.SimpleBooleanProperty;
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
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.utils.IntegerParser;

import java.io.IOException;

import static ru.wert.normic.AppStatics.MAIN_CONTROLLER;
import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

/**
 * ДОБАВЛЕНИЕ ДЕТАЛИ
 */
public class PlatePackController extends AbstractOpPlate{

    @FXML
    private TextField tfName;

    @FXML
    private ImageView ivDone;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblQuantity;

    @FXML
    private TextField tfN;

    private OpPack opData;

    //Переменные
    private int quantity;
    @Setter@Getter
    private double currentPackNormTime;

    //Переменные для ИМЕНИ
    public static int nameIndex = 0;
    private String detailName;

    private FormPackController formPackController;
    private ImgDone imgDone;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpPack opData = (OpPack)data;

        imgDone = new ImgDone(ivDone, 24);

        //исправляет nullpointer exception при копипасте операции снизу вверх
        if(opData.getDoneProperty() == null) opData.setDoneProperty(new SimpleBooleanProperty(false));

        imgDone.getStateProperty().bindBidirectional(opData.getDoneProperty());
        imgDone.getStateProperty().setValue(opData.isDone());

        ivDone.setOnMouseClicked(e->{
            openFormEditor(opData);
        });

        lblOperationName.setStyle("-fx-text-fill: darkblue");
        lblQuantity.setStyle("-fx-text-fill: darkblue");

        if(opData.getName() == null &&
                tfName.getText() == null || tfName.getText().equals("")) {
            detailName = String.format("Упаковка #%s", ++nameIndex);
            tfName.setText(detailName);
        }

        vbOperation.setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2)
                openFormEditor(opData);
        });

        //Сохраняем имя при изменении
        tfName.textProperty().addListener((observable, oldValue, newValue) -> {
            ((OpPack)this.opData).setName(tfName.getText());
        });

        //Сохраняем количество и пересчитываем при изменении
        tfN.textProperty().addListener((observable, oldValue, newValue) -> {
            this.opData.setQuantity(IntegerParser.getValue(tfN));
            MAIN_CONTROLLER.recountMainOpData();
        });

    }

    /**
     * Открыть форму редактирования упаковки
     */
    private void openFormEditor(OpPack opData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formPack.fxml"));
            Parent parent = loader.load();
            formPackController = loader.getController();
            formPackController.init(tfName, tfN, this.opData, imgDone);
            Decoration windowDecoration = new Decoration(
                    "УПАКОВКА",
                    parent,
                    true,
                    (Stage) lblOperationName.getScene().getWindow(),
                    "decoration-detail",
                    true,
                    false);
            ImageView closer = windowDecoration.getImgCloseWindow();
            closer.setOnMousePressed(ev -> collectOpData(tfName, tfN, imgDone));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpPack)data;
        opData.setOpPlate(this);

        countInitialValues();

        double packingTime = 0;

        for(OpData op : opData.getOperations()){
            packingTime += op.getPackTime();
        }

        currentPackNormTime = packingTime * quantity;

        collectOpData(tfName, tfN, imgDone);
        if (formPackController != null)
            new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        quantity = IntegerParser.getValue(tfN);
    }


    public void collectOpData(TextField tfName, TextField tfN, ImgDouble imgDone) {
        opData.setDone(imgDone.getStateProperty().getValue());
        opData.setName(tfName.getText());
        opData.setQuantity(IntegerParser.getValue(tfN));
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPack opData = (OpPack)data;

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
