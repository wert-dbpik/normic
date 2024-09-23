package ru.wert.normic.controllers.welding;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import ru.wert.normic.components.*;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.TotalCounter;
import ru.wert.normic.entities.db_connection.simpleOperations.SimpleOperation;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.opWelding.OpWeldDifficulty;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.enums.EPieceMeasurement;
import ru.wert.normic.enums.EWeldDifficulty;
import ru.wert.normic.utils.DoubleParser;
import ru.wert.normic.utils.IntegerParser;

import static java.lang.String.format;
import static ru.wert.normic.AppStatics.MAIN_OP_DATA;

/**
 * ПРОЧИЕ ПРОСТЫЕ ОПЕРАЦИИ
 */
public class PlateWeldDifficultyController extends AbstractOpPlate {

    @FXML
    private Label lblOperationName;
    
    @FXML
    private ComboBox<EWeldDifficulty> bxDifficulty;

    @FXML
    private TextField tfNormTime;

    
    private OpWeldDifficulty opData;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        opData = (OpWeldDifficulty) data;
        opData.setPlate(this);

        new TFNormTime(tfNormTime, prevFormController);
        new BXWeldDifficulty().create(bxDifficulty, opData.getDifficulty(), this);
    }

    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        opData = (OpWeldDifficulty) data;

        countInitialValues();

        currentNormTime = opData.getOpType().getNormCounter().count(data).getMechTime();//результат в минутах

        new TotalCounter().recountNormTimes(MAIN_OP_DATA, 1);
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        collectOpData();
    }


    private void collectOpData(){
        opData.setDifficulty(bxDifficulty.getValue());
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpWeldDifficulty opData = (OpWeldDifficulty)data;

        bxDifficulty.setValue(opData.getDifficulty());

    }

    @Override
    public String helpText() {
        return
                "Здесь добавляется врямя на непрерывную сварку на всю сборку\n" +
                        "в зависимости от ее сложности\n" +
                format("\t\tСредняя сложность %d минут\n", EWeldDifficulty.MIDDLE.getTime()) +
                format("\t\tВысокая сложность %d минут", EWeldDifficulty.HEIGHT.getTime());

    }

    @Override
    public Image helpImage() {
        return null;
    }
}
