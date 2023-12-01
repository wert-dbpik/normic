package ru.wert.normic.components;

import javafx.beans.property.BooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImgDone extends ImgDouble{
    public static final Image imgDone;
    public static final Image imgEdit;

    static {
        imgDone = new Image("/pics/btns/done.png", 28, 28, true, true);
        imgEdit = new Image("/pics/btns/edit2.png", 28, 28, true, true);
    }


    public ImgDone(ImageView ivDone, BooleanProperty doneProperty) {
        super(ivDone, imgEdit, imgDone);

        doneProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue) this.initImageToStateON();
            else this.initImageToStateOFF();
        });

    }
}
