package ru.wert.normic.menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuPlate {

    public ContextMenu create(){

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setId("contextMenu");

        MenuItem copy = new MenuItem("Копировать");
        copy.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/copy.png").toString(), 24, 24, true, true)));

        MenuItem cut = new MenuItem("Вырезать");
        cut.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/cut.png").toString(), 24, 24, true, true)));

        MenuItem paste = new MenuItem("Вставить");
        paste.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/paste.png").toString(), 24, 24, true, true)));

        contextMenu.getItems().addAll(copy, cut, paste);

        return contextMenu;
    }
}
