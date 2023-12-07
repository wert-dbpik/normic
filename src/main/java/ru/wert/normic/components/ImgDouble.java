package ru.wert.normic.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;

public class ImgDouble extends ImageView{

    private final BooleanProperty stateProperty = new SimpleBooleanProperty();
    public BooleanProperty getStateProperty(){return stateProperty;}

    private final ImageView imageView;
    private final Image imageOFF;
    private final Image imageON;

    /**
     * Начальное состояние кнопки OFF, stateProperty = false;
     */
    public ImgDouble(ImageView imageView, Image imageOFF, Image imageON, int size) {
        this.imageView = imageView;
        this.imageOFF = imageOFF;
        this.imageON = imageON;

        imageView.setFitHeight(size);
        imageView.setFitWidth(size);
        imageView.resize(size, size);

        initButtonTosStateOFF();

        stateProperty.addListener((observable, oldValue, newValue) -> {
            switchButton(newValue);
        });

    }

    void initButtonTosStateOFF(){
        imageView.setImage(imageOFF);
    }

    void initButtonTosStateON(){
        imageView.setImage(imageON);
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
