package ru.wert.normic.controllers.listOperations;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.settings.NormConstants.*;

/**
 * ПОЛУЧЕНИЕ ДЕТАЛИ НА ЛАЗЕРНОМ СТАНКЕ С КРП
 *
 * При создании класса создается экзепляр класса OpCutting
 * В этом классе хранятся все актуальные значения полей, введенные пользователем,
 * они обновляются при любом изменении полей плашки.
 */
public class PlateCuttingController extends AbstractOpPlate {

    @FXML
    private TextField tfHoles;

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfPerfHoles;

    @FXML
    private CheckBox chbxStripping;

    @FXML
    private TextField tfExtraPerimeter;

    private OpCutting opData;

    private Material material; //Материал
    private double perimeter; //Периметр контура развертки
    private double area; //Площадь развертки
    private int extraPerimeter; //Дополнительный периметр обработки
    private double t; //Толщина материала
    private int paramA; //Параметр А развертки
    private int paramB; //Параметр B развертки
    private boolean stripping = false; //Применить зачистку
    private int holes; //Количество отверстий в развертке
    private int perfHoles; //Количество перфораций в развертке


    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFIntegerColored(tfHoles, this);
        new TFIntegerColored(tfPerfHoles, this);
        new TFIntegerColored(tfExtraPerimeter, this);
        new TFNormTime(tfNormTime, prevFormController);
        new ChBox(chbxStripping, this);

//        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
//            prevFormController.countSumNormTimeByShops();
//        });
    }

    /**
     * Метод вызывается для пересчета норм времени при любом изменении значения полей плашки
     * Сначала  в методе countInitialValues() происходит сбор необходимых для расчета значений.
     * После выполненных вычислений переменная currentNormTime обновляется, и в методе collectOpData() значения полей
     * плашки вместе с полученным значением нормы времени сохраняются в класс OpData
     */
    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpCutting) data;

        countInitialValues();

        currentNormTime = opData.getNormCounter().count(data).getMechTime();//результат в минутах

        setTimeMeasurement();
    }


    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        material = ((FormDetailController) prevFormController).getCmbxMaterial().getValue();
        paramA = IntegerParser.getValue(((FormDetailController) prevFormController).getMatPatchController().getTfA());
        paramB = IntegerParser.getValue(((FormDetailController) prevFormController).getMatPatchController().getTfB());
        t = material.getParamS();
        perimeter = 2 * (paramA + paramB) * MM_TO_M;
        area = paramA * paramB * MM2_TO_M2;
        extraPerimeter = IntegerParser.getValue(tfExtraPerimeter);
        stripping = chbxStripping.isSelected();
        holes = IntegerParser.getValue(tfHoles);
        perfHoles = IntegerParser.getValue(tfPerfHoles);

        collectOpData();

    }

    private void collectOpData(){
        opData.setMaterial(material);
        opData.setParamA(paramA);
        opData.setParamB(paramB);
        opData.setHoles(holes);
        opData.setPerfHoles(perfHoles);
        opData.setExtraPerimeter(extraPerimeter);
        opData.setStripping(stripping);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpCutting opData = (OpCutting) data;

        stripping = opData.isStripping();
        chbxStripping.setSelected(stripping);

        holes = opData.getHoles();
        tfHoles.setText(String.valueOf(holes));

        perfHoles = opData.getPerfHoles();
        tfPerfHoles.setText(String.valueOf(perfHoles));

        extraPerimeter = opData.getExtraPerimeter();
        tfExtraPerimeter.setText(String.valueOf(extraPerimeter));
    }

    public String helpText() {
        return String.format("N отв - количество отверстий, пробиваемых стандартным инструментом;\n" +
                        "N перф. отв - количество пробиваемых отверстий в перфорации, если таковая есть;\n" +
                        "\t\t(кроме отверстий нарезаеммых лазером)\n" +
                        "P экстра , мм - периметр, вырезаемый станком в детали помимо\n" +
                        "\t\tосновного контура;\n\n" +
                        "Время резания вычисляется по формуле:\n\n" +
                        "\t\t\tТрез = (P / V рез + T пз.рез х S дет + \n" +
                        "\t\t\t\t\t+ V рев * N отв + V перф x N перф) х K пз.общ\n" +
                        "\t\t\t\t\t+ T чист, мин\n" +
                        "где \n" +
                        "\tP =  Pпер + P экстра - общий периметр резания , м;\n" +
                        "\tV рез - скорость резания, зависящая от толщины материала\n" +
                        "\t\t(t = 1.0 - 5.5 м/мин, t = 1.5 - 4.0 м/мин, t = 2.0 - 2.5 м/мин)\n" +
                        "\tT пз.рез = %s - ПЗ время, зависящее от площади детали, мин;\n" +
                        "\tS дет - площадь обрабатываемой детали, м2;\n" +
                        "\tV рев = %s - скорость револьверной головки, мин/удар;\n" +
                        "\tV перф = %s  - скорость перфорации,  мин/удар;\n" +
                        "\tK пз.общ =%s - коэффициент ПЗ времени общего,  мин;\n" +
                        "\tT чист  - время зачистки, мин.\n" +
                        "\n" +
                        "Зачистка выполняется ручным инструментом:\n\n" +
                        "\t\t\tT чист = P x V чист + N отв,\n" +
                        "где\n" +
                        "\tV чист = %s - скорость зачистки, сек/м.\n",
                CUTTING_SPEED, REVOLVER_SPEED, PERFORATION_SPEED, CUTTING_SERVICE_RATIO, STRIPING_SPEED);
    }

    public Image helpImage() {
        Image image = new Image(getClass().getResource("/pics/help/cutting.PNG").toString());
        return image;
    }

}
