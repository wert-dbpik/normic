package ru.wert.normic.controllers.paint;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPaintingDifficulty;
import ru.wert.normic.help.HelpWindow;
import ru.wert.normic.materials.matlPatches.ListMatPatchController;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * ОКРАШИВАНИЕ ЛИСТОВОЙ ДЕТАЛИ
 */
public class PlatePaintController extends AbstractOpPlate {

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
    private TextField tfAlong;

    @FXML
    private TextField tfAcross;

    @FXML
    private ComboBox<EPaintingDifficulty> cmbxDifficulty;

    @FXML@Getter
    private ImageView ivHelp;

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
    public void initViews(OpData opData) {

        new BXPaintingDifficulty().create(cmbxDifficulty, ((OpPaint)opData).getDifficulty(), this);
        new BXColor().create(cmbxColor, ((OpPaint)opData).getColor(), this);
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfAlong, this);
        new TFIntegerColored(tfAcross, this);
        new TFIntegerColored(tfHangingTime, this);
        new CmBx(cmbxColor, this);
        new CmBx(cmbxDifficulty, this);
        new ChBox(chbxTwoSides, this);

        ivHelp.setOnMouseClicked(e->{
            HelpWindow.create(e, "ПОКРАСКА", helpText(), helpImage());
        });

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
     * Устанавливает и рассчитывает значения, заданные пользователем
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


    public String helpText() {
        return String.format("Цвет краски - палитра цвета устанавливается в отдельном окне «палитра»;\n" +
                        "А(вдоль) - размер навешенной детали вдоль штанги, мм;\n" +
                        "В(поперек) - размер навешенной детали поперек штанги, мм;\n" +
                        "Т навеш - время навешивания (от 7 до 24 сек);\n" +
                        "С 2х сторон - по умолчанию, окрашивание с одной стороны - \n" +
                        "\t\tэто редкий случай для корпусной детали из нержавейки;\n" +
                        "Сложность - выбирается в зависимости от трудностей прокрашивания.\n" +
                        "Площадь покрытия и расход краски рассчитываются по габаритам развертки.\n" +
                        "\n" +
                        "Время операции окрашивания вычисляется по формуле:\n\n" +
                        "\t\t\tТокр = Т навеш/60 + Т мойки/60 + Т продув/60 + \n" +
                        "\t\t\t\t\t+ Т сушки/60/ N с.штанг/ N с.дет + \n" +
                        "\t\t\t\t\t+ K слож х (2 х S покр)^0.7 + \n" +
                        "\t\t\t\t\t+ Т печь / N п.штанг/ N п.дет, мин;\n" +
                        "где \n" +
                        "\tТ навеш = время навешивания, сек ;\n" +
                        "\tТ мойки = %s - время мойки, сек;\n" +
                        "\tТ продув = %s - время продувки, сек;\n" +
                        "\tТ сушки = %s - время сушки, сек;\n" +
                        "\tN с.штанг = количество штанг в сушилке;\n" +
                        "\tN с.дет = количество деталей на штанге;\n" +
                        "\tK слож - коэффициент сложности (значения: 1.0 - 1.4 - 2.0);\n" +
                        "\tS покр - площадь покрытия, м2;\n" +
                        "\t Т печь = %s - время полимеризации в печи, мин;\n" +
                        "\tN п.штанг = количество штанг в печи;\n" +
                        "\tN п.дет = количество деталей на штанге в печи;\n" +
                        "Количество деталей на штанге вычисляется в зависимости отот габаритов " +
                        "детали и оборудования.\n",
                WASHING, WINDING, DRYING, BAKING);
    }

    public Image helpImage() {
        return null;
    }
}
