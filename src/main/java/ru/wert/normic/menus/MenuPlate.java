package ru.wert.normic.menus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.controllers._forms.AbstractFormController;
import ru.wert.normic.controllers._forms.MainController;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.enums.EMenuSource;
import ru.wert.normic.interfaces.IOpWithOperations;

import java.util.List;

/**
 * МЕНЮ УДАЛЕНИЯ, КОПИРОВАНИЯ И ПЕРЕНОСА ОПЕРАЦИЙ
 */
public class MenuPlate {

    boolean showDone = false;
    boolean showCopy = true;
    boolean showCut = true;
    boolean showPaste = true;
    boolean showDelete = true;
    boolean showSave = true;

    public ContextMenu create(AbstractFormController formController, OpData opData, boolean cellIsEmpty){

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setId("contextMenu");

        MenuItem done = new MenuItem();
        if(opData instanceof IOpWithOperations){
            showDone = true;
            if(!((IOpWithOperations) opData).isDone()){
                done.setText("Готово");
                done.setOnAction(e->((IOpWithOperations) opData).setDone(true));
                done.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/done.png").toString(), 24, 24, true, true)));
            } else {
                done.setText("НЕ готово");
                done.setOnAction(e->((IOpWithOperations) opData).setDone(false));
                done.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/edit2.png").toString(), 24, 24, true, true)));
            }
        }

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

        MenuItem save = new MenuItem("Сохранить");
        if(!cellIsEmpty && opData instanceof IOpWithOperations) {
            List<OpData> addedOperations = ((IOpWithOperations) opData).getOperations();
            String initialName = ((IOpWithOperations) opData).getName();
            opData.setQuantity(1); //Количество меняем на 1
            save.setOnAction(e -> MainController.saveAs(opData, addedOperations, initialName, e, EMenuSource.FORM_MENU));
            save.setGraphic(new ImageView(new Image(getClass().getResource("/pics/btns/save.png").toString(), 24, 24, true, true)));
        } else
            showSave = false;

        List<VBox> selectedItems = formController.getListViewTechOperations().getSelectionModel().getSelectedItems();
        //Если буфер обмена пустой или вставка невозможна
        if(AbstractFormController.clipOpDataList.isEmpty() ||
                !formController.isPastePossible(cellIsEmpty))
            showPaste = false;
        if(cellIsEmpty){
            showCopy = false;
            showCut = false;
            showDelete = false;
        } else {
            //Если выделенных элементов не 1
            if(selectedItems.size() != 1) showPaste = false;
        }

        if (showDone) contextMenu.getItems().add(done);
        if (showDone) contextMenu.getItems().add(new SeparatorMenuItem());
        if (showCopy) contextMenu.getItems().add(copy);
        if (showCut) contextMenu.getItems().add(cut);
        if (showPaste) contextMenu.getItems().add(paste);
        if (showDelete) contextMenu.getItems().add(delete);
        if (showSave && (showCopy || showCut || showPaste || showDelete))
            contextMenu.getItems().add(new SeparatorMenuItem());

        if (showSave) contextMenu.getItems().add(save);


        return contextMenu;
    }

}
