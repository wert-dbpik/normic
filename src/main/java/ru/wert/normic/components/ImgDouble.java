package ru.wert.normic.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;


public class ImgDouble extends ImageView{


    private BooleanProperty stateProperty = new SimpleBooleanProperty();
    public BooleanProperty getStateProperty(){return stateProperty;}


    @Setter private ImageView image;
    @Setter private Image imageOFF;
    @Setter private Image imageON;

    /**
     * Начальное состояние кнопки OFF, stateProperty = false;
     */
    public ImgDouble() {

//        initImageToStateOFF();
    }

    void initImageToStateOFF(){
        image.setImage(imageOFF);
    }

    void initImageToStateON(){
        image.setImage(imageON);
    }

}
