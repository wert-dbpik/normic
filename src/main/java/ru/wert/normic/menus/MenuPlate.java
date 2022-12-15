package ru.wert.normic.menus;

import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.wert.normic.controllers.AbstractOpPlate;

public class MenuPlate {

    private AbstractOpPlate plate;

    public ContextMenu create(AbstractOpPlate plate){
        this.plate = plate;

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setId("contextMenu");

        MenuItem copy = new MenuItem("Копировать");
        copy.setOnAction(plate::copyOperation);
        copy.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/copy.png").toString(), 24, 24, true, true)));

        MenuItem cut = new MenuItem("Вырезать");
        cut.setOnAction(plate::cutOperation);
        cut.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/cut.png").toString(), 24, 24, true, true)));

        MenuItem paste = new MenuItem("Вставить");
        paste.setOnAction(plate::pasteOperation);
        paste.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/paste.png").toString(), 24, 24, true, true)));

        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(plate::deleteOperation);
        delete.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/close.png").toString(), 24, 24, true, true)));

        if(!plate.isPastePossible(null)) paste.setDisable(true);
        contextMenu.getItems().addAll(copy, cut, paste, delete);


        return contextMenu;
    }

}
