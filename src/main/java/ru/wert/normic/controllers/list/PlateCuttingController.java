package ru.wert.normic.controllers.list;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.help.HelpWindow;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * ПОЛУЧЕНИЕ ДЕТАЛИ НА ЛАЗЕРНОМ СТАНКЕ С КРП
 *
 * При создании класса создается экзепляр класса OpCutting
 * В этом классе хранятся все актуальные значения полей, введенные пользователем,
 * они обновляются при любом изменении полей плашки.
 */
public class PlateCuttingController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

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

    @FXML
    private ImageView ivHelp;

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
        OpCutting opData = (OpCutting) data;

        new TFIntegerColored(tfHoles, this);
        new TFIntegerColored(tfPerfHoles, this);
        new TFIntegerColored(tfExtraPerimeter, this);
        new TFNormTime(tfNormTime, formController);
        new ChBox(chbxStripping, this);
        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> {
            formController.countSumNormTimeByShops();
        });

        ivHelp.setOnMouseClicked(e->{
            HelpWindow.create(e, "РАСКРОЙ И ЗАЧИСТКА", helpText(), helpImage());
        });
    }

    /**
     * Метод вызывается для пересчета норм времени при любом изменении значения полей плашки
     * Сначала  в методе countInitialValues() происходит сбор необходимых для расчета значений.
     * После выполненных вычислений переменная currentNormTime обновляется, и в методе collectOpData() значения полей
     * плашки вместе с полученным значением нормы времени сохраняются в класс OpData
     */
    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpCutting opData = (OpCutting) data;

        countInitialValues();

        final double PLUS_LENGTH = extraPerimeter * MM_TO_M;

        double speed;
        //Скорость резания, м/мин
        if (t < 1.5) speed = 5.5;
        else if (t >= 1.5 && t < 2) speed = 5.0;
        else if (t >= 2 && t < 2.5 ) speed = 4.0;
        else if (t >= 2.5 && t < 3.0) speed = 3.0;
        else speed = 1.9;

        //Время зачистки
        double strippingTime; //мин
        if(stripping){
            strippingTime = ((perimeter + PLUS_LENGTH) * STRIPING_SPEED + holes) / 60;
        } else
            strippingTime = 0.0;

        double time;

        time = ((perimeter + PLUS_LENGTH)/speed                 //Время на резку по периметру
                + CUTTING_SPEED * area                          //Время подготовительное - заключительоне
                + REVOLVER_SPEED * holes                //Время на пробивку отверстий
                + PERFORATION_SPEED * perfHoles)        //Время на пробивку перфорации
                * CUTTING_SERVICE_RATIO
                + strippingTime;

        if(area == 0.0) time = 0.0;

        currentNormTime = time;//результат в минутах
        collectOpData(opData);
        setTimeMeasurement();
    }


    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        paramA = IntegerParser.getValue(((FormDetailController)formController).getMatPatchController().getTfA());
        paramB = IntegerParser.getValue(((FormDetailController)formController).getMatPatchController().getTfB());
        t = ((FormDetailController)formController).getCmbxMaterial().getValue().getParamS();
        perimeter = 2 * (paramA + paramB) * MM_TO_M;
        area = paramA * paramB * MM2_TO_M2;
        extraPerimeter = IntegerParser.getValue(tfExtraPerimeter);
        stripping = chbxStripping.isSelected();
        holes = IntegerParser.getValue(tfHoles);
        perfHoles = IntegerParser.getValue(tfPerfHoles);

    }

    /**
     * Метод собирает данные с полей плашки на операцию в класс OpData
     * Вызывается при изменении любого значения на операционной плашке
     */
    private void collectOpData(OpCutting opData){
        opData.setHoles(holes);
        opData.setPerfHoles(perfHoles);
        opData.setExtraPerimeter(extraPerimeter);
        opData.setStripping(stripping);

        opData.setMechTime(currentNormTime);
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

    private String helpText() {
        return String.format("N отв - количество отверстий, пробиваемых стандартным инструментом;\n" +
                        "N перф. отв - количество пробиваемых отверстий в перфорации, если таковая есть;\n" +
                        "\t\t(кроме отверстий нарезаеммых лазером)\n" +
                        "P экстра , мм - периметр, вырезаемый станком в детали помимо\n" +
                        "\t\tосновного контура;\n" +
                        "\n" +
                        "Время резания вычисляется по формуле:\n" +
                        "Трез = (P / Vрез + Tпз.рез х Sдет + Vрев * N отв + Vперф x N перф) х\n" +
                        "\t\t\t Tпз.общ + Tчист,\n" +
                        "где \n" +
                        "\tP =  Pпер + P экстра - общий периметр резания , м;\n" +
                        "\tVрез - скорость резания, зависящая от толщины материала\n" +
                        "\t\t(t = 1.0 - 5.5 м/мин, t = 1.5 - 4.0 м/мин, t = 2.0 - 2.5 м/мин)\n" +
                        "\tTпз.рез = %s - ПЗ время, зависящее от площади детали, мин;\n" +
                        "\tSдет - площадь обрабатываемой детали, м2;\n" +
                        "\tVрев = %s - скорость револьверной головки, мин/удар;\n" +
                        "\tVперф = %s  - скорость перфорации,  мин/удар;\n" +
                        "\tTпз.общ =%s - ПЗ время общее,  мин;\n" +
                        "\tTчист  - время зачистки, мин.\n" +
                        "\n" +
                        "Зачистка выполняется ручным инструментом:\n" +
                        "Tчист = P x Vчист + N отв,\n" +
                        "где\n" +
                        "\tVчист = %s - скорость зачистки, сек/м.\n",
                CUTTING_SPEED, REVOLVER_SPEED, PERFORATION_SPEED, CUTTING_SERVICE_RATIO, STRIPING_SPEED);
    }

    private Image helpImage() {
        Image image = new Image(getClass().getResource("/pics/help/cutting.PNG").toString());
        return image;
    }

}
