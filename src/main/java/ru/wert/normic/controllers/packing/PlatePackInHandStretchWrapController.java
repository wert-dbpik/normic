package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import ru.wert.normic.components.RadBtn;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.controllers.packing.counters.OpPackInBubbleWrapCounter;
import ru.wert.normic.controllers.packing.counters.OpPackInHandStretchWrapCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInBubbleWrap;
import ru.wert.normic.entities.ops.opPack.OpPackInHandStretchWrap;
import ru.wert.normic.enums.EWinding;

import static ru.wert.normic.settings.NormConstants.*;

/**
 * УПАКОВКА РУЧНОЙ СТРЕЙЧ-ПЛЕНКОЙ
 */
public class PlatePackInHandStretchWrapController extends AbstractOpPlate {

    @FXML
    private RadioButton rbByHeight;

    @FXML
    private RadioButton rbByDepth;

    @FXML
    private RadioButton rbByWidth;

    @FXML
    private TextField tfStretchWrap;

    @FXML
    private TextField tfDuctTape;

    @FXML@Getter
    private ImageView ivHelp;

    private OpPackInHandStretchWrap opData;

    private int width, depth, height;
    private ToggleGroup toggleGroup; //Накручивание по
    private int selectedRadioButton; //выделенный


    @FXML
    void initialize(){
        toggleGroup = new ToggleGroup();
        rbByHeight.setToggleGroup(toggleGroup);
        rbByDepth.setToggleGroup(toggleGroup);
        rbByWidth.setToggleGroup(toggleGroup);
    }


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        new RadBtn(rbByHeight, this);
        new RadBtn(rbByDepth, this);
        new RadBtn(rbByWidth, this);

        getTfNormTime().textProperty().addListener((observable, oldValue, newValue) -> formController.countSumNormTimeByShops());

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpPackInHandStretchWrap) data;

        countInitialValues();

        currentNormTime = OpPackInHandStretchWrapCounter.count((OpPackInHandStretchWrap) data).getPackTime();//результат в минутах
        tfStretchWrap.setText(DECIMAL_FORMAT.format(opData.getStretchHandWrap()));
        tfDuctTape.setText(DECIMAL_FORMAT.format(opData.getDuctTape()));

        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        selectedRadioButton = toggleGroup.getToggles().indexOf(toggleGroup.getSelectedToggle());

        width = ((FormPackController)formController).getWidth();
        depth = ((FormPackController)formController).getDepth();
        height = ((FormPackController)formController).getHeight();

        collectOpData();

    }



    private void collectOpData(){
        opData.setHeight(height);
        opData.setWidth(width);
        opData.setDepth(depth);

        opData.setSelectedRadioButton(selectedRadioButton);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpPackInHandStretchWrap opData = (OpPackInHandStretchWrap)data;

        selectedRadioButton = opData.getSelectedRadioButton();
        toggleGroup.selectToggle(toggleGroup.getToggles().get(selectedRadioButton));
    }

    @Override
    public String helpText() {
        return String.format("Наматывание по высоте - вокруг вертикальной оси, аналогично \n" +
                        "по ширине - вокруг горизонтальной оси параллельной фронтальной стенке \n" +
                        "и по глубине - параллельно боковой стенке.\n\n" +
                        "Расход ручной стрейч пленки рассчитывается по формуле:\n\n" +
                        "\t\t\tL стрейч = P * H / 0.3 * 2, м (в 2 слоя),\n" +
                        "где\n" +
                        "\tP - периметр наматываемой поверхности, м;\n" +
                        "\tH (высота) - размер, перпиндикулярный плосткости периметра, м;\n" +
                        "\t0.3 - нахлест, м;\n\n" +
                        "Для крепления пленки используется скотч, расход:\n\n" +
                        "\t\t\tL скотч = H / L рулон (1 высота), шт,\n" +
                        "где\n" +
                        "\tL рулон = %s - длина скотча в рулоне, м\n\n" +
                        "Норма времени упаковки рассчитывается по формуле:\n\n" +
                        "\t\t\tT упак = L стрейч * V упак, мин\n" +
                        "где\n" +
                        "\tV упак = %s - скорость оборачивания пленки, мин/м.",

                DUCT_TAPE_LENGTH, STRETCH_HAND_WINDING);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
