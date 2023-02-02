package ru.wert.normic.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.TFColoredInteger;
import ru.wert.normic.components.TFNormTime;
import ru.wert.normic.entities.OpAssmNut;
import ru.wert.normic.entities.OpData;
import ru.wert.normic.utils.IntegerParser;

import static ru.wert.normic.entities.settings.AppSettings.*;

public class PlateAssmNutsController extends AbstractOpPlate {

    @FXML
    private ImageView ivOperation;

    @FXML
    private VBox vbOperation;

    @FXML
    private TextField tfNormTime;

    @FXML
    private TextField tfOthers;

    @FXML
    private TextField tfGroundSets;

    @FXML
    private TextField tfVSHGs;

    @FXML
    private TextField tfRivets;

    @FXML
    private Label lblNormTimeMeasure;

    @FXML
    private TextField tfRivetNuts;

    @FXML
    private ImageView ivDeleteOperation;

    @FXML
    private Label lblOperationName;

    @FXML
    private TextField tfScrews;

    private int screws; //Количество винтов
    private int vshgs; //Количество комплектов ВШГ
    private int rivets; //Количество заклепок
    private int rivetNuts; //Количество аклепочных гаек
    private int groundSets; //Количество комплектов заземления с этикеткой
    private int others; //Количество другого крепежа

    @Override //AbstractOpPlate
    public void initViews(OpData data){
        OpAssmNut opData = (OpAssmNut)data;

        new TFNormTime(tfNormTime, formController);
        new TFColoredInteger(tfScrews, this);
        new TFColoredInteger(tfVSHGs, this);
        new TFColoredInteger(tfRivets, this);
        new TFColoredInteger(tfRivetNuts, this);
        new TFColoredInteger(tfGroundSets, this);
        new TFColoredInteger(tfOthers, this);

        lblOperationName.setStyle("-fx-text-fill: saddlebrown");
    }


    @Override//AbstractOpPlate
    public void countNorm(OpData data){
        OpAssmNut opData = (OpAssmNut)data;

        countInitialValues();

        double time;
        time =  screws * SCREWS_SPEED
                + vshgs * VSHGS_SPEED
                + rivets * RIVETS_SPEED * SEC_TO_MIN
                + rivetNuts * RIVET_NUTS_SPEED * SEC_TO_MIN
                + groundSets * GROUND_SETS_SPEED
                + others * OTHERS_SPEED * SEC_TO_MIN;   //мин

        currentNormTime = time;
        collectOpData(opData);
        setTimeMeasurement();
    }


    /**
     * Устанавливает и расчитывает значения, заданные пользователем
     */
    @Override //AbstractOpPlate
    public void countInitialValues() {

        screws = IntegerParser.getValue(tfScrews);
        vshgs = IntegerParser.getValue(tfVSHGs);
        rivets = IntegerParser.getValue(tfRivets);
        rivetNuts = IntegerParser.getValue(tfRivetNuts);
        groundSets = IntegerParser.getValue(tfGroundSets);
        others = IntegerParser.getValue(tfOthers);

    }

    private void collectOpData(OpAssmNut opData){
        opData.setScrews(screws);
        opData.setVshgs(vshgs);
        opData.setRivets(rivets);
        opData.setRivetNuts(rivetNuts);
        opData.setGroundSets(groundSets);
        opData.setOthers(others);

        opData.setAssmTime(currentNormTime);
    }

    @Override//AbstractOpPlate
    public void fillOpData(OpData data){
        OpAssmNut opData = (OpAssmNut)data;

        screws = opData.getScrews();
        tfScrews.setText(String.valueOf(screws));

        vshgs = opData.getVshgs();
        tfVSHGs.setText(String.valueOf(vshgs));

        rivets = opData.getRivets();
        tfRivets.setText(String.valueOf(rivets));

        rivetNuts = opData.getRivetNuts();
        tfRivetNuts.setText(String.valueOf(rivetNuts));

        groundSets = opData.getGroundSets();
        tfGroundSets.setText(String.valueOf(groundSets));

        others = opData.getOthers();
        tfOthers.setText(String.valueOf(others));

    }


}
