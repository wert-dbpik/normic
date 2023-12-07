package ru.wert.normic.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImgDone extends ImgDouble{

    public static Image imgEdit; //Off
    public static Image imgDone; //On

    static {
        imgDone = new Image("/pics/btns/done.png");
        imgEdit = new Image("/pics/btns/edit2.png");
    }


    public ImgDone(ImageView ivDone, int size) {
        super(ivDone, imgEdit, imgDone, size);

    }
}
