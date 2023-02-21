package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFDouble;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.OpCutOff;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpPackFrame;
import ru.wert.normic.utils.IntegerParser;

import java.util.NoSuchElementException;

/**
 * ОТРЕЗАНИЕ ДЕТАЛИ НА ТОКАРНОМ СТАНКЕ
 */
public class PlatePackFrameController extends AbstractOpPlate {


    @FXML
    private TextField tfWidth, tfDepth, tfHeight;

    @FXML
    private TextField tfRoofWrap, tfSideWrap, tfStretchWrap,  tfPolyWrap, tfBubbleWrap, tfDuctTape;

    @FXML
    private CheckBox chbxRoofWrap, chbxSideWrap, chbxStretchWrap,  chbxPolyWrap, chbxBubbleWrap, chbxDuctTape;

    @FXML
    private ImageView ivHelpOnUseStripping;

    @FXML
    private Label lblOperationName;

    @FXML
    private TextField tfNormTime;

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblNormTimeMeasure;

    @FXML
    private ImageView ivDeleteOperation;

    private String initStyle;
    private int width, depth, height;
    private boolean roofWrap, sideWrap, stretchWrap, polyWrap, bubbleWrap, ductTape;
    private Double roofWrapL, sideWrapL, stretchWrapL, polyWrapL, bubbleWrapL, ductTapeL;
    private Integer totalCartoon;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpPackFrame opData = (OpPackFrame) data;
        initStyle = tfWidth.getStyle(); //Сохраняем исходный стиль

        new TFIntegerColored(tfWidth, this);
        new TFIntegerColored(tfDepth, this);
        new TFIntegerColored(tfHeight, this);

        new ChBox(chbxRoofWrap, this);
        new ChBox(chbxSideWrap, this);
        new ChBox(chbxStretchWrap, this);
        new ChBox(chbxPolyWrap, this);
        new ChBox(chbxBubbleWrap, this);
        new ChBox(chbxDuctTape, this);


        tfRoofWrap.disableProperty().bind(chbxRoofWrap.selectedProperty().not());
        tfSideWrap.disableProperty().bind(chbxSideWrap.selectedProperty().not());
        tfStretchWrap.disableProperty().bind(chbxStretchWrap.selectedProperty().not());
        tfPolyWrap.disableProperty().bind(chbxPolyWrap.selectedProperty().not());
        tfBubbleWrap.disableProperty().bind(chbxBubbleWrap.selectedProperty().not());
        tfDuctTape.disableProperty().bind(chbxDuctTape.selectedProperty().not());


        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpPackFrame opData = (OpPackFrame) data;

        countInitialValues();

        roofWrapL = ((width * MM_TO_M + 0.1) * (depth * MM_TO_M + 0.1) * 1.2);
        if(roofWrap) tfRoofWrap.setText(String.format(DOUBLE_FORMAT, roofWrapL));
        else tfRoofWrap.setText("");

        sideWrapL = (height * MM_TO_M * 4.0 * 1.1);
        if(sideWrap) tfSideWrap.setText(String.format(DOUBLE_FORMAT, sideWrapL));
        else tfSideWrap.setText("");

        stretchWrapL = ((width * MM_TO_M + depth * MM_TO_M) * 2 * height * MM_TO_M / 0.3 * 2);
        if(stretchWrap) tfStretchWrap.setText(String.format(DOUBLE_FORMAT, stretchWrapL));
        else tfStretchWrap.setText("");

        polyWrapL = (height * MM_TO_M * 1.15 * 4.0 + 2.0 * width * MM_TO_M);
        if(polyWrap) tfPolyWrap.setText(String.format(DOUBLE_FORMAT, polyWrapL));
        else tfPolyWrap.setText("");

        bubbleWrapL = ((width * MM_TO_M + depth * MM_TO_M) * 2.0 * height * MM_TO_M * 1.1);
        if(bubbleWrap) tfBubbleWrap.setText(String.format(DOUBLE_FORMAT, bubbleWrapL));
        else tfBubbleWrap.setText("");

        ductTapeL = ((width * MM_TO_M + depth * MM_TO_M) * 2.0 * 1.5 * height * MM_TO_M / 0.5);
        if(ductTape) tfDuctTape.setText(String.format(DOUBLE_FORMAT, ductTapeL));
        else tfDuctTape.setText("");

        currentNormTime = 0.0;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        width = IntegerParser.getValue(tfWidth);
        depth = IntegerParser.getValue(tfDepth);
        height = IntegerParser.getValue(tfHeight);

        roofWrap = chbxRoofWrap.isSelected();
        sideWrap = chbxSideWrap.isSelected();
        stretchWrap = chbxStretchWrap.isSelected();
        polyWrap = chbxPolyWrap.isSelected();
        bubbleWrap = chbxBubbleWrap.isSelected();
        ductTape = chbxDuctTape.isSelected();
    }

    private void collectOpData(OpPackFrame opData){
        opData.setWidth(width);
        opData.setDepth(depth);
        opData.setHeight(height);

        opData.setRoofWrap(chbxRoofWrap.isSelected());
        opData.setSideWrap(chbxSideWrap.isSelected());
        opData.setStretchWrap(chbxStretchWrap.isSelected());
        opData.setPolyWrap(chbxPolyWrap.isSelected());
        opData.setBubbleWrap(chbxBubbleWrap.isSelected());
        opData.setDuctTape(chbxDuctTape.isSelected());

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPackFrame opData = (OpPackFrame)data;

        width = opData.getWidth();
        tfWidth.setText(String.valueOf(width));

        depth = opData.getDepth();
        tfDepth.setText(String.valueOf(depth));

        height = opData.getHeight();
        tfHeight.setText(String.valueOf(height));

        //-------------------------------------------------

        roofWrap = opData.isRoofWrap();
        chbxRoofWrap.setSelected(roofWrap);

        sideWrap = opData.isSideWrap();
        chbxSideWrap.setSelected(sideWrap);

        stretchWrap = opData.isStretchWrap();
        chbxStretchWrap.setSelected(stretchWrap);

        polyWrap = opData.isPolyWrap();
        chbxPolyWrap.setSelected(polyWrap);

        bubbleWrap = opData.isBubbleWrap();
        chbxBubbleWrap.setSelected(bubbleWrap);

        ductTape = opData.isDuctTape();
        chbxDuctTape.setSelected(ductTape);

    }

}
