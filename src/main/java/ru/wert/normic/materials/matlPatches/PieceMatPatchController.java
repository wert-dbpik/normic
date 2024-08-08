package ru.wert.normic.materials.matlPatches;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers._forms.FormDetailController;
import ru.wert.normic.dataBaseEntities.db_connection.material.Material;
import ru.wert.normic.dataBaseEntities.ops.single.OpDetail;
import ru.wert.normic.enums.EPieceMeasurement;

import java.util.List;

import static ru.wert.normic.AppStatics.roundTo001;
import static ru.wert.normic.controllers.AbstractOpPlate.*;

/**
 * Класс описывающий поведение панели листового материала
 */
public class PieceMatPatchController extends AbstractMatPatchController {

    @FXML@Getter
    private TextField tfA;

    @FXML@Getter
    private TextField tfB;

    @FXML@Getter
    private TextField tfC;

    @FXML@Getter
    private TextField tfWasteRatio;

    @FXML@Getter
    private TextField tfWeight;

    @FXML@Getter
    private TextField tfPieceOutlay;

    @FXML@Getter
    private Label lblMeasure;

    @Override
    public TextField getTfCoat() {
        return null; //Не используется
    }

    @Override
    public void init(OpDetail opData, FormDetailController detailController, List<AbstractOpPlate> addedPlates) {
        super.init(opData, detailController, addedPlates);

        getTfC().textProperty().addListener((observable, oldValue, newValue) -> {
            countWeightAndArea();
            for(AbstractOpPlate nc : addedPlates){
                nc.countNorm(nc.getOpData());
            }
        });

        Material material = detailController.getCmbxMaterial().getValue();
        EPieceMeasurement measure = EPieceMeasurement.values()[(int)material.getParamX()];
        opData.setMeasurement(measure);

        tfC.setText(String.valueOf(opData.getParamC()));

        if(measure.equals(EPieceMeasurement.PIECE)){
            tfA.setDisable(false);
            tfB.setDisable(true);
            tfC.setDisable(true);
            tfWasteRatio.setDisable(true);
            tfWeight.setDisable(true);

            tfPieceOutlay.setText("1");
            opData.setPieceOutlay(1.0);
        }
        if(measure.equals(EPieceMeasurement.METER)){
            tfA.setDisable(false);
            tfB.setDisable(true);
            tfC.setDisable(true);
        }
        if(measure.equals(EPieceMeasurement.SQUARE_METER)){
            tfA.setDisable(false);
            tfB.setDisable(false);
            tfC.setDisable(true);
        }
        if(measure.equals(EPieceMeasurement.CUBE_METER)){
            tfA.setDisable(false);
            tfB.setDisable(false);
            tfC.setDisable(false);
        }

        opData.setWasteRatio(1.0);

    }

    @Override
    public void fillPatchOpData() {

        paramA = opData.getParamA();
        getTfA().setText(String.valueOf(paramA));

        paramB = opData.getParamB();
        getTfB().setText(String.valueOf(paramB));

        paramC = opData.getParamC();
        getTfC().setText(String.valueOf(paramC));

        wasteRatio = opData.getWasteRatio();
        getTfWasteRatio().setText(String.valueOf(wasteRatio));

        tfWeight.setText(String.valueOf(opData.getWeight()));
        lblMeasure.setText(opData.getMeasurement().getMeasureName());

        tfPieceOutlay.setText(String.valueOf(opData.getPieceOutlay()));

    }


    @Override
    public void countWeightAndArea() {
        double weight = 0.0;
        double pieceOutlay = 0.0;
        EPieceMeasurement measure = opData.getMeasurement();
        paramA = tfA.getText().equals("") ? 0 : Integer.parseInt(tfA.getText().trim());
        paramB = tfB.getText().equals("") ? 0 : Integer.parseInt(tfB.getText().trim());
        paramC = tfC.getText().equals("") ? 0 : Integer.parseInt(tfC.getText().trim());

        if(measure.equals(EPieceMeasurement.PIECE)){
            weight = 1;
        } else if (measure.equals(EPieceMeasurement.METER)){
            weight = paramA * MM_TO_M;
        } else if(measure.equals(EPieceMeasurement.SQUARE_METER)){
            weight = paramA * paramB * MM2_TO_M2;
        } else if(measure.equals(EPieceMeasurement.CUBE_METER)){
            weight = paramA * paramB * paramC * MM3_TO_M3;
        }

        Material material = detailController.getCmbxMaterial().getValue();
        pieceOutlay = weight / material.getParamS();



        weight = measure.equals(EPieceMeasurement.PIECE)  ? weight : weight * wasteRatio;

        tfWeight.setText(String.format(DOUBLE_FORMAT, weight));
        lblMeasure.setText(measure.getMeasureName());
        tfPieceOutlay.setText(String.format(DOUBLE_FORMAT, pieceOutlay));

        opData.setParamC(paramC);
        opData.setWeight(roundTo001(weight));
        opData.setPieceOutlay(pieceOutlay);

    }
}
