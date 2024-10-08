package ru.wert.normic.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers.paint.PlatePaintAssmController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPaint.OpPaintAssm;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.enums.ENormType;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.help.HelpWindow;
import ru.wert.normic.interfaces.IOpPlate;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.text.DecimalFormat;
import java.util.List;

import static java.lang.String.format;
import static ru.wert.normic.AppStatics.*;
import static ru.wert.normic.enums.ETimeMeasurement.*;


/**
 * Класс наследует интерфейс IOpPlate
 * Изначальный расчет нормы времени производится в минутах
 * Конвертация в секунды происходит в методе setTimeMeasurement()
 */
public abstract class AbstractOpPlate implements IOpPlate {

    public static OpData bufferedOpData = null; //Операция, хранимая для копирования/вырезания
    public static AbstractFormController whereFromController; //Операция в которой находится bufferedOpData
    public static boolean deleteWhenPaste; //Флаг удаления bufferedOpData после вставки

    // КОНСТАНТЫ
    public static final double MM_TO_M = 0.001; //перевод мм в метры
    public static final double MM2_TO_M2 = 0.000001; //перевод мм квадратных в квадратные метры
    public static final double MM3_TO_M3 = 0.000000001; //перевод мм квадратных в квадратные метры
    public static final double MIN_TO_SEC = 60; //перевод минут в секунды
    public static final double MIN_TO_HOUR = 1.0/60; //перевод минут в часы
    public static final double SEC_TO_MIN = 1.0/60; //перевод секунды в минут
    public static final double RO = 0.00000785; //плотность стали кг/м3
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.###");
    public static final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("###");
    public static final String DOUBLE_FORMAT = "%5.3f";
    public static final String INTEGER_FORMAT = "%10.0f";

    //Переменные
    protected double currentNormTime;

    protected AbstractFormController prevFormController;
    protected OpData opData;

    public void setOpData(OpData opData){
        this.opData = opData;
    }

    @Override //IOpData
    public OpData getOpData(){
        return opData;
    }

    /**
     * Метод устанавливает/восстанавливает начальные значения полей
     * согласно данным в классе OpData
     */
    public abstract void initViews(OpData opData);

    public abstract void countNorm(OpData opData);

    public abstract void countInitialValues();

    public abstract void fillOpData(OpData opData);

    public abstract String helpText();

    public abstract Image helpImage();

    @FXML
    protected ImageView ivOperation;

    @FXML
    protected Label lblOperationName;

    @FXML
    private VBox vbOperation;

    @FXML @Getter
    private TextField tfNormTime;

    @FXML
    private Label lblNormTimeMeasure;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private ImageView ivHelp;


    public AbstractOpPlate() {
    }

    public void init(AbstractFormController prevFormController, OpData opData, Integer index, String helpTitle) {

        ivOperation.setImage(opData.getOpType().getLogo());

        lblOperationName.setText(opData.getOpType().getOpName().toUpperCase());
        lblOperationName.setStyle("-fx-text-fill: darkblue");

        ivHelp.setOnMouseClicked(e->{
            HelpWindow.create(e, helpTitle, helpText(), helpImage());
        });
        init(prevFormController, opData, index);
    }

    public void init(AbstractFormController prevFormController, OpData opData, Integer index) {
        this.prevFormController = prevFormController;
        this.opData = opData;

        prevFormController.getAddedPlates().add(index, this);
        prevFormController.getAddedOperations().add(index, opData);

        fillOpData(opData);

        initViews(opData);

        ivDeleteOperation.setOnMouseClicked(this::deleteSelectedOperation);

        countNorm(opData);

    }

    public void deleteSelectedOperation(Event e) {
        prevFormController.getAddedPlates().remove(this);
        VBox box = prevFormController.getListViewTechOperations().getSelectionModel().getSelectedItem();
        prevFormController.getListViewTechOperations().getItems().remove(box);
        prevFormController.getAddedOperations().remove(this.getOpData());
        prevFormController.countSumNormTimeByShops();
        //Пересчет площади покрытия при удалении деталей и сборок
        OpData deletingOp = this.getOpData();
        if(deletingOp instanceof OpDetail || deletingOp instanceof OpAssm){
            recountPaintedAssm(MAIN_OP_DATA);
        }
    }

    /**
     * Пересчет всех операций окрашивания сборок
     */
    public static  void recountPaintedAssm(IOpWithOperations opData) {
        List<OpData> ops = opData.getOperations();
        for (OpData op : ops) {
            if (op instanceof OpPaintAssm) {
                OpPaintAssm opPaintAssm = (OpPaintAssm) ((OpPaintAssm) op).getOpType().getNormCounter().count(op);
                PlatePaintAssmController controller = opPaintAssm.getController();
                if (controller != null) {
                    controller.countNorm(opPaintAssm);
                }
            }
            if (op instanceof OpAssm) recountPaintedAssm((IOpWithOperations) op);
        }
    }



    /**
     * Метод возвращает текущее расчитанное
     */
    public double getCurrentNormTime(){
        return currentNormTime;
    }

    /**
     * Метод устанавливает поле с расчитанной нормой в значением требуемой размерности
     */
    public void setTimeMeasurement(){
        double time = currentNormTime;
        String format = DOUBLE_FORMAT;
        String measure = MIN.getMeasure();
        if (CURRENT_MEASURE.equals(ETimeMeasurement.SEC)) {
            time = currentNormTime * MIN_TO_SEC;
            format = INTEGER_FORMAT;
            measure = SEC.getMeasure();
        }
        if (CURRENT_MEASURE.equals(HOUR)) {
            time = currentNormTime * MIN_TO_HOUR;
            format = DOUBLE_FORMAT;
            measure = HOUR.getMeasure();
        }

        if(tfNormTime != null){
            tfNormTime.setText(format(format,time).trim());
            lblNormTimeMeasure.setText(measure);
        }

    }

    /**
     * Прописывает расчитанную норму времени в поле tfNormTime
     * Вызывается при пересчете норм при изменении общего количества деталей в изделии
     * Пр: из FormDetailController, FormAssmController
     */
    public void writeNormTime(OpData opData){

        ENormType type = opData.getNormType();
        double time = 0.0;
        switch (type){
            case NORM_MECHANICAL: time = opData.getMechTime(); break;
            case NORM_PAINTING: time = opData.getPaintTime(); break;
            case NORM_ASSEMBLING: time = opData.getAssmTime(); break;
            case NORM_PACKING: time = opData.getPackTime(); break;
        }


        tfNormTime.setText(format(DOUBLE_FORMAT, time * CURRENT_MEASURE.getRate()));
    }

}
