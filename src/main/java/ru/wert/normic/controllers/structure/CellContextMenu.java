package ru.wert.normic.controllers.structure;

import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.wert.normic.components.ImgDouble;
import ru.wert.normic.entities.ops.OpData;
import ru.wert.normic.entities.ops.single.OpAssm;
import ru.wert.normic.entities.ops.single.OpDetail;
import ru.wert.normic.entities.ops.single.OpPack;
import ru.wert.normic.enums.EOpType;
import ru.wert.normic.interfaces.IOpWithOperations;

public class CellContextMenu extends ContextMenu{

    static final int size = 20; //Размер для пиктограмм
    private final TreeItem<OpData> selectedItem;
    private final TreeItem<OpData> parentItem;
    private final StructureController controller;
    private final TextField tfName;
    private final TextField tfN;
    private final ImgDouble imgDone;

    private static OpData clipOpData; //Объект в клипборде
    private boolean cuttingOn; //Флаг операции вырезания

    boolean useEdit = true;
    boolean useCopy = true;
    boolean useCut = true;
    boolean usePaste = true;
    boolean useDelete = true;

    private Manipulator manipulator;


    public CellContextMenu(TreeItem<OpData> selectedItem, TreeItem<OpData> parentItem, StructureController controller,
                           TextField tfName, TextField tfN, ImgDouble imgDone) {
        this.selectedItem = selectedItem;
        this.parentItem = parentItem;
        this.controller = controller;
        this.tfName = tfName;
        this.tfN = tfN;
        this.imgDone = imgDone;

        manipulator = new Manipulator(controller);

        createMenu();
    }

    private void createMenu() {

        MenuItem edit = new MenuItem("Редактировать");
        edit.setOnAction(manipulator::editItem);
        edit.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/edit.png").toString(), size, size, true, true)));

        MenuItem copy = new MenuItem("Копировать");
        copy.setOnAction(manipulator::copyOperation);
        copy.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/copy.png").toString(), size, size, true, true)));

        MenuItem cut = new MenuItem("Вырезать");
        cut.setOnAction(manipulator::cutOperation);
        cut.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/cut.png").toString(), size, size, true, true)));

        MenuItem paste = new MenuItem("Вставить");
        paste.setOnAction(manipulator::pasteOperation);
        paste.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/paste.png").toString(), size, size, true, true)));

        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(manipulator::deleteItem);
        delete.setGraphic(new ImageView(new Image(getClass().getResource(
                "/pics/btns/close.png").toString(), size, size, true, true)));

        if(selectedItem.equals(controller.getTreeView().getRoot())){
            useCut = false;
            useDelete = false;
        }

        if(clipOpData == null) usePaste = false;

        if(useEdit) getItems().add(edit);
        if(useCopy) getItems().add(copy);
        if(useCut) getItems().add(cut);
        if(usePaste) getItems().add(paste);
        if(useDelete) getItems().add(new SeparatorMenuItem());
        if(useDelete) getItems().add(delete);
    }

}
