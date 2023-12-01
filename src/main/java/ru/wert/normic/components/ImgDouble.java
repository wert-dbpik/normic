package ru.wert.normic.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ImgDouble {


    private BooleanProperty stateProperty = new SimpleBooleanProperty();
    public BooleanProperty getStateProperty(){return stateProperty;}

    private final ImageView image;
    private final Image imageOFF;
    private final String textOFF;
    private final Image imageON;
    private final String textON;

    /**
     * Начальное состояние кнопки OFF, stateProperty = false;
     */
    public ImgDouble(ImageView image, Image imageOFF, String textOFF, Image imageON, String textON) {
        this.image = image;
        this.imageOFF = imageOFF;
        this.textOFF = textOFF;
        this.imageON = imageON;
        this.textON = textON;

        initImageToStateOFF();

        stateProperty.addListener((observable, oldValue, newValue) -> {
            switchImage(newValue);
        });
    }

    void initImageToStateOFF(){
        image.setImage(imageOFF);
    }

    void initImageToStateON(){
        image.setImage(imageON);
    }

    private void switchImage(boolean state) {
        if (state) {
            initImageToStateON();
        } else {
            initImageToStateOFF();
        }
        stateProperty.set(state);
    }

}
