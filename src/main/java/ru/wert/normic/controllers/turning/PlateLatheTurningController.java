package ru.wert.normic.controllers.turning;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.db_connection.material.Material;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opTurning.OpLatheTurning;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

/**
 * ТОЧЕНИЕ ИЛИ РАСТАЧИВАНИЕ НА ТОКАРНОМ СТАНКЕ
 */
public class PlateLatheTurningController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;

    @FXML
    private TextField tfTurningLength;

    @FXML
    private TextField tfNumOfPassings;

    @FXML
    private TextField tfNormTime;

    private OpLatheTurning opData;

    private String initStyle;

    private Material material; //материал заготовки
    private int paramA; //Длина заготовки
    private int length; //Длина точения
    private int passages; //Число токарных проходов


    @Override //AbstractOpPlate
    public void initViews(OpData data){
        initStyle = tfTurningLength.getStyle(); //Сохраняем исходный стиль

        new TFIntegerColored(tfTurningLength, this);
        new TFIntegerColored(tfNumOfPassings, this);

        new TFNormTime(tfNormTime, prevFormController);

    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpLatheTurning) data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        material = ((FormDetailController) prevFormController).getCmbxMaterial().getValue();

        paramA = ((FormDetailController) prevFormController).getMatPatchController().getParamA();
        length = IntegerParser.getValue(tfTurningLength);
        if(length > paramA)
            tfTurningLength.setStyle("-fx-border-color: #FF5555");
        else
            tfTurningLength.setStyle(initStyle);

        passages = IntegerParser.getValue(tfNumOfPassings);

        collectOpData();
    }

    private void collectOpData(){
        opData.setMaterial(material);
        opData.setPassages(passages);
        opData.setLength(length);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpLatheTurning opData = (OpLatheTurning)data;

        length = opData.getLength() == 0 ?
                Integer.parseInt(((FormDetailController) prevFormController).getMatPatchController().getTfA().getText()) :
                opData.getLength();
        tfTurningLength.setText(String.valueOf(length));

        passages = opData.getPassages();
        tfNumOfPassings.setText(String.valueOf(passages));

    }

    @Override
    public String helpText() {
        return "Расчет ведется как для продольного так и для поперечного точения.\n" +
                "Припуск на проход берется до 4 мм\n\n" +
                "\tL точения - длина прохода резца за один проход, мм;\n" +
                "\tN проходов - количество проходов, шт. \n\n" +
                "Норма времени на точение вычисляется по формуле:\n\n" +
                "\t\t\tT точения. = T прох x N проходов, мин,\n" +
                "где\n" +
                "\tT прох - время точения одного прохода берется из таблиц стандартных норм\n" +
                "\tи зависит от диаметра обработки, мин";
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
