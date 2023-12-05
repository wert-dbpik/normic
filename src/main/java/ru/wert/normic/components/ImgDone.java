package ru.wert.normic.components;

import javafx.beans.property.BooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImgDone extends ImgDouble{

    public static Image imgDone;
    public static Image imgEdit;


    public ImgDone(ImageView ivDone, BooleanProperty doneProperty, int size) {
        super();

        imgDone = new Image("/pics/btns/done.png", size, size, true, true);
        imgEdit = new Image("/pics/btns/edit2.png", size, size, true, true);

        super.setImage(ivDone);
        super.setImageOFF(imgEdit);
        super.setImageON(imgDone);

        setProperty(doneProperty.getValue());

        doneProperty.addListener((observable, oldValue, newValue) -> {
            setProperty(newValue);
        });
    }

    private void setProperty(boolean val){
        if(val) this.initImageToStateON();
        else this.initImageToStateOFF();
    }
}
