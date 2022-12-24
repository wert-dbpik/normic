package ru.wert.normic.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ru.wert.normic.entities.OpAssm;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.entities.OpDetail;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.ETimeMeasurement;
import ru.wert.normic.controllers.forms.AbstractFormController;
import ru.wert.normic.interfaces.IOpPlate;
import ru.wert.normic.interfaces.IOpWithOperations;
import ru.wert.normic.menus.MenuPlate;

import java.util.Arrays;
import java.util.List;

import static ru.wert.normic.AppStatics.MEASURE;
import static ru.wert.normic.enums.EOpType.*;


/**
 * Класс наследует интерфейс IOpPlate
 * Изначальный расчет нормы времени производится в минутах
 * Конвертация в секунды происходит в методе setTimeMeasurement()
 */
public abstract class AbstractOpPlate implements IOpPlate {

    public static OpData bufferedOpData = null; //Операция, хранимая для копирования/вырезания
    public static AbstractFormController whereFromController; //Операция в которой находится bufferedOpData
    public static boolean deleteWhenPaste; //Флаг удаления bufferedOpData после вставки
    private final List<EOpType> restrictedForDetail =  //Перечень операций, которые нельзя добавить в Деталь
            Arrays.asList(DETAIL, ASSM, ASSM_CUTTINGS, ASSM_NUTS, ASSM_NODES, PAINT_ASSM, LEVELING_SEALER);
    private final List<EOpType> restrictedForAssm = //Перечень операций, которые нельзя добавить в Сборку
            Arrays.asList(CUTTING, BENDING, PAINTING);

    // КОНСТАНТЫ
    public static final double MM_TO_M = 0.001; //перевод мм в метры
    public static final double MM2_TO_M2 = 0.000001; //перевод мм квадратных в квадратные метры
    public static final double MM3_TO_M3 = 0.000000001; //перевод мм квадратных в квадратные метры
    public static final double MIN_TO_SEC = 60; //перевод минут в секунды
    public static final double SEC_TO_MIN = 1.0/60; //перевод минут в секунды
    public static final double RO = 0.00000785; //плотность стали кг/м3
    public static final String DOUBLE_FORMAT = "%5.3f";
    public static final String INTEGER_FORMAT = "%10.0f";

    //Переменные
    protected double currentNormTime;

    protected AbstractFormController formController;
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

    public abstract void countInitialValues();


    @FXML
    private ImageView ivContextMenu;

    @FXML
    private VBox vbOperation;

    @FXML @Getter
    private TextField tfNormTime;

    @FXML
    private Label lblNormTimeMeasure;

    @FXML
    private ImageView ivDeleteOperation;

    public AbstractOpPlate() {
    }

    public void init(AbstractFormController formController, OpData opData, Integer index) {
        this.formController = formController;
        this.opData = opData;

        formController.getAddedPlates().add(index, this);
        formController.getAddedOperations().add(index, opData);

        fillOpData(opData);

        initViews(opData);

        ivContextMenu.setOnMouseClicked(e->{
            if(e.getButton().equals(MouseButton.PRIMARY)){
                new MenuPlate().create(this).show(
                        ivContextMenu,
                        Side.LEFT,
                        0.0,
                        24.0);
            }
        });

        ivDeleteOperation.setOnMouseClicked(this::deleteSelectedOperation);

        countNorm(opData);

    }

    public void deleteSelectedOperation(Event e) {
        formController.getAddedPlates().remove(this);
        VBox box = formController.getListViewTechOperations().getSelectionModel().getSelectedItem();
        formController.getListViewTechOperations().getItems().remove(box);
        formController.getAddedOperations().remove(this.getOpData());
        formController.countSumNormTimeByShops();
    }

    public void cutOperation(Event e){
        int selectedIndex = formController.getListViewTechOperations().getSelectionModel().getSelectedIndex();
        bufferedOpData = formController.getAddedOperations().get(selectedIndex);
        whereFromController = formController;
        deleteWhenPaste = true;
    }

    public void copyOperation(Event e){
        int selectedIndex = formController.getListViewTechOperations().getSelectionModel().getSelectedIndex();
        bufferedOpData = formController.getAddedOperations().get(selectedIndex);
        whereFromController = formController;
        deleteWhenPaste = false;
    }

    public boolean isPastePossible(Event e){
        if(bufferedOpData == null) return false;

        int selectedIndex = formController.getListViewTechOperations().getSelectionModel().getSelectedIndex();
        OpData selectedOpData = formController.getAddedOperations().get(selectedIndex);
        if(selectedOpData.equals(bufferedOpData)) return false;
        if(selectedOpData instanceof OpDetail) {
            return !restrictedForDetail.contains(bufferedOpData.getOpType());
        }
        else if(selectedOpData instanceof OpAssm) {
            return !restrictedForAssm.contains(bufferedOpData.getOpType());
        }
        return true;
    }

    public void pasteOperation(Event e) {
        int selectedIndex = formController.getListViewTechOperations().getSelectionModel().getSelectedIndex();
        OpData selectedOpData = formController.getAddedOperations().get(selectedIndex);
        //Сначала удаляем bufferedOpData из whereFromController
        if (deleteWhenPaste) {
            whereFromController.deleteOperation(bufferedOpData);
        }
        //Потом вставляем bufferedOpData в selectedOpData
        ((IOpWithOperations) selectedOpData).getOperations().add(bufferedOpData);

        bufferedOpData = null;
        whereFromController = null;

        formController.fillOpData();
        formController.countSumNormTimeByShops();
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
        String measure = "мин";
        if (MEASURE.getValue().equals(ETimeMeasurement.SEC)) {
            time = currentNormTime * MIN_TO_SEC;
            format = INTEGER_FORMAT;
            measure = "сек";
        }

        if(tfNormTime != null){
            tfNormTime.setText(String.format(format,time));
            lblNormTimeMeasure.setText(measure);
        }

    }
}
