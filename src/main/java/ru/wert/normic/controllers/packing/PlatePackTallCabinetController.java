package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpPackTallCabinet;
import ru.wert.normic.utils.IntegerParser;

/**
 * УПАКОВКА ВЫСОКОГО ШКАФА
 */
public class PlatePackTallCabinetController extends AbstractOpPlate {


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
    private boolean useRoofWrap, useSideWrap, useStretchWrap, usePolyWrap, useBubbleWrap, useDuctTape;
    private Double roofWrapL, sideWrapL, stretchWrapL, polyWrapL, bubbleWrapL, ductTapeL;
    private Integer totalCartoon;


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpPackTallCabinet opData = (OpPackTallCabinet) data;
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
        OpPackTallCabinet opData = (OpPackTallCabinet) data;

        countInitialValues();

        roofWrapL = ((width * MM_TO_M + 0.1) * (depth * MM_TO_M + 0.1) * 1.2);
        if(useRoofWrap) tfRoofWrap.setText(String.format(DOUBLE_FORMAT, roofWrapL));
        else tfRoofWrap.setText("");

        sideWrapL = (height * MM_TO_M * 4.0 * 1.1);
        if(useSideWrap) tfSideWrap.setText(String.format(DOUBLE_FORMAT, sideWrapL));
        else tfSideWrap.setText("");

        stretchWrapL = ((width * MM_TO_M + depth * MM_TO_M) * 2 * height * MM_TO_M / 0.3 * 2);
        if(useStretchWrap) tfStretchWrap.setText(String.format(DOUBLE_FORMAT, stretchWrapL));
        else tfStretchWrap.setText("");

        polyWrapL = (height * MM_TO_M * 1.15 * 4.0 + 2.0 * width * MM_TO_M);
        if(usePolyWrap) tfPolyWrap.setText(String.format(DOUBLE_FORMAT, polyWrapL));
        else tfPolyWrap.setText("");

        bubbleWrapL = ((width * MM_TO_M + depth * MM_TO_M) * 2.0 * height * MM_TO_M * 1.1);
        if(useBubbleWrap) tfBubbleWrap.setText(String.format(DOUBLE_FORMAT, bubbleWrapL));
        else tfBubbleWrap.setText("");

        ductTapeL = ((width * MM_TO_M + depth * MM_TO_M) * 2.0 * 1.5 * height * MM_TO_M / 0.5);
        if(useDuctTape) tfDuctTape.setText(String.format(DOUBLE_FORMAT, ductTapeL));
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

        useRoofWrap = chbxRoofWrap.isSelected();
        useSideWrap = chbxSideWrap.isSelected();
        useStretchWrap = chbxStretchWrap.isSelected();
        usePolyWrap = chbxPolyWrap.isSelected();
        useBubbleWrap = chbxBubbleWrap.isSelected();
        useDuctTape = chbxDuctTape.isSelected();
    }

    private void collectOpData(OpPackTallCabinet opData){
        opData.setCartoon(roofWrapL + sideWrapL);
        opData.setStretchMachineWrap(stretchWrapL);
        opData.setBubbleWrap(bubbleWrapL);
        opData.setPolyWrap(polyWrapL);
        opData.setDuctTape(ductTapeL);

        opData.setWidth(width);
        opData.setDepth(depth);
        opData.setHeight(height);

        opData.setUseRoofWrap(chbxRoofWrap.isSelected());
        opData.setUseSideWrap(chbxSideWrap.isSelected());
        opData.setUseStretchWrap(chbxStretchWrap.isSelected());
        opData.setUsePolyWrap(chbxPolyWrap.isSelected());
        opData.setUseBubbleWrap(chbxBubbleWrap.isSelected());
        opData.setUseDuctTape(chbxDuctTape.isSelected());

        opData.setPackTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPackTallCabinet opData = (OpPackTallCabinet)data;

        width = opData.getWidth();
        tfWidth.setText(String.valueOf(width));

        depth = opData.getDepth();
        tfDepth.setText(String.valueOf(depth));

        height = opData.getHeight();
        tfHeight.setText(String.valueOf(height));

        //-------------------------------------------------

        useRoofWrap = opData.isUseRoofWrap();
        chbxRoofWrap.setSelected(useRoofWrap);

        useSideWrap = opData.isUseSideWrap();
        chbxSideWrap.setSelected(useSideWrap);

        useStretchWrap = opData.isUseStretchWrap();
        chbxStretchWrap.setSelected(useStretchWrap);

        usePolyWrap = opData.isUsePolyWrap();
        chbxPolyWrap.setSelected(usePolyWrap);

        useBubbleWrap = opData.isUseBubbleWrap();
        chbxBubbleWrap.setSelected(useBubbleWrap);

        useDuctTape = opData.isUseDuctTape();
        chbxDuctTape.setSelected(useDuctTape);

    }

}
