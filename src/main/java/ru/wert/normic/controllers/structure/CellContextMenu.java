package ru.wert.normic.controllers.structure;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.wert.normic.entities.ops.OpData;

public class CellContextMenu extends ContextMenu{

    static final int size = 20; //Размер для пиктограмм
    private final TreeItem<OpData> selectedItem;
    private final StructureController controller;


    private static OpData clipOpData; //Объект в клипборде
    private boolean cuttingOn; //Флаг операции вырезания

    boolean useEdit = true;
    boolean useCopy = true;
    boolean useCut = true;
    boolean usePaste = true;
    boolean useCancel = true;
    boolean useDelete = true;

    private final Manipulator manipulator;


    public CellContextMenu(TreeItem<OpData> selectedItem, StructureController controller) {
        this.selectedItem = selectedItem;
        this.controller = controller;

        manipulator = new Manipulator(controller);

        createMenu();
    }

    private void createMenu() {

        MenuItem edit = new MenuItem("Редактировать (Ctrl+F)");
        edit.setOnAction(manipulator::editItem);
        edit.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/edit.png").toString(), size, size, true, true)));

        MenuItem copy = new MenuItem("Копировать (Ctrl+C)");
        copy.setOnAction(manipulator::copyOperation);
        copy.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/copy.png").toString(), size, size, true, true)));

        MenuItem cut = new MenuItem("Вырезать");
        cut.setOnAction(manipulator::cutOperation);
        cut.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/cut.png").toString(), size, size, true, true)));

        MenuItem paste = new MenuItem("Вставить (Ctrl+V)");
        paste.setOnAction(manipulator::pasteOperation);
        paste.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/paste.png").toString(), size, size, true, true)));

        MenuItem delete = new MenuItem("Удалить (Del)");
        delete.setOnAction(manipulator::deleteItem);
        delete.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/close.png").toString(), size, size, true, true)));

        MenuItem cancelCutting = new MenuItem("Отменить (Ctrl+X)");
        cancelCutting.setOnAction(manipulator::cancelPasting);
        cancelCutting.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/cancel.png").toString(), size, size, true, true)));

        if(selectedItem.equals(controller.getTreeView().getRoot())){
            useCut = false;
            useDelete = false;
        }

        if(Manipulator.clipItem == null)
            usePaste = false;
        if(Manipulator.cuttedPosition == null)
            useCancel = false;

        if(useEdit) getItems().add(edit);
        if(useCopy) getItems().add(copy);
        if(useCut) getItems().add(cut);
        if(usePaste) getItems().add(paste);
        if(useCancel) getItems().add(cancelCutting);
        if(useDelete) getItems().add(new SeparatorMenuItem());
        if(useDelete) getItems().add(delete);
    }

}
