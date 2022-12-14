package ru.wert.normic.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MoverController {

    @FXML
    HBox hbMover;

    AbstractOpPlate plate;
    VBox vbPlate;

    protected static double dragOffsetX;
    protected static double dragOffsetY;

    public void init(AbstractOpPlate plate, VBox vbPlate){
        this.plate = plate;
        this.vbPlate = vbPlate;

//        hbMover.setOnMousePressed(e->{
//            Stage window = (Stage) vbPlate.getScene().getWindow();
//            dragOffsetX = e.getScreenX() - window.getX();
//            dragOffsetY = e.getScreenY() - window.getY();
//        });
//
//        hbMover.setOnMouseDragged(e -> {
//            Stage window = (Stage) vbPlate.getScene().getWindow();
//            window.setX(e.getScreenX() - dragOffsetX);
//            window.setY(e.getScreenY() - dragOffsetY);
//        });

    }
}
