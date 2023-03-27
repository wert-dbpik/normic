package ru.wert.normic.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class BtnDouble{


    private BooleanProperty stateProperty = new SimpleBooleanProperty();
    public BooleanProperty getStateProperty(){return stateProperty;}

    private final Button button;
    private final ImageView imageOFF;
    private final String textOFF;
    private final ImageView imageON;
    private final String textON;

    /**
     * Начальное состояние кнопки OFF, stateProperty = false;
     */
    public BtnDouble(Button button, Image imageOFF, String textOFF, Image imageON, String textON) {
        this.button = button;
        this.imageOFF = new ImageView(imageOFF);
        this.textOFF = textOFF;
        this.imageON = new ImageView(imageON);
        this.textON = textON;

        initButtonTosStateOFF();

        stateProperty.addListener((observable, oldValue, newValue) -> {
            switchButton(newValue);
        });

        //Кнопку нажали
        button.setOnMousePressed(e->{
            switchButton(!stateProperty.get());
        });
    }

    void initButtonTosStateOFF(){
        button.setGraphic(imageOFF);
        button.setTooltip(new Tooltip(textOFF));
    }

    void initButtonTosStateON(){
        button.setGraphic(imageON);
        button.setTooltip(new Tooltip(textON));
    }

    private void switchButton(boolean state) {
        if (state) {
            initButtonTosStateON();
        } else {
            initButtonTosStateOFF();
        }
        stateProperty.set(state);
    }

}
