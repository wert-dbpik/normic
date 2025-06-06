package ru.wert.normic.controllers.paint;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaintDetail;
import ru.wert.normic.enums.EAssemblingType;
import ru.wert.normic.enums.EColor;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * ОКРАШИВАНИЕ СБОРОЧНОЙ ЕДИНИЦЫ
 */
public class PlatePaintDetailController extends AbstractOpPlate {

    @FXML
    private ComboBox<EColor> cmbxColor;

    @FXML
    private TextField tfDyeWeight;

    @FXML
    private CheckBox chbxTwoSides;

    @FXML
    private ComboBox<EAssemblingType> cmbxAssemblingType;

    @FXML
    private TextField tfCalculatedArea;

    @FXML
    private TextField tfAlong;

    @FXML
    private TextField tfAcross;

    @FXML@Getter
    private ImageView ivHelp;

    @FXML
    private TextField tfNormTime;

    private OpPaintDetail opData;

    private EColor color; //Цвет краски
    private double dyeWeight; //Вес краски
    private int along; //Параметр А вдоль штанги
    private int across; //Параметр B поперек штанги
    private double area; //Площадь покрытия введенная вручную
    private boolean twoSides; //Красить с двух сторон

    private double kArea; //k = 1, если chbxTwoSides isSelected (окрашивание с двух сторон), иначе k = 0.5

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpPaintDetail opData = (OpPaintDetail) data;


        prevFormController.getFormAreaProperty().addListener((observable, oldValue, newValue) -> {
            countNorm(opData);
        });

        new ChBox(chbxTwoSides, this);
        new BXColor().create(cmbxColor, opData.getColor(), this);
        new BXAssemblingType().create(cmbxAssemblingType, opData.getAssmType(), this);
        new TFNormTime(tfNormTime, prevFormController);
        new TFIntegerColored(tfAlong, this);
        new TFIntegerColored(tfAcross, this);
        new CmBx(cmbxAssemblingType, this);

        countNorm(opData);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpPaintDetail)data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);

        tfCalculatedArea.setText(String.format(DOUBLE_FORMAT, opData.getCountedArea()));
        tfDyeWeight.setText(String.format(DOUBLE_FORMAT, opData.getDyeWeight()));
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    public void countInitialValues() {

        along = IntegerParser.getValue(tfAlong);
        across = IntegerParser.getValue(tfAcross);

        collectOpData();
    }

    private void collectOpData(){

        opData.setMaterial (((FormDetailController) prevFormController).getCmbxMaterial().getValue());

        opData.setRazvA(IntegerParser.getValue(((FormDetailController) prevFormController).getMatPatchController().getTfA()));
        opData.setRazvB(IntegerParser.getValue(((FormDetailController) prevFormController).getMatPatchController().getTfB()));
        opData.setColor(cmbxColor.getValue());
        opData.setTwoSides(chbxTwoSides.isSelected());
        opData.setAlong(along);
        opData.setAcross(across);
        opData.setAssmType(cmbxAssemblingType.getValue());

    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPaintDetail opData = (OpPaintDetail)data;

        cmbxColor.setValue(opData.getColor());

        twoSides = opData.isTwoSides();
        chbxTwoSides.setSelected(twoSides);

        area = opData.getCountedArea();

        tfCalculatedArea.setText(String.format(DOUBLE_FORMAT, area));

        along = opData.getAlong();
        tfAlong.setText(String.valueOf(along));

        across = opData.getAcross();
        tfAcross.setText(String.valueOf(across));

        double pantingSpeed = opData.getAssmType().getSpeed();
        cmbxAssemblingType.setValue(opData.getAssmType());
    }

    @Override
    public String helpText() {
        return String.format("Площадь окрашивания можно задавать вручную и использовать расчетное \n " +
                        "\tзначения, полученное суммерованием всех входящих в сборку деталей.\n " +
                        "\t(Входящие в сборку подсборки не суммируются!)\n" +
                        "Цвет - палитра цвета и норма расхода устанавливаются в отдельном окне «палитра»;\n" +
                        "А(вдоль) - размер навешенной сборки вдоль штанги, мм;\n" +
                        "В(поперек) - размер навешенной сборки поперек штанги, мм;\n" +
                        "С 2х сторон - по умолчанию, окрашивание с одной стороны - \n" +
                        "\t\tэто редкий случай для корпусной детали из нержавейки;\n" +
                        "Тип сборки - влияет на скорость окрашивания;\n" +
                        "\n" +
                        "Время операции окрашивания вычисляется по формуле:\n\n" +
                        "\t\t\tТокр = Т навеш + S изд х Т продув + S покр х V окр + \n" +
                        "\t\t\t\t\t+ Т печь / N п.штанг/ N п.дет;\n" +
                        "где \n" +
                        "\tТ навеш = %s - время навешивания до и после полимеризации, мин ;\n" +
                        "\tS покр - площадь изделия суммируется исходя из площади входящих деталей\n " +
                        "\t\t\tили указывается пользователем вручную;\n" +
                        "\tТ продув = %s - время продувки и перемещение изделя на штанге, мин;\n" +
                        "\tV окр = скорость окрашивания зависит от типа сборки:\n " +
                        "\t\t%s мин - для глухих шкафов, %s мин для открытых рам и кроссов;\n" +
                        "\tТ печь = %s - время полимеризации в печи, мин;\n" +
                        "\tN п.штанг = количество штанг в печи (для цветной краски - штанг не более 4);\n" +
                        "\tN п.дет = количество сборок на штанге в печи;\n" +
                        "Количество сборок на штанге вычисляется в зависимости от габаритов\n " +
                        "детали и оборудования.\n",
                HANGING_TIME, WINDING_MOVING_SPEED, SOLID_BOX_SPEED, FRAME_SPEED, BAKING);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
