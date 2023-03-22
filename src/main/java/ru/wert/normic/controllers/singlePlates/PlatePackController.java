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
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.decoration.Decoration;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpPlate;

import java.io.IOException;

/**
 * ДОБАВЛЕНИЕ ДЕТАЛИ
 */
public class PlatePackController extends AbstractOpPlate{

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfName;

    @FXML
    private ImageView ivEdit;

    @FXML
    private Label lblOperationName;

    //Переменные
    @Setter@Getter
    private double currentPackNormTime;

    //Переменные для ИМЕНИ
    public static int nameIndex = 0;
    private String detailName;

    private FormPackController formPackController;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpPack opData = (OpPack)data;
        ivOperation.setImage(EOpType.PACK.getLogo());

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        if(opData.getName() == null &&
                tfName.getText() == null || tfName.getText().equals("")) {
            detailName = String.format("Упаковка #%s", ++nameIndex);
            tfName.setText(detailName);
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
            ((OpPack)this.opData).setName(tfName.getText());
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
            formPackController.init(formController, tfName, this.opData);
            Decoration windowDecoration = new Decoration(
                    "УПАКОВКА",
                    parent,
                    false,
                    (Stage) lblOperationName.getScene().getWindow(),
                    "decoration-detail",
                    true,
                    false);
            ImageView closer = windowDecoration.getImgCloseWindow();
            closer.setOnMousePressed(ev -> collectOpData(opData, tfName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpPack opData = (OpPack)data;

        countInitialValues();

        for(OpData op : opData.getOperations()){
            currentPackNormTime += op.getPackTime();
        }

        collectOpData(opData, tfName);
        if (formPackController != null)
            setTimeMeasurement();
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

    }


    public static void collectOpData(OpPack opData, TextField tfName) {
        opData.setName(tfName.getText());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPack opData = (OpPack)data;

        tfName.setText(opData.getName());
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
