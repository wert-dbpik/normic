package ru.wert.normic.controllers.list;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.BXBendingTool;
import ru.wert.normic.components.TFIntegerColored;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.entities.OpBending;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.enums.EBendingTool;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.BENDING_SERVICE_RATIO;
import static ru.wert.normic.entities.settings.AppSettings.BENDING_SPEED;

/**
 * ГИБКА ЛИСТА НА ЛИСТОГИБЕ
 */
public class PlateBendController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private TextField tfBends;

    @FXML
    private TextField tfMen;

    @FXML
    private ComboBox<EBendingTool> cmbxBendingTool;

    @FXML
    private TextField tfNormTime;

    private int bends;
    private int men;
    private double toolRatio;

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpBending opData = (OpBending)data;

        new BXBendingTool().create(cmbxBendingTool, opData.getTool(), this);
        new TFNormTime(tfNormTime, formController);
        new TFIntegerColored(tfBends, this);
        new TFIntegerColored(tfMen, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");

//        new CmBx(cmbxBendingTool, this);

    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpBending opData = (OpBending)data;

        countInitialValues();

        double time;
        time =  bends * BENDING_SPEED * toolRatio * men  //мин
                * BENDING_SERVICE_RATIO;

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }

    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public  void countInitialValues() {

        bends = IntegerParser.getValue(tfBends);
        men = IntegerParser.getValue(tfMen);
        toolRatio = cmbxBendingTool.getValue().getToolRatio();
    }

    private void collectOpData(OpBending opData){
        opData.setBends(bends);
        opData.setMen(men);
        opData.setTool(cmbxBendingTool.getValue());

        opData.setMechTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpBending opData = (OpBending)data;

        bends = opData.getBends();
        tfBends.setText(String.valueOf(bends));

        men = opData.getMen();
        tfMen.setText(String.valueOf(men));

        if(opData.getTool() != null){
            toolRatio = opData.getTool().getToolRatio();
            cmbxBendingTool.setValue(opData.getTool());
        }
    }
}