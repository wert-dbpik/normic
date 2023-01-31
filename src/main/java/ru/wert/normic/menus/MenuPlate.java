package ru.wert.normic.menus;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ru.wert.normic.controllers.AbstractOpPlate;
import ru.wert.normic.controllers.forms.AbstractFormController;

import java.util.List;

public class MenuPlate {

    boolean showCopy = true;
    boolean showCut = true;
    boolean showPaste = true;
    boolean showDelete = true;

    public ContextMenu create(AbstractFormController formController, boolean cellIsEmpty){

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setId("contextMenu");

        MenuItem copy = new MenuItem("Копировать");
        copy.setOnAction(formController::copyOperation);
        copy.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/copy.png").toString(), 24, 24, true, true)));

        MenuItem cut = new MenuItem("Вырезать");
        cut.setOnAction(formController::cutOperation);
        cut.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/cut.png").toString(), 24, 24, true, true)));

        MenuItem paste = new MenuItem("Вставить");
        paste.setOnAction(e->formController.pasteOperation(cellIsEmpty));
        paste.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/paste.png").toString(), 24, 24, true, true)));

        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(formController::deleteSelectedOperation);
        delete.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/close.png").toString(), 24, 24, true, true)));

        List<VBox> selectedItems = formController.getListViewTechOperations().getSelectionModel().getSelectedItems();
        //Если буфер обмена пустой или вставка невозможна
        if(AbstractFormController.clipOpDataList.isEmpty() ||
                !formController.isPastePossible(null))
            showPaste = false;
        if(cellIsEmpty){
            showCopy = false;
            showCut = false;
            showDelete = false;
        } else {
            //Если выделенных элементов не 1
            if(selectedItems.size() != 1) showPaste = false;
        }

        if(showCopy) contextMenu.getItems().add(copy);
        if(showCut) contextMenu.getItems().add(cut);
        if(showPaste) contextMenu.getItems().add(paste);
        if(showDelete) contextMenu.getItems().add(delete);


        return contextMenu;
    }

}
