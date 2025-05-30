package ru.wert.normic.controllers.packing;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import ru.wert.normic.components.RadBtn;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormPackController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opPack.OpPackInBubbleWrap;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;
import static ru.wert.normic.settings.NormConstants.*;

/**
 * УПАКОВКА ВЫСОКОГО ШКАФА
 */
public class PlatePackInBubbleController extends AbstractOpPlate {

    @FXML
    private RadioButton rbByHeight;

    @FXML
    private RadioButton rbByDepth;

    @FXML
    private RadioButton rbByWidth;

    @FXML
    private TextField tfBubbleWrap;

    @FXML
    private TextField tfDuctTape;

    @FXML
    private TextField tfNormTime;

    private int width, depth, height;
    private ToggleGroup toggleGroup; //Накручивание по
    private int selectedRadioButton; //выделенный
    private Double bubbleWrap;
    private Double ductTape;

    private OpPackInBubbleWrap opData;

    @FXML
    void initialize(){
        toggleGroup = new ToggleGroup();
        rbByHeight.setToggleGroup(toggleGroup);
        rbByDepth.setToggleGroup(toggleGroup);
        rbByWidth.setToggleGroup(toggleGroup);

    }


    @Override //AbstractOpPlate
    public void initViews(OpData data){

        new TFNormTime(tfNormTime, prevFormController);
        new RadBtn(rbByHeight, this);
        new RadBtn(rbByDepth, this);
        new RadBtn(rbByWidth, this);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpPackInBubbleWrap) data;

        countInitialValues();

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);

        tfBubbleWrap.setText(DECIMAL_FORMAT.format(opData.getBubbleWrap()));
        tfDuctTape.setText(DECIMAL_FORMAT.format(opData.getDuctTape()));
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {
        selectedRadioButton = toggleGroup.getToggles().indexOf(toggleGroup.getSelectedToggle());

        width = ((FormPackController) prevFormController).getWidth();
        depth = ((FormPackController) prevFormController).getDepth();
        height = ((FormPackController) prevFormController).getHeight();

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
        OpPackInBubbleWrap opData = (OpPackInBubbleWrap)data;

        selectedRadioButton = opData.getSelectedRadioButton();
        toggleGroup.selectToggle(toggleGroup.getToggles().get(selectedRadioButton));

    }

    @Override
    public String helpText() {
        return String.format("Наматывание по высоте - вокруг вертикальной оси, аналогично \n" +
                        "по ширине - вокруг горизонтальной оси параллельной фронтальной стенке \n" +
                        "и по глубине - параллельно боковой стенке.\n\n" +
                        "Расход пузырьковой пленки рассчитывается по формуле:\n\n" +
                        "\t\t\tS пуз = P * H * 1.1, м.кв.,\n" +
                        "где\n" +
                        "\tP - периметр наматываемой поверхности, м;\n" +
                        "\tH (высота) - размер, перпиндикулярный плосткости периметра, м;\n\n" +
                        "Для крепления пузырьковой пленки используется скотч, расход:\n\n" +
                        "\t\t\tL скотч = H * 2 / L рулон (2 высоты), шт,\n" +
                        "где\n" +
                        "\tL рулон = %s - длина скотча в рулоне, м\n\n" +
                        "Норма времени упаковки рассчитывается по формуле:\n\n" +
                        "\t\t\tT упак = Т пз + S пуз * V упак, мин\n" +
                        "где\n" +
                        "\tТ пз = %s - ПЗ время, мин;\n" +
                        "\tV упак = %s - скорость оборачивания пузырьковой пленки, мин/м.кв.",

                DUCT_TAPE_LENGTH, BUBBLE_CUT_AND_DUCT, BUBBLE_HAND_WINDING);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
