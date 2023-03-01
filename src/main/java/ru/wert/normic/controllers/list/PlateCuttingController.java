package ru.wert.normic.controllers.list;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.ChBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.entities.ops.opList.OpCutting;
import ru.wert.normic.entities.ops.OpData;
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
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

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
    private ImageView ivHelpOnUseStripping;

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
            strippingTime = ((perimeter + PLUS_LENGTH) * 2.5 + holes) / 60;
        } else
            strippingTime = 0.0;

        double time;

        time = ((perimeter + PLUS_LENGTH)/speed                 //Время на резку по периметру
                + 1.28 * area                              //Время подготовительное - заключительоне
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

}
