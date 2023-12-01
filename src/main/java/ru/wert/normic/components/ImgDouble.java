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
    private final Image imageON;

    /**
     * Начальное состояние кнопки OFF, stateProperty = false;
     */
    public ImgDouble(ImageView image, Image imageOFF, Image imageON) {
        this.image = image;
        this.imageOFF = imageOFF;
        this.imageON = imageON;

        initImageToStateOFF();
    }

    void initImageToStateOFF(){
        image.setImage(imageOFF);
    }

    void initImageToStateON(){
        image.setImage(imageON);
    }

}
