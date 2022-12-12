package ru.wert.normic;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.controllers.forms.FormDetailController;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.interfaces.IFormController;
import ru.wert.normic.interfaces.IOpPlate;

import static ru.wert.normic.AppStatics.MEASURE;


/**
 * Класс наследует интерфейс IOpPlate
 * Изначальный расчет нормы времени производится в минутах
 * Конвертация в секунды происходит в методе setTimeMeasurement()
 */
public abstract class AbstractOpPlate implements IOpPlate {

    // КОНСТАНТЫ
    public static final double MM_TO_M = 0.001; //перевод мм в метры
    public static final double MM2_TO_M2 = 0.000001; //перевод мм квадратных в квадратные метры
    public static final double MM3_TO_M3 = 0.000000001; //перевод мм квадратных в квадратные метры
    public static final double MIN_TO_SEC = 60; //перевод минут в секунды
    public static final double SEC_TO_MIN = 1.0/60; //перевод минут в секунды
    public static final double RO = 0.00000785; //плотность стали кг/м3
    public static final String doubleFormat = "%5.3f";
    public static final String integerFormat = "%10.0f";

    //Переменные
    protected double currentNormTime;

    protected IFormController formController;
//    protected FormDetailController detailController;
    protected OpData opData;

    public void setOpData(OpDetail opData){
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
    public abstract void fillOpData(OpData opData);

    public abstract void initViews(OpData opData);

    public abstract void countNorm(OpData opData);

    @FXML @Getter
    private TextField tfNormTime;

    @FXML
    private Label lblNormTimeMeasure;

    @FXML
    private ImageView ivDeleteOperation;

    public void init(IFormController formController, OpData opData) {
        this.formController = formController;
        this.opData = opData;

        formController.getAddedPlates().add(this);
        formController.getAddedOperations().add(opData);

        fillOpData(opData); //Должен стоять до навешивагия слушателей на TextField

        initViews(opData);

        ivDeleteOperation.setOnMouseClicked(e->{
            formController.getAddedPlates().remove(this);
            VBox box = formController.getListViewTechOperations().getSelectionModel().getSelectedItem();
            formController.getListViewTechOperations().getItems().remove(box);
            formController.getAddedOperations().remove(this.getOpData());
            formController.countSumNormTimeByShops();
        });

        countNorm(opData);

    }

    public AbstractOpPlate() {
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
        String format = doubleFormat;
        String measure = "мин";
        if (MEASURE.getValue().equals(ETimeMeasurement.SEC)) {
            time = currentNormTime * MIN_TO_SEC;
            format = integerFormat;
            measure = "сек";
        }

        if(tfNormTime != null){
            tfNormTime.setText(String.format(format,time));
            lblNormTimeMeasure.setText(measure);
        }

    }
}
