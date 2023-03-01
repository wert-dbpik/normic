package ru.wert.normic.controllers.paint;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.opPaint.OpPaint;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.EPaintingDifficulty;
import ru.wert.normic.materials.matlPatches.ListMatPatchController;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * ОКРАШИВАНИЕ ЛИСТОВОЙ ДЕТАЛИ
 */
public class PlatePaintController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private ComboBox<EColor> cmbxColor;

    @FXML
    private TextField tfCoatArea;

    @FXML
    private TextField tfDyeWeight;

    @FXML
    private CheckBox chbxTwoSides;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfAlong;

    @FXML
    private TextField tfAcross;

    @FXML
    private ComboBox<EPaintingDifficulty> cmbxDifficulty;

    @FXML
    private ImageView ivHelpOnA;

    @FXML
    private TextField tfHangingTime;

    @FXML
    private TextField tfNormTime;

    private double kArea; //С двух сторон
    private int razvA; //Параметр А развертки
    private int razvB; //Параметр B развертки
    private int along; //Параметр А - габарит сложенной детали вдоль штанги
    private int across; //Параметр B - габарит сложенной детали поперек штанги
    private EColor color; //Цвет покрытия
    private double coatArea; //Площадь развертки
    private double dyeWeight; //Вес краски
    private double difficulty; //Сложность окрашивания
    private int hangingTime; //Время навешивания
    private boolean twoSides; //Красить с двух сторон

    @Override //AbstractOpPlate
    public void initViews(OpData data) {
        OpPaint opData = (OpPaint) data;
        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        new BXPaintingDifficulty().create(cmbxDifficulty, opData.getDifficulty(), this);
        new BXColor().create(cmbxColor, opData.getColor(), this);
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfAlong, this);
        new TFIntegerColored(tfAcross, this);
        new TFIntegerColored(tfHangingTime, this);
        new CmBx(cmbxColor, this);
        new CmBx(cmbxDifficulty, this);
        new ChBox(chbxTwoSides, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data) {
        OpPaint opData = (OpPaint) data;

        countInitialValues();

        if (((FormDetailController) formController).getMatPatchController() instanceof ListMatPatchController) {
            //Площадь покрытия
            tfCoatArea.setText(String.format(DOUBLE_FORMAT, coatArea * kArea));
            //Вес краски
            dyeWeight = (color.getConsumption() * 0.001) * coatArea * kArea;
        } else {
            //Площадь покрытия
            tfCoatArea.setText(String.format(DOUBLE_FORMAT, coatArea));
            //Вес краски
            dyeWeight = (color.getConsumption()) * 0.001 * coatArea;
        }

        tfDyeWeight.setText(String.format(DOUBLE_FORMAT, dyeWeight));

        final double HOLDING_TIME = hangingTime / 60.0; //время навешивания, мин

        final int alongSize = Math.max(along, across) + DETAIL_DELTA;
        final int acrossSize = Math.min(along, across) + DETAIL_DELTA;

        //Количество штанг в сушилке
        int dryingBars;
        if (acrossSize < 99) dryingBars = 3;
        else if (acrossSize >= 100 && acrossSize <= 300) dryingBars = 2;
        else dryingBars = 1;

        int partsOnBar = 2500 / alongSize;

        //Количество штанг в печи
        int bakeBars;
        if (acrossSize < 49) bakeBars = 6;
        else if (acrossSize >= 50 && acrossSize <= 99) bakeBars = 5;
        else if (acrossSize >= 100 && acrossSize <= 199) bakeBars = 4;
        else if (acrossSize >= 200 && acrossSize <= 299) bakeBars = 3;
        else if (acrossSize >= 300 && acrossSize <= 399) bakeBars = 2;
        else bakeBars = 1;

        double time;
        time = HOLDING_TIME //Время навешивания
                + ((WASHING / 60.0) + (WINDING / 60.0) + (DRYING / 60.0) / dryingBars) / partsOnBar //Время подготовки к окрашиванию
                + Math.pow(2 * coatArea, 0.7) * difficulty //Время нанесения покрытия
                + 40.0 / bakeBars / partsOnBar;  //Время полимеризации
        if (coatArea == 0.0) time = 0.0;

        currentNormTime = time;//результат в минутах
        collectOpData(opData);
        setTimeMeasurement();
    }


    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {

        razvA = IntegerParser.getValue(((FormDetailController) formController).getMatPatchController().getTfA());
        razvB = IntegerParser.getValue(((FormDetailController) formController).getMatPatchController().getTfB());

        color = cmbxColor.getValue();

        if (((FormDetailController) formController).getMatPatchController() instanceof ListMatPatchController) {
            twoSides = chbxTwoSides.isSelected();
            kArea = twoSides ? 1.0 : 0.5;

            coatArea = razvA * razvB * 2 * MM2_TO_M2; //Площадь покрытия с двух сторон
        } else {
            //Масса погонного метра
            double diameter
                    = ((FormDetailController) formController).getCmbxMaterial().getValue().getParamS();
            coatArea = 3.14 * diameter * (razvA + razvB) * MM2_TO_M2;
        }
        along = IntegerParser.getValue(tfAlong);
        across = IntegerParser.getValue(tfAcross);
        if (along == 0 && across == 0) {
            along = Math.min(razvA, razvB);
            across = 0;
        }
        difficulty = cmbxDifficulty.getValue().getDifficultyRatio();
        hangingTime = IntegerParser.getValue(tfHangingTime);


    }

    private void collectOpData(OpPaint opData) {
        opData.setColor(color);
        opData.setArea(coatArea);
        opData.setDyeWeight(dyeWeight);
        opData.setTwoSides(twoSides);
        opData.setAlong(along);
        opData.setAcross(across);
        opData.setDifficulty(cmbxDifficulty.getValue());
        opData.setHangingTime(hangingTime);

        opData.setPaintTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data) {
        OpPaint opData = (OpPaint) data;

        cmbxColor.setValue(opData.getColor());

        twoSides = opData.isTwoSides();
        chbxTwoSides.setSelected(twoSides);

        along = opData.getAlong();
        tfAlong.setText(String.valueOf(along));

        across = opData.getAcross();
        tfAcross.setText(String.valueOf(across));

        difficulty = opData.getDifficulty().getDifficultyRatio();
        cmbxDifficulty.setValue(opData.getDifficulty());

        hangingTime = opData.getHangingTime();
        tfHangingTime.setText(String.valueOf(hangingTime));
    }

}
