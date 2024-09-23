package ru.wert.normic.controllers.paint;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaint;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.enums.EPaintingDifficulty;
import ru.wert.normic.help.HelpWindow;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

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

    private OpPaint opData;

    private Material material; //Материал
    private int razvA; //Параметр А развертки
    private int razvB; //Параметр B развертки
    private int along; //Параметр А - габарит сложенной детали вдоль штанги
    private int across; //Параметр B - габарит сложенной детали поперек штанги
    private int hangingTime; //Время навешивания
    private boolean twoSides; //Красить с двух сторон

    @Override //AbstractOpPlate
    public void initViews(OpData opData) {

        new BXPaintingDifficulty().create(cmbxDifficulty, ((OpPaint)opData).getDifficulty(), this);
        new BXColor().create(cmbxColor, ((OpPaint)opData).getColor(), this);
        new TFNormTime(tfNormTime, prevFormController);
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
        opData = (OpPaint) data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getPaintTime();//результат в минутах

        tfCoatArea.setText(String.format(DOUBLE_FORMAT, opData.getArea()));
        tfDyeWeight.setText(String.format(DOUBLE_FORMAT, opData.getDyeWeight()));

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {

        material = ((FormDetailController) prevFormController).getCmbxMaterial().getValue();

        razvA = IntegerParser.getValue(((FormDetailController) prevFormController).getMatPatchController().getTfA());
        razvB = IntegerParser.getValue(((FormDetailController) prevFormController).getMatPatchController().getTfB());

        along = IntegerParser.getValue(tfAlong);
        across = IntegerParser.getValue(tfAcross);
        if (along == 0 && across == 0) {
            along = Math.min(razvA, razvB);
            across = 0;
        }
        hangingTime = IntegerParser.getValue(tfHangingTime);

        collectOpData();
    }

    private void collectOpData() {
        opData.setMaterial(material);
        opData.setColor(cmbxColor.getValue());
        opData.setTwoSides(chbxTwoSides.isSelected());
        opData.setDifficulty(cmbxDifficulty.getValue());
        opData.setHangingTime(hangingTime);
        opData.setRazvA(razvA);
        opData.setRazvB(razvB);

        opData.setAlong(along);
        opData.setAcross(across);
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
                        "\tN п.штанг = количество штанг в печи (для цветной краски - штанг не более 4);\n" +
                        "\tN п.дет = количество деталей на штанге в печи;\n" +
                        "Количество деталей на штанге вычисляется в зависимости отот габаритов " +
                        "детали и оборудования.\n",
                WASHING, WINDING, DRYING, BAKING);
    }

    public Image helpImage() {
        return null;
    }
}
