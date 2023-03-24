package ru.wert.normic.controllers.assembling;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.ops.opAssembling.OpAssmNode;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

/**
 * СБОРКА ОТДЕЛЬНЫХ УЗЛОВ
 */
public class PlateAssmNodesController extends AbstractOpPlate {

    @FXML
    private TextField tfMirrors;

    @FXML
    private TextField tfDoubleLocks;

    @FXML
    private TextField tfConnectionBoxes;

    @FXML
    private TextField tfPostLocks;

    @FXML
    private TextField tfDetectors;

    @FXML
    private TextField tfNormTime;

    @FXML
    private Label lblOperationName;

    private int postLocks; //Количество почтовых замков
    private int doubleLocks; //Количество замков с рычагами
    private int mirrors; //Количество стекол
    private int detectors; //Количество извещателей ИО-102
    private int connectionBoxes; //Количество еоробок соединительных КС-4

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpAssmNode opData = (OpAssmNode)data;
        ivOperation.setImage(EOpType.ASSM_NODES.getLogo());

        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfPostLocks, this);
        new TFIntegerColored(tfDoubleLocks, this);
        new TFIntegerColored(tfMirrors, this);
        new TFIntegerColored(tfDetectors, this);
        new TFIntegerColored(tfConnectionBoxes, this);

        lblOperationName.setText(EOpType.ASSM_NODES.getOpName().toUpperCase());
        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpAssmNode opData = (OpAssmNode)data;

        countInitialValues();

        double time;
        time =  postLocks * POST_LOCKS_SPEED
                + doubleLocks * DOUBLE_LOCKS_SPEED
                + mirrors * GLASS_SPEED
                + detectors * DETECTORS_SPEED
                + connectionBoxes * CONNECTION_BOXES_SPEED;   //мин

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и рассчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        postLocks = IntegerParser.getValue(tfPostLocks);
        doubleLocks = IntegerParser.getValue(tfDoubleLocks);
        mirrors = IntegerParser.getValue(tfMirrors);
        detectors = IntegerParser.getValue(tfDetectors);
        connectionBoxes = IntegerParser.getValue(tfConnectionBoxes);
    }


    private void collectOpData(OpAssmNode opData){
        opData.setPostLocks(postLocks);
        opData.setDoubleLocks(doubleLocks);
        opData.setMirrors(mirrors);
        opData.setDetectors(detectors);
        opData.setConnectionBoxes(connectionBoxes);

        opData.setAssmTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpAssmNode opData = (OpAssmNode)data;

        postLocks = opData.getPostLocks();
        tfPostLocks.setText(String.valueOf(postLocks));

        doubleLocks = opData.getDoubleLocks();
        tfDoubleLocks.setText(String.valueOf(doubleLocks));

        mirrors = opData.getMirrors();
        tfMirrors.setText(String.valueOf(mirrors));

        detectors = opData.getDetectors();
        tfDetectors.setText(String.valueOf(detectors));

        connectionBoxes = opData.getConnectionBoxes();
        tfConnectionBoxes.setText(String.valueOf(connectionBoxes));

    }

    @Override
    public String helpText() {
        return String.format("ЗАМОК ПОЧТОВЫЙ - указывается суммарное количество в сборке.\n" +
                "\tОдин замок устанавливается за %s мин с регулировкой.\n" +
                "\n" +
                "ЗАМОК С РЫЧАГАМИ - указывается суммарное количество в сборке (Меттэм, Месан).\n" +
                "\tОдин замок устанавливается за %s мин с регулировкой.\n" +
                "\n" +
                "СТЕКЛО В ДВЕРЬ - указывается суммарное количество в сборке.\n" +
                "\tСтекло устанваливается на полиуретан за %s мин.\n" +
                "\n" +
                "ИЗВЕЩАТЕЛЬ ИО-102 - указывается суммарное количество в сборке.\n" +
                "\tОдин извещатель устанавливается за %s мин.\n" +
                "\n" +
                "КОРОБКА СОЕД ТИПА КС-4 - указывается суммарное количество в сборке.\n" +
                "\tОдна коробка с калибровкой резьбы устанавливается за %s мин.\n",

                POST_LOCKS_SPEED, DOUBLE_LOCKS_SPEED, GLASS_SPEED, DETECTORS_SPEED, CONNECTION_BOXES_SPEED);
    }

    @Override
    public Image helpImage() {
        return null;
    }
}
