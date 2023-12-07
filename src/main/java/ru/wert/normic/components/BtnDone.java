package ru.wert.normic.components;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import ru.wert.normic.interfaces.IOpWithOperations;


public class BtnDone extends BtnDouble {

    public static final Image imgDone;
    public static final Image imgEdit;

    static {
        imgDone = new Image("/pics/btns/done.png", 28, 28, true, true);
        imgEdit = new Image("/pics/btns/edit2.png", 28, 28, true, true);
    }


    public BtnDone(Button btnDone) {
        super(btnDone,
                imgEdit, "Готово",
                imgDone, "Не готово");

    }
}
